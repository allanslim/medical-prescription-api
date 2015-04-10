package com.codewarrior.csc686.project.service;

import oracle.jdbc.OracleTypes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class EmailValidatorService extends BaseService {


    @PostConstruct
    public void initialize() {
    }

    public boolean doesEmailExist(String email) {


        try {
            CallableStatement callableStatement = createCallableStatement("{ call RMP_USRMGT_PKG.VALIDATE_USER_EMAIL(?,?)}");

            callableStatement.setString(1, email);
            callableStatement.registerOutParameter(2, OracleTypes.CURSOR);

            callableStatement.executeUpdate();

            ResultSet resultSet = (ResultSet) callableStatement.getObject(2);

            while(resultSet.next()) {
                String messageName = resultSet.getString("MSG_NAME");

                return StringUtils.equals(messageName, "MRX_USER.DUPEMAIL");

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }



}
