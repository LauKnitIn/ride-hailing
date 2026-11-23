package com.ride_hailing.tarifas.infraestructure.salida.persistence;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableJpaRepositories(
    basePackages = "com.ride_hailing.tarifas.infraestructure.salida.persistence",
    entityManagerFactoryRef = "tarifasEntityManagerFactory",
    transactionManagerRef = "tarifasTransactionManager"
)
class TarifasPersistenceConfig {

    @Bean(name = "tarifasDataSource")
    DataSource tarifasDataSource(
            @Value("${app.datasource.tarifas.url}") String url,
            @Value("${app.datasource.tarifas.driver-class-name}") String driverClassName,
            @Value("${app.datasource.tarifas.username}") String username,
            @Value("${app.datasource.tarifas.password}") String password) {
        return DataSourceBuilder.create()
                .url(url)
                .driverClassName(driverClassName)
                .username(username)
                .password(password)
                .build();
    }

    @Bean(name = "tarifasEntityManagerFactory")
    LocalContainerEntityManagerFactoryBean tarifasEntityManagerFactory(@Qualifier("tarifasDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("com.ride_hailing.tarifas.infraestructure.salida.persistence");
        factory.setPersistenceUnitName("tarifas");
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", "update");
        factory.setJpaProperties(jpaProperties);

        return factory;
    }

    @Bean(name = "tarifasTransactionManager")
    PlatformTransactionManager tarifasTransactionManager(
            @Qualifier("tarifasEntityManagerFactory") EntityManagerFactory tarifasEntityManagerFactory) {
        return new JpaTransactionManager(tarifasEntityManagerFactory);
    }
}
