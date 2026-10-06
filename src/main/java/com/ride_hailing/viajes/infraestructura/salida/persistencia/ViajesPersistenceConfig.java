package com.ride_hailing.viajes.infraestructura.salida.persistencia;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
    basePackages = "com.ride_hailing.viajes.infraestructura.salida.persistencia",
    entityManagerFactoryRef = "viajesEntityManagerFactory",
    transactionManagerRef = "viajesTransactionManager"
)
public class ViajesPersistenceConfig {

    @Primary
    @Bean(name = "viajesDataSource")
    public DataSource viajesDataSource(
            @Value("${app.datasource.viajes.url}") String url,
            @Value("${app.datasource.viajes.driver-class-name}") String driverClassName,
            @Value("${app.datasource.viajes.username}") String username,
            @Value("${app.datasource.viajes.password}") String password) {
        return DataSourceBuilder.create()
                .url(url)
                .driverClassName(driverClassName)
                .username(username)
                .password(password)
                .build();
    }

    @Primary
    @Bean(name = "viajesEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean viajesEntityManagerFactory(
            DataSource viajesDataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(viajesDataSource);
        factory.setPackagesToScan("com.ride_hailing.viajes.dominio");
        factory.setPersistenceUnitName("viajes");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factory.setJpaVendorAdapter(vendorAdapter);

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", "update");
        jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        factory.setJpaProperties(jpaProperties);

        return factory;
    }

    @Primary
    @Bean(name = "viajesTransactionManager")
    public PlatformTransactionManager viajesTransactionManager(
            LocalContainerEntityManagerFactoryBean viajesEntityManagerFactory) {
        return new JpaTransactionManager(viajesEntityManagerFactory.getObject());
    }
}
