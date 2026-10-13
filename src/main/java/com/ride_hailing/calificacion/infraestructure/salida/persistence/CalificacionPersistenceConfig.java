package com.ride_hailing.calificacion.infraestructure.salida.persistence;

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
    basePackages = "com.ride_hailing.calificacion.infraestructure.salida.persistence",
    entityManagerFactoryRef = "calificacionEntityManagerFactory",
    transactionManagerRef = "calificacionTransactionManager"
)
class CalificacionPersistenceConfig {

    @Bean(name = "calificacionDataSource")
    DataSource calificacionDataSource(
            @Value("${app.datasource.calificacion.url}") String url,
            @Value("${app.datasource.calificacion.driver-class-name}") String driverClassName,
            @Value("${app.datasource.calificacion.username}") String username,
            @Value("${app.datasource.calificacion.password}") String password) {
        return DataSourceBuilder.create()
                .url(url)
                .driverClassName(driverClassName)
                .username(username)
                .password(password)
                .build();
    }

    @Bean(name = "calificacionEntityManagerFactory")
    LocalContainerEntityManagerFactoryBean calificacionEntityManagerFactory(@Qualifier("calificacionDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("com.ride_hailing.calificacion.infraestructure.salida.persistence");
        factory.setPersistenceUnitName("calificacion");
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", "update");
        factory.setJpaProperties(jpaProperties);

        return factory;
    }

    @Bean(name = "calificacionTransactionManager")
    PlatformTransactionManager calificacionTransactionManager(
            @Qualifier("calificacionEntityManagerFactory") EntityManagerFactory calificacionEntityManagerFactory) {
        return new JpaTransactionManager(calificacionEntityManagerFactory);
    }
}
