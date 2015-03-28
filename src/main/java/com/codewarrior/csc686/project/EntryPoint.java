package com.codewarrior.csc686.project;

import com.codewarrior.csc686.project.configuration.CommonConfiguration;
import org.apache.catalina.connector.Connector;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.FileNotFoundException;
import java.util.Arrays;


@SpringBootApplication
@Import( {CommonConfiguration.class})
public class EntryPoint extends WebMvcConfigurerAdapter {



    @Autowired
   	private StandardPBEStringEncryptor standardPBEStringEncryptor;



    public static void main(String[] args) {

        ApplicationContext ctx = SpringApplication.run(EntryPoint.class, args);

    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseRegisteredSuffixPatternMatch(true);
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer(@Value("${keystore.file}") String keystoreFile, @Value("${keystore.password}") String keystorePassword, @Value("${keystore.type}") String keystoreType, @Value("${keystore.alias}") String keystoreAlias) throws FileNotFoundException {

        final String absoluteKeystoreFile = ResourceUtils.getFile(keystoreFile).getAbsolutePath();

        return (ConfigurableEmbeddedServletContainer factory) -> {
            TomcatEmbeddedServletContainerFactory containerFactory = (TomcatEmbeddedServletContainerFactory) factory;
            containerFactory.addConnectorCustomizers((TomcatConnectorCustomizer) (Connector connector) -> {
                connector.setSecure(true);
                connector.setScheme("https");
                connector.setAttribute("keystoreFile", absoluteKeystoreFile);
                connector.setAttribute("keystorePass", standardPBEStringEncryptor.decrypt(keystorePassword));
                connector.setAttribute("keystoreType", keystoreType);
                connector.setAttribute("keyAlias", keystoreAlias);
                connector.setAttribute("clientAuth", "false");
                connector.setAttribute("sslProtocol", "TLS");
                connector.setAttribute("SSLEnabled", true);
            });
        };
    }
}