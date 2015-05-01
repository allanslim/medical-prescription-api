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
public class EmailValidatorService extends BaseService {

    private static final Logger LOG = LoggerFactory.getLogger(MemberInformationService.class);


    public boolean doesEmailExist(String email) throws SQLException {

        CallableStatement callableStatement = null;
        ResultSet resultSet = null;

        try {
            callableStatement = createCallableStatement("{ call RMP_USRMGT_PKG.VALIDATE_USER_EMAIL(?,?)}");

            callableStatement.setString(1, email);
            callableStatement.registerOutParameter(2, OracleTypes.CURSOR);

            callableStatement.executeUpdate();

            resultSet = (ResultSet) callableStatement.getObject(2);

            while (resultSet.next()) {
                String messageName = resultSet.getString("MSG_NAME");

                return StringUtils.equals(messageName, "MRX_USER.DUPEMAIL");

            }


        } catch (SQLException e) {
            LOG.error("SQL EXCEPTION", e);
        } catch (BadRequestException e) {
            LOG.error("BAD REQUEST EXCEPTION", e);
        } finally {
            closeResources(callableStatement, resultSet, null);
        }

        return false;

    }


}
