package com.codewarrior.csc686.project.endpoint;

import com.codewarrior.csc686.project.configuration.CommonConfiguration;
import com.codewarrior.csc686.project.model.RegisterUserInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { CommonConfiguration.class})
@Ignore
public class UserEndpointTest {

    @Autowired
   	private ObjectMapper objectMapper;


    @Before
    public void setUp() throws Exception {

    }

    //{"groupId":1,"insuranceId":"2222223","firstName":"CHONG","lastName":"LEE","birthday":"1966-04-22","email":"chong@lee.com","password":"abc123","seqQ1":"answer1","seqA1":"this is a test","seqQ2":"answer2","seqA2":"this is a test2","seqQ3":"answer3","seqA3":"this is a test3"}

    @Test
    public void testRegister() throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        RegisterUserInput registerUserInput = new RegisterUserInput();

        registerUserInput.groupId = 1;
        registerUserInput.insuranceId = "2222223";
        registerUserInput.firstName = "CHONG";
        registerUserInput.lastName = "LEE";



        registerUserInput.birthday = "1966-04-22";

        registerUserInput.email = "chong@lee.com";
        registerUserInput.password = "abc123";
        registerUserInput.seqA1 = "this is a test";
        registerUserInput.seqQ1 = "answer1";
        registerUserInput.seqA2 = "this is a test2";
        registerUserInput.seqQ2 = "answer2";
        registerUserInput.seqA3 = "this is a test3";
        registerUserInput.seqQ3 = "answer3";

        String json = objectMapper.writer().writeValueAsString(registerUserInput);

        System.out.println("################ JSON: " + json);
    }
}