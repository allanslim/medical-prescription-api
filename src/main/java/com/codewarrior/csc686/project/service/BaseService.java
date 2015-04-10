package com.codewarrior.csc686.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;

import java.sql.CallableStatement;
import java.sql.SQLException;


public class BaseService {

    @Autowired
    protected JpaTransactionManager transactionManager;

    protected CallableStatement createCallableStatement(String procedureName) throws SQLException {
        return transactionManager.getDataSource().getConnection().prepareCall(procedureName);
    }

}
