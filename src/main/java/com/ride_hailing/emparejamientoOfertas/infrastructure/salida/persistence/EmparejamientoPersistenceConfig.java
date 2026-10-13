package com.ride_hailing.emparejamientoOfertas.infrastructure.salida.persistence;

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
    basePackages = "com.ride_hailing.emparejamientoOfertas.infrastructure.salida.persistence",
    entityManagerFactoryRef = "emparejamientoEntityManagerFactory",
    transactionManagerRef = "emparejamientoTransactionManager"
)
class EmparejamientoPersistenceConfig {

    @Bean(name = "emparejamientoDataSource")
    DataSource emparejamientoDataSource(
            @Value("${app.datasource.emparejamiento.url}") String url,
            @Value("${app.datasource.emparejamiento.driver-class-name}") String driverClassName,
            @Value("${app.datasource.emparejamiento.username}") String username,
            @Value("${app.datasource.emparejamiento.password}") String password) {
        return DataSourceBuilder.create()
                .url(url)
                .driverClassName(driverClassName)
                .username(username)
                .password(password)
                .build();
    }

    @Bean(name = "emparejamientoEntityManagerFactory")
    LocalContainerEntityManagerFactoryBean emparejamientoEntityManagerFactory(@Qualifier("emparejamientoDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("com.ride_hailing.emparejamientoOfertas.infrastructure.salida.persistence");
        factory.setPersistenceUnitName("emparejamiento");
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", "update");
        factory.setJpaProperties(jpaProperties);

        return factory;
    }

    @Bean(name = "emparejamientoTransactionManager")
    PlatformTransactionManager emparejamientoTransactionManager(
            @Qualifier("emparejamientoEntityManagerFactory") EntityManagerFactory emparejamientoEntityManagerFactory) {
        return new JpaTransactionManager(emparejamientoEntityManagerFactory);
    }
}
