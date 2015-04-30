package com.codewarrior.csc686.project.configuration;

import com.codewarrior.csc686.project.model.Person;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;
import java.util.Properties;

@Configuration
public class CommonConfiguration extends WebMvcConfigurerAdapter {

    public static final String ENCRYPTION_ALGORITHM = "PBEWithMD5AndTripleDES";

    @Autowired
   	private Environment env;

    @Autowired
   	private LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean;


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        converters.add(converter());
        converters.add(jaxbMessageConverter());
        super.configureMessageConverters(converters);
    }


    @Bean
    public MappingJackson2HttpMessageConverter converter() {

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper());
        return converter;
    }

    @Bean
    public Jaxb2RootElementHttpMessageConverter jaxbMessageConverter() {

        return new Jaxb2RootElementHttpMessageConverter();
    }

    @Bean
    public ObjectMapper objectMapper() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        objectMapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);

        objectMapper.registerSubtypes(Person.class);
        return objectMapper;
    }


    @Bean
    public StandardPBEStringEncryptor standardPBEStringEncryptor() {

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm(ENCRYPTION_ALGORITHM);

        String password = System.getProperty("SYSTEM_PASSWORD");
        if (StringUtils.isEmpty(password)) {
            password = System.getenv("SYSTEM_PASSWORD");
        }
        encryptor.setPassword(password);
        return encryptor;
    }

    @Bean
   	public Email email() throws EmailException {

        Email email = new SimpleEmail();
        email.setHostName(env.getProperty( "mail.host" ));
        email.setSmtpPort(Integer.parseInt(env.getProperty("mail.port")));
        email.setAuthenticator(new DefaultAuthenticator(env.getProperty( "mail.username" ), env.getProperty( "mail.password" )));
        email.setSSL(true);
        email.setFrom(env.getProperty( "mail.username" ));

        return email;
   	}

    @Bean
   	@Primary
   	@Autowired
   	public JpaTransactionManager transactionManager() throws Exception
   	{
   		JpaTransactionManager txManager = new JpaTransactionManager();
   		txManager.setEntityManagerFactory(localContainerEntityManagerFactoryBean.getObject());
   		return txManager;
   	}



}
