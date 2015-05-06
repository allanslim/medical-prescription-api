package com.codewarrior.csc686.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class BaseService {

    @Autowired
    protected JpaTransactionManager transactionManager;

    @Autowired
    protected DataSource datasource;

    protected CallableStatement createCallableStatement(String procedureName) throws SQLException {
//        return transactionManager.getDataSource().getConnection().prepareCall(procedureName);
        return datasource.getConnection().prepareCall(procedureName);

    }

    protected void closeResources(CallableStatement callableStatement, ResultSet resultSet, ResultSet resultSet2) throws SQLException {

//        if (resultSet != null) {
//            resultSet.close();
//        }
//
//        if (resultSet2 != null) {
//            resultSet2.close();
//        }
//
//        if (callableStatement != null) {
//            callableStatement.close();
//        }
    }


}
