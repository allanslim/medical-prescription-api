package com.codewarrior.csc686.project.configuration;


import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class JpaPersistenceConfig {
	@Autowired
	private Environment env;

	@Autowired
	private StandardPBEStringEncryptor standardPBEStringEncryptor;

	@Bean
	@Primary
	@Autowired
	public JpaTransactionManager transactionManager() throws Exception
	{
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory( entityManagerFactory().getObject() );
		return txManager;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws Exception
	{
		LocalContainerEntityManagerFactoryBean lcemfb = new LocalContainerEntityManagerFactoryBean();

		lcemfb.setJpaVendorAdapter(jpaVendorAdapter());
		lcemfb.setDataSource( jpaDataSource() );
		lcemfb.setPersistenceUnitName("PU");

        lcemfb.setJpaProperties( createProperties() );

        lcemfb.setPackagesToScan( "com.codewarrior.csc686.project.entity" );
		return lcemfb;
	}


    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

	@Bean
	public DataSource jpaDataSource() throws Exception
	{
		String username = standardPBEStringEncryptor.decrypt(env.getProperty("db.username"));
		String password = standardPBEStringEncryptor.decrypt(env.getProperty("db.password"));
		String jdbcUrl = standardPBEStringEncryptor.decrypt(env.getProperty( "db.jdbc.url" ));

		ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
		comboPooledDataSource.setDriverClass( "oracle.jdbc.driver.OracleDriver" );
		comboPooledDataSource.setJdbcUrl( jdbcUrl );
		comboPooledDataSource.setUser( username);
		comboPooledDataSource.setPassword(password);
		comboPooledDataSource.setAcquireIncrement( env.getProperty(
				"hibernate.c3p0.acquireIncrement",
				Integer.class ) );
		comboPooledDataSource.setMinPoolSize( env.getProperty(
				"hibernate.c3p0.minsize",
				Integer.class ) );
		comboPooledDataSource.setMaxPoolSize( env.getProperty(
				"hibernate.c3p0.maxsize",
				Integer.class ) );
		comboPooledDataSource.setMaxIdleTime( env.getProperty(
				"hibernate.c3p0.timeout",
				Integer.class ) );
		comboPooledDataSource.setMaxStatementsPerConnection( env.getProperty(
				"hibernate.c3p0.maxstatements",
				Integer.class ) );
		return comboPooledDataSource;
	}

    private Properties createProperties() {

        Properties jpaProperties = new Properties();
        jpaProperties.put( "jpaDialect", "org.springframework.orm.jpa.vendor.HibernateJpaDialect" );
        jpaProperties.put( "hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect" );
        jpaProperties.put( "hibernate.format_sql", env.getProperty( "hibernate.format.sql" ) );
        jpaProperties.put( "hibernate.connection.autocommit", false );
        jpaProperties.put( "hibernate.show_sql", env.getProperty( "hibernate.show.sql" ) );

        jpaProperties.put( "hibernate.cache.use_second_level_cache", true );
        jpaProperties.put( "hibernate.cache.use_query_cache", false );
        jpaProperties.put(
                "hibernate.cache.region.factory_class",
                "org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory" );
        return jpaProperties;
    }

}
