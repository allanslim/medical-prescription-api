package com.codewarrior.csc686.project.repository;

import com.codewarrior.csc686.project.entity.MrxUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MrxUserRepository extends CrudRepository<MrxUser, Long> {

    List<MrxUser> findByEmail(String email);

    List<MrxUser> findByToken(String token);
}
