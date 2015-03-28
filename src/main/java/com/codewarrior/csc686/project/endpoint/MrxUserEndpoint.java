package com.codewarrior.csc686.project.endpoint;

import com.codewarrior.csc686.project.entity.MrxUser;
import com.codewarrior.csc686.project.service.MrxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
