package com.codewarrior.csc686.project.service;

import com.codewarrior.csc686.project.entity.MrxUser;
import com.codewarrior.csc686.project.model.Pharmacy;
import com.codewarrior.csc686.project.repository.BackdoorRepository;
import com.codewarrior.csc686.project.repository.MrxUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class MrxService {

    @Autowired
    private MrxUserRepository mrxUserRepository;

    @Autowired
    private BackdoorRepository backdoorRepository;

    @Autowired
    private PharmacyService pharmacyService;


    public Optional<MrxUser> retrieveMrxUserByEmail(String email) {

        List<MrxUser> users = mrxUserRepository.findByEmail(email);

        if(!users.isEmpty()) {
            return Optional.of(users.get(0));
        }

        return Optional.empty();
    }

    public Optional<String> retrieveUserToken(String token) {

        List<MrxUser> users = mrxUserRepository.findByToken(token);

        if(!users.isEmpty()) {
            return Optional.of(users.get(0).token);
        }

        return Optional.empty();
    }



    public void confirmUser(String token) {
        List<MrxUser> users = mrxUserRepository.findByToken(token);

        if(!users.isEmpty()) {
            MrxUser mrxUser = users.get(0);
            mrxUser.isValid = true;
            mrxUserRepository.save(mrxUser);
        }

    }

    public String deleteRegisteredAccount(String email) {

        backdoorRepository.deleteAttachedQuestionaires(email);
        backdoorRepository.deleteRegisteredAccount(email);

        return  "Success";
    }


    public void deleteToken(String token) {

        List<MrxUser> mrxUsers = mrxUserRepository.findByToken(token);

        mrxUsers.stream().forEach(u -> u.token = "");

        mrxUserRepository.save(mrxUsers);
    }


    public List<Pharmacy> retrievePharmacy(String token, Integer zipcode, Integer radius) throws SQLException {

        return pharmacyService.retrievePharmacy(token, zipcode, radius);
    }

    public List<String> retrieveDrugs(String token, String drug) throws SQLException {

        return pharmacyService.retrieveDrugs(token, drug);
    }
}
