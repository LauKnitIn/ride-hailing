package com.ride_hailing.pasajeros.infraestructura.salida.persistencia;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(
    basePackages = "com.ride_hailing.pasajeros.infraestructura.salida.persistencia",
    entityManagerFactoryRef = "pasajerosEntityManagerFactory",
    transactionManagerRef = "pasajerosTransactionManager"
)
public class PasajerosPersistenceConfig {

    @Bean(name = "pasajerosDataSource")
    public DataSource pasajerosDataSource(
            @Value("${app.datasource.pasajeros.url}") String url,
            @Value("${app.datasource.pasajeros.driver-class-name}") String driverClassName,
            @Value("${app.datasource.pasajeros.username}") String username,
            @Value("${app.datasource.pasajeros.password}") String password) {
        return DataSourceBuilder.create()
                .url(url)
                .driverClassName(driverClassName)
                .username(username)
                .password(password)
                .build();
    }

    @Bean(name = "pasajerosEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean pasajerosEntityManagerFactory(
            DataSource pasajerosDataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(pasajerosDataSource);
        factory.setPackagesToScan("com.ride_hailing.pasajeros.dominio");
        factory.setPersistenceUnitName("pasajeros");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factory.setJpaVendorAdapter(vendorAdapter);

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", "update");
        jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        factory.setJpaProperties(jpaProperties);

        return factory;
    }

    @Bean(name = "pasajerosTransactionManager")
    public PlatformTransactionManager pasajerosTransactionManager(
            LocalContainerEntityManagerFactoryBean pasajerosEntityManagerFactory) {
        return new JpaTransactionManager(pasajerosEntityManagerFactory.getObject());
    }
}
