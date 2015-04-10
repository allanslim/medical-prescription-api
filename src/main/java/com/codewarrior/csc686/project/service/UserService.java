package com.codewarrior.csc686.project.service;

import com.codewarrior.csc686.project.exception.BadRequestException;
import oracle.jdbc.OracleTypes;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class UserService extends BaseService {


    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private static String loginUserProcedure = "{ call RMP_USRMGT_PKG.LOGIN_USER(?,?,?) }";


    public String login(String email, String password) throws SQLException {


        CallableStatement callableStatement = getCallableStatement(email, password);

        callableStatement.executeUpdate();

        ResultSet resultSet = (ResultSet) callableStatement.getObject(3);

        while (resultSet.next()) {
            String messageName = resultSet.getString("MSG_NAME");
            String description = resultSet.getString("MSG_DESCR");
            LOG.info("messageName: " + messageName + " description: " + description);

            String token =  resultSet.getString("TOKEN");

            if(StringUtils.isBlank(token)) {
                throw new BadRequestException("400", description);
            }

            return token;

        }
        return StringUtils.EMPTY;
    }

    protected CallableStatement getCallableStatement(String email, String password) throws SQLException {

        CallableStatement callableStatement = createCallableStatement(loginUserProcedure);

        callableStatement.setString(1, email);
        callableStatement.setString(2, password);

        callableStatement.registerOutParameter(3, OracleTypes.CURSOR);
        return callableStatement;
    }
}
