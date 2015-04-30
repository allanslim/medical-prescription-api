package com.codewarrior.csc686.project;

import com.codewarrior.csc686.project.configuration.CommonConfiguration;
import com.codewarrior.csc686.project.service.EmailValidatorService;
import com.codewarrior.csc686.project.service.FreemarkerEmailModel;
import com.codewarrior.csc686.project.service.Mailer;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;


@SpringBootApplication
@Import({CommonConfiguration.class})
public class EntryPoint extends WebMvcConfigurerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(EntryPoint.class);



    public static void main(String[] args) {

        ApplicationContext ctx = SpringApplication.run(EntryPoint.class, args);

//        System.out.println(Arrays.asList(ctx.getBeanDefinitionNames()));

        EmailValidatorService emailValidatorService = (EmailValidatorService) ctx.getBean("emailValidatorService");

//        boolean doesExist = emailValidatorService.doesEmailExist("allanslim@gmail.com");
//        LOG.info(" allanslim@gmail.com exist?" + doesExist);
//
//        doesExist = emailValidatorService.doesEmailExist("rlkcpo@gmail.com");
//        LOG.info(" rlkcpo@gmail.com exist?" + doesExist);


//        LoginService loginService = (LoginService) ctx.getBean("loginService");
//        String token = loginService.login("chong@lee.com", "abc123");
//
//        LOG.info("the token is: " + token);


//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//
//        RegisterUserService registerUserService = (RegisterUserService) ctx.getBean("registerUserService");
//
//        RegisterUserInput registerUserInput = new RegisterUserInput();
//        registerUserInput.groupId = 1;
//        registerUserInput.insuranceId = "2222223";
//        registerUserInput.firstName = "CHONG";
//        registerUserInput.lastName = "LEE";
//
//        long dateInMillis = 0;
//
//        try {
//            dateInMillis = sdf.parse("1966-04-22").getTime();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        registerUserInput.birthday = new Date(dateInMillis);
//
//        registerUserInput.email = "chong@lee.com";
//        registerUserInput.password = "abc123";
//        registerUserInput.seqA1 = "this is a test";
//        registerUserInput.seqQ1 = "answer1";
//        registerUserInput.seqA2 = "this is a test2";
//        registerUserInput.seqQ2 = "answer2";
//        registerUserInput.seqA3 = "this is a test3";
//        registerUserInput.seqQ3 = "answer3";
//
//
//        registerUserService.registerUser(registerUserInput);

//
//        Mailer mailer = (Mailer) ctx.getBean("mailer");
//
//        FreemarkerEmailModel freemarkerEmailModel = new FreemarkerEmailModel.Builder().setToEmail("allanslim@gmail.com").setName("Allan").setUrl("http://testblah.com").
//                setSubject("registration").setFromEmail("admin@ec2-54-145-194-211.compute-1.amazonaws.com").setTemplateName(FreemarkerEmailModel.VERIFICATION_TEMPLATE).build();
//
//        FreemarkerEmailModel freemarkerEmailModel2 = new FreemarkerEmailModel.Builder().setToEmail("allanslim@gmail.com").setName("Allan").setUrl("http://testblah.com").
//                setSubject("registration & hotdog").setFromEmail("admin@ec2-54-145-194-211.compute-1.amazonaws.com").setTemplateName(FreemarkerEmailModel.VERIFICATION_TEMPLATE).build();
//
//
//        try {
//            mailer.sendConfirmationEmail(freemarkerEmailModel);
//            mailer.sendConfirmationEmail(freemarkerEmailModel2);
//            LOG.info("### EMAIL SENT##");
//
//        } catch (UnsupportedEncodingException e) {
//            LOG.info("### EMAIL NOT SENT##");
//            e.printStackTrace();
//        } catch (MessagingException e) {
//            LOG.info("### EMAIL NOT SENT##");
//            e.printStackTrace();
//        } catch (EmailException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {

        configurer.setUseRegisteredSuffixPatternMatch(true);
    }

//    @Bean
//    public EmbeddedServletContainerCustomizer containerCustomizer(@Value("${keystore.file}") String keystoreFile, @Value("${keystore.password}") String keystorePassword, @Value("${keystore.type}") String keystoreType, @Value("${keystore.alias}") String keystoreAlias) throws FileNotFoundException {
//
//        final String absoluteKeystoreFile = ResourceUtils.getFile(keystoreFile).getAbsolutePath();
//
//        return (ConfigurableEmbeddedServletContainer factory) -> {
//            TomcatEmbeddedServletContainerFactory containerFactory = (TomcatEmbeddedServletContainerFactory) factory;
//            containerFactory.addConnectorCustomizers((TomcatConnectorCustomizer) (Connector connector) -> {
//                connector.setSecure(true);
//                connector.setScheme("https");
//                connector.setAttribute("keystoreFile", absoluteKeystoreFile);
//                connector.setAttribute("keystorePass", standardPBEStringEncryptor.decrypt(keystorePassword));
//                connector.setAttribute("keystoreType", keystoreType);
//                connector.setAttribute("keyAlias", keystoreAlias);
//                connector.setAttribute("clientAuth", "false");
//                connector.setAttribute("sslProtocol", "TLS");
//                connector.setAttribute("SSLEnabled", true);
//            });
//        };
//    }
}