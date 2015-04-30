package com.codewarrior.csc686.project.service;

import freemarker.template.TemplateException;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.Message;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Service
public class Mailer {

    private static final Logger LOG = LoggerFactory.getLogger(Mailer.class);

    @Autowired
    private Email email;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;



    @Async
    public void sendConfirmationEmail(FreemarkerEmailModel freemarkerEmailModel) throws UnsupportedEncodingException, AddressException, javax.mail.MessagingException, EmailException {


        StringBuffer content = new StringBuffer();
        Map<String,FreemarkerEmailModel> model = new HashMap<>();
        model.put("freemarkerEmailModel", freemarkerEmailModel);

        try {
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerConfigurer.getConfiguration().getTemplate(freemarkerEmailModel.getTemplateName()), model));
        } catch (IOException e) {
            LOG.error("error thrown in getting template", e);
        } catch (TemplateException e) {
            LOG.error("error thrown in getting template", e);
        }

        email.setSubject(freemarkerEmailModel.getSubject());
        email.setMsg(content.toString());
        email.addTo(freemarkerEmailModel.getToEmail());
        email.send();

    }

}
