package com.codewarrior.csc686.project.configuration;

import com.codewarrior.csc686.project.model.Person;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class CommonConfiguration extends WebMvcConfigurerAdapter {

    public static final String ENCRYPTION_ALGORITHM = "PBEWithMD5AndTripleDES";

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

}
