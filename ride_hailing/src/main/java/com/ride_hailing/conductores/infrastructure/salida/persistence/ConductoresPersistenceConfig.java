package com.ride_hailing.conductores.infrastructure.salida.persistence;

import java.util.Properties;

import javax.sql.DataSource;

import jakarta.persistence.EntityManagerFactory;

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

@Configuration
@EnableJpaRepositories(
    basePackages = "com.ride_hailing.conductores.infrastructure.salida.persistence",
    entityManagerFactoryRef = "conductoresEntityManagerFactory",
    transactionManagerRef = "conductoresTransactionManager"
)
class ConductoresPersistenceConfig {

    @Bean(name = "conductoresDataSource")
    DataSource conductoresDataSource(
            @Value("${app.datasource.conductores.url}") String url,
            @Value("${app.datasource.conductores.driver-class-name}") String driverClassName,
            @Value("${app.datasource.conductores.username}") String username,
            @Value("${app.datasource.conductores.password}") String password) {
        return DataSourceBuilder.create()
                .url(url)
                .driverClassName(driverClassName)
                .username(username)
                .password(password)
                .build();
    }

    @Bean(name = "conductoresEntityManagerFactory")
    LocalContainerEntityManagerFactoryBean conductoresEntityManagerFactory(@Qualifier("conductoresDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("com.ride_hailing.conductores.infrastructure.salida.persistence");
        factory.setPersistenceUnitName("conductores");
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", "update");
        factory.setJpaProperties(jpaProperties);

        return factory;
    }

    @Bean(name = "conductoresTransactionManager")
    PlatformTransactionManager conductoresTransactionManager(
            @Qualifier("conductoresEntityManagerFactory") EntityManagerFactory conductoresEntityManagerFactory) {
        return new JpaTransactionManager(conductoresEntityManagerFactory);
    }
}
