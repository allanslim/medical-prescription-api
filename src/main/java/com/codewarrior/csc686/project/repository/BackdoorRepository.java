package com.codewarrior.csc686.project.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BackdoorRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public void deleteRegisteredAccount( String email) {
        String sql = "delete from mrx_user where email='" + email + "'";
        jdbcTemplate.update(sql);

    }


    public void deleteAttachedQuestionaires( String email) {

        String sql = "delete from MRX_USER_ACSECQ where user_id in (select user_id from mrx_user where email='" + email + "') ";

        jdbcTemplate.update(sql);
    }
}
