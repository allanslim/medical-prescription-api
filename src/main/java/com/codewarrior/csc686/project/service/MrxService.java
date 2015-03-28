package com.codewarrior.csc686.project.service;

import com.codewarrior.csc686.project.entity.MrxUser;
import com.codewarrior.csc686.project.repository.MrxUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MrxService {

    @Autowired
    private MrxUserRepository mrxUserRepository;


    public Optional<MrxUser> retrieveMrxUserByEmail(String email) {

        List<MrxUser> users = mrxUserRepository.findByEmail(email);

        if(!users.isEmpty()) {
            return Optional.of(users.get(0));
        }

        return Optional.empty();
    }

}
