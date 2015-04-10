package com.codewarrior.csc686.project.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties( ignoreUnknown = true )
public class RegisterUserInput {

    public Integer groupId;
    public String insuranceId;
    public String firstName;
    public String lastName;


    public String birthday;

    public String email;
    public String password;

    public String seqQ1;
    public String seqA1;

    public String seqQ2;
    public String seqA2;

    public String seqQ3;
    public String seqA3;

}
