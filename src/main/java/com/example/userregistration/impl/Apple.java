package com.example.userregistration.impl;

package com.ubs.CTI.configuration;

import com.ubs.frmwj2.crypto.impl.CryptoUtils;
import oracle.jdbc.pool.OracleDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.ubs.CTI.repo.staging") // Enable repositories in this package
public class StageDataSourceConfig {

    @Value("${spring.datasource.stage.keystore.path}")
    private String stageKeystorePath;

    @Value("${spring.datasource.stage.keystore.password}")
    private String stageKeystorePassword;

    @Value("${spring.datasource.stage.truststore.path}")
    private String stageTruststorePath;

    @Value("${spring.datasource.stage.truststore.password}")
    private String stageTruststorePassword;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${ffl.key}")
    private String keyfile;

    @Primary
    @Bean
    public DataSource stageDataSource() throws SQLException {
        OracleDataSource ds = new OracleDataSource();

        Properties props = new Properties();
        props.put("oracle.net.authentication_services", "(TCPS)");
        props.put("oracle.net.ssl_version", "1.2");
        props.put("javax.net.ssl.keyStoreType", "JKS");
        props.put("javax.net.ssl.trustStoreType", "JKS");

        // Keystore and Truststore configuration
        props.put("javax.net.ssl.keyStore", stageKeystorePath);
        props.put("javax.net.ssl.keyStorePassword", CryptoUtils.decrypt(stageKeystorePassword, keyfile));
        props.put("javax.net.ssl.trustStore", stageTruststorePath);
        props.put("javax.net.ssl.trustStorePassword", CryptoUtils.decrypt(stageTruststorePassword, keyfile));
        props.put("oracle.net.crypto_checksum_client", "REJECTED");

        ds.setConnectionProperties(props);
        ds.setURL(url);

        return ds;
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("stageDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan("com.ubs.CTI.entity.staging"); // Specify the package to scan for entities
        factoryBean.setJpaProperties(hibernateProperties());
        return factoryBean;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.Oracle12cDialect");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.hbm2ddl.auto", "update");
        return properties;
    }

    @Primary
    @Bean
    public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public JdbcTemplate stageJdbcTemplate(@Qualifier("stageDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public NamedParameterJdbcTemplate stageNamedJdbcTemplate(@Qualifier("stageDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    // Exception translation for JPA
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
