package com.codewarrior.csc686.project.endpoint;

import com.codewarrior.csc686.project.entity.MrxUser;
import com.codewarrior.csc686.project.service.MemberInformationService;
import com.codewarrior.csc686.project.service.MrxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping( value = "/mrxuser" )
public class MrxUserEndpoint {


    @Autowired
    private MrxService mrxService;

    @Autowired
    private MemberInformationService memberInformationService;



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


    @RequestMapping( method = RequestMethod.GET, value = "/memberInfo/{token}")
    public Map<String,String> retrieveMemberInfo(@PathVariable String token) throws SQLException {

        Map<String,String> memberInfoResponse = memberInformationService.retrieveMemberProfile(token);

        return memberInfoResponse;
    }

    @RequestMapping( method = RequestMethod.GET, value = "/annualBenefits/{token}")
    public Map<String,String> retrieveAnnualBenefit(@PathVariable String token) throws SQLException {

        Map<String,String> memberInfoResponse = memberInformationService.retrieveMemberAnnualBenefitSumary(token);

        return memberInfoResponse;
    }

    @RequestMapping( method = RequestMethod.GET, value = "/logout/{token}")
    public Map<String,String> logout(@PathVariable String token) {

        mrxService.deleteToken(token);

        Map<String,String> response = new HashMap<>();
        response.put("isLogOutSuccessful", "true");

        return response;
    }

    @RequestMapping( method = RequestMethod.GET, value = "/confirmation/{token}")
    public String confirmation(@PathVariable String token) {

        mrxService.confirmUser(token);

        return "Your Email Address has been successfully Confirmed! You may login to the system.";
    }

    @RequestMapping( method = RequestMethod.GET, value = "/backdoor/{email}")
    public Map<String,String> deleteByEmail(@PathVariable String email) {

        String response = mrxService.deleteRegisteredAccount(email);

        Map<String,String> map = new HashMap<>();
        map.put("response", response);

        return map;
    }
}
