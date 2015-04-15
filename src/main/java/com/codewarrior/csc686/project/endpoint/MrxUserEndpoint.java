package com.codewarrior.csc686.project.endpoint;

import com.codewarrior.csc686.project.entity.MrxUser;
import com.codewarrior.csc686.project.service.MrxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping( value = "/mrxuser" )
public class MrxUserEndpoint {


    @Autowired
    private MrxService mrxService;

    @RequestMapping( method = RequestMethod.GET, value = "/email/{email}")
    public MrxUser retrieveByEmail(@PathVariable String email) {

        Optional<MrxUser> optionalMrxUser = mrxService.retrieveMrxUserByEmail(email);

        return optionalMrxUser.orElseGet( () -> new MrxUser());
    }


    @RequestMapping( method = RequestMethod.GET, value = "/token/{token}")
    public Map<String,String> retrieveToken(@PathVariable String token) {

        Optional<String> optionalToken = mrxService.retrieveUserToken(token);

        Map<String,String> response = new HashMap<>();
        response.put("token", optionalToken.orElseGet( () -> ""));

        return response;
    }

    @RequestMapping( method = RequestMethod.GET, value = "/confirmation/{token}")
    public String confirmation(@PathVariable String token) {

        mrxService.confirmUser(token);

        return "Your Email Address has been successfully Confirmed! You may login to the system.";
    }
}
