package com.ride_hailing.calificacion.infraestructure.salida.persistencia;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
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

@Configuration
@EnableJpaRepositories(
        basePackages = "com.ride_hailing.calificacion.infraestructure.salida.persistencia",
        entityManagerFactoryRef = "calificacionesEntityManagerFactory",
        transactionManagerRef = "calificacionesTransactionManager"
)
public class CalificacionesPersistenceConfig {

    @Bean(name = "calificacionesDataSource")
    public DataSource calificacionesDataSource(
            @Value("${app.datasource.calificaciones.url}") String url,
            @Value("${app.datasource.calificaciones.driver-class-name}") String driverClassName,
            @Value("${app.datasource.calificaciones.username}") String username,
            @Value("${app.datasource.calificaciones.password}") String password) {
        return DataSourceBuilder.create()
                .url(url)
                .driverClassName(driverClassName)
                .username(username)
                .password(password)
                .build();
    }

    @Bean(name = "calificacionesEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean calificacionesEntityManagerFactory(
            @Qualifier("calificacionesDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("com.ride_hailing.calificacion.domain");
        factory.setPersistenceUnitName("calificaciones");
        factory.setJpaVendorAdapter(jpaVendorAdapter());
        factory.setJpaProperties(jpaProperties());
        return factory;
    }

    @Bean(name = "calificacionesTransactionManager")
    public PlatformTransactionManager calificacionesTransactionManager(
            @Qualifier("calificacionesEntityManagerFactory")
            LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory.getObject());
    }

    private JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    private Properties jpaProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        return properties;
    }
}
