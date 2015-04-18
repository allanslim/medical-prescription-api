package com.codewarrior.csc686.project.endpoint;

import com.codewarrior.csc686.project.exception.BadRequestException;
import com.codewarrior.csc686.project.model.RegisterUserInput;
import com.codewarrior.csc686.project.service.FreemarkerEmailModel;
import com.codewarrior.csc686.project.service.Mailer;
import com.codewarrior.csc686.project.service.RegisterUserService;
import com.codewarrior.csc686.project.service.UserService;
import org.apache.catalina.connector.Request;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping( value = "/user" )
public class UserEndpoint {



    private static final Logger LOG = LoggerFactory.getLogger(UserEndpoint.class);

    @Value("${middletier.host}")
    private String host;

    @Autowired
    private UserService userService;

    @Autowired
    private RegisterUserService registerUserService;

    @Autowired
    private Mailer mailer;


    @RequestMapping( method = RequestMethod.POST, value = "/login")
    public Map<String, String> login( @RequestParam(value = "email") String email,
                                      @RequestParam("password") String password) throws SQLException {

        validate(email, password);

        String token = userService.login(email, password);

        Map<String,String> response = new HashMap<>();
        response.put("token", token);

        return response;
    }

    @RequestMapping( method = RequestMethod.POST, value = "/registration")
    public Map<String, String> register(@RequestBody RegisterUserInput registerUserInput) throws SQLException, ParseException {

        String token = registerUserService.registerUser(registerUserInput);


        sendEmail(registerUserInput, token);


        Map<String,String> response = new HashMap<>();
        response.put("isRegistrationSuccessful", "true");

        return response;
    }

    @RequestMapping( method = RequestMethod.POST, value = "/validateMember")
    public Map<String, String> isMemberValid(@RequestBody RegisterUserInput registerUserInput) throws SQLException, ParseException {

        boolean isMemberValid = registerUserService.isMemberValid(registerUserInput);

        Map<String,String> response = new HashMap<>();

        response.put("isMemberValid", String.valueOf(isMemberValid));

        return response;
    }

    @RequestMapping( method = RequestMethod.POST, value = "isEmailAvailable")
    public Map<String, String> isEmailAvailable( @RequestBody  RegisterUserInput registerUserInput) throws SQLException, ParseException {

        boolean isEmailAvailable = registerUserService.isEmailAvailable(registerUserInput.email);

        Map<String,String> response = new HashMap<>();

        response.put("isEmailAvailable", String.valueOf(isEmailAvailable));

        return response;
    }


    private void sendEmail(RegisterUserInput registerUserInput, String token) {

        try {
            mailer.sendConfirmationEmail(createEmailMode(registerUserInput, token));

        } catch (UnsupportedEncodingException e ) {

            LOG.error(String.format("Error sending confirmation email for user: %s", registerUserInput.email), e);

        } catch (MessagingException e) {

            LOG.error(String.format("Error sending confirmation email for user: %s", registerUserInput.email), e);
        }
    }

    private FreemarkerEmailModel createEmailMode(RegisterUserInput registerUserInput, String token) {

        FreemarkerEmailModel freemarkerEmailModel = new FreemarkerEmailModel.Builder().
                setToEmail(registerUserInput.email).
                setName(registerUserInput.firstName + " " + registerUserInput.lastName).
                setUrl("http://" + host + "/mrxuser/confirmation/" + token).
                setSubject("MRP: Please confirm your registration").
                setFromEmail("admin@ec2-54-145-194-211.compute-1.amazonaws.com").
                setTemplateName(FreemarkerEmailModel.VERIFICATION_TEMPLATE).build();


        return freemarkerEmailModel;
    }


    protected void validate(@RequestParam(value = "email") String email, @RequestParam("password") String password) {

        if(StringUtils.isBlank(email) || StringUtils.isBlank(password)) {
            throw new BadRequestException("BAD_REQUEST", "Email and Password are required.");
        }
    }

}
