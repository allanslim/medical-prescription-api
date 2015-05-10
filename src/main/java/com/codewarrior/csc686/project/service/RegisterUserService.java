package com.codewarrior.csc686.project.service;


import com.codewarrior.csc686.project.exception.BadRequestException;
import com.codewarrior.csc686.project.model.RegisterUserInput;
import oracle.jdbc.OracleTypes;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;


//PROCEDURE Register_User
//            (
//             P_RX_GRPID      IN MRX_GROUP.GRP_ID%TYPE,
//             P_MBR_INSRID    IN MRX_MEMBER.INSURANCE_ID%TYPE,
//             P_MBR_FNAME     IN MRX_MEMBER.FIRST_NAME%TYPE,
//             P_MBR_LNAME     IN MRX_MEMBER.LAST_NAME%TYPE,
//             P_MBR_DOB       IN MRX_MEMBER.BIRTHDATE%TYPE,
//             P_EMAIL         IN MRX_USER.EMAIL%TYPE,
//             P_PASSWORD      IN MRX_USER.ENCRYPTED_PASSWORD%TYPE,
//             P_SECQ_1        IN MRX_USER_ACSECQ.AC_SECQ%type,
//             P_SECA_1        IN MRX_USER_ACSECQ.AC_SECQ%type,
//             P_SECQ_2        IN MRX_USER_ACSECQ.AC_SECQ%type,
//             P_SECA_2        IN MRX_USER_ACSECQ.AC_SECQ%type,
//             P_SECQ_3        IN MRX_USER_ACSECQ.AC_SECQ%type,
//             P_SECA_3        IN MRX_USER_ACSECQ.AC_SECQ%type,
//             P_REFCURMSG    OUT SYS_REFCURSOR,
//             P_REFCURDAT    OUT SYS_REFCURSOR
//             )

@Service
public class RegisterUserService extends BaseService {

    private static final Logger LOG = LoggerFactory.getLogger(RegisterUserService.class);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private static String REGISTER_USER_STORED_PROCEDURE_NAME = "{ call RMP_USRMGT_PKG.REGISTER_USER(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

    private static String VALIDATE_MEMBER_STORED_PROCEDURE_NAME = "{ call RMP_USRMGT_PKG.Validate_Member(?,?,?,?,?,?)}";

    private static String VALIDATE_USEREMAIL_PROCEDURE_NAME = "{ call RMP_USRMGT_PKG.Validate_User_Email(?,?)}";


    public String registerUser(RegisterUserInput registerUserInput) throws SQLException, ParseException {

        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        try {
            connection = super.getConnection();
            callableStatement = createCallableStatement(connection, REGISTER_USER_STORED_PROCEDURE_NAME);

            callableStatement.setInt(1, registerUserInput.groupId);
            callableStatement.setString(2, registerUserInput.insuranceId);
            callableStatement.setString(3, registerUserInput.firstName);
            callableStatement.setString(4, registerUserInput.lastName);

            long time = sdf.parse(registerUserInput.birthday).getTime();
            callableStatement.setDate(5, new Date(time));

            callableStatement.setString(6, registerUserInput.email);
            callableStatement.setString(7, registerUserInput.password);

            callableStatement.setString(8, registerUserInput.seqQ1);
            callableStatement.setString(9, registerUserInput.seqA1);

            callableStatement.setString(10, registerUserInput.seqQ2);
            callableStatement.setString(11, registerUserInput.seqA2);

            callableStatement.setString(12, registerUserInput.seqQ3);
            callableStatement.setString(13, registerUserInput.seqA3);

            callableStatement.registerOutParameter(14, OracleTypes.CURSOR);

            callableStatement.executeUpdate();

            resultSet = (ResultSet) callableStatement.getObject(14);

            while (resultSet.next()) {
                String messageName = resultSet.getString("MSG_NAME");
                String description = resultSet.getString("MSG_DESCR");
                String token = resultSet.getString("TOKEN");

                LOG.info("messageName: " + messageName + " description: " + description);

                if (!StringUtils.equals("SUCCESS", messageName)) {
                    closeResources(callableStatement, resultSet, null, connection);
                    throw new BadRequestException("400", description);
                }

                closeResources(callableStatement, resultSet, null, connection);
                return token;
            }
        } catch (SQLException e) {
            LOG.error("SQL EXCEPTION", e);
        } catch (BadRequestException e) {
            LOG.error("BAD REQUEST EXCEPTION", e);
        } finally {
            closeResources(callableStatement, resultSet, null, connection);
        }

        return StringUtils.EMPTY;
    }


    //    PROCEDURE Validate_Member
//                (
//                 P_RX_GRPID      IN MRX_GROUP.GRP_ID%TYPE,
//                 P_MBR_INSRID    IN MRX_MEMBER.INSURANCE_ID%TYPE,
//                 P_MBR_FNAME     IN MRX_MEMBER.FIRST_NAME%TYPE,
//                 P_MBR_LNAME     IN MRX_MEMBER.LAST_NAME%TYPE,
//                 P_MBR_DOB       IN MRX_MEMBER.BIRTHDATE%TYPE,
//                 P_REFCURMSG    OUT SYS_REFCURSOR
//                 )
    //  MSG_NAME (MRX_USR.DUP), MSG_DESCR (MEMBER ALREADY HAS RMP ACCOUNT)
    public boolean isMemberValid(RegisterUserInput registerUserInput) throws SQLException, ParseException {

        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        try {
            connection = super.getConnection();
            callableStatement = createCallableStatement(connection, VALIDATE_MEMBER_STORED_PROCEDURE_NAME);

            callableStatement.setInt(1, registerUserInput.groupId);
            callableStatement.setString(2, registerUserInput.insuranceId);
            callableStatement.setString(3, registerUserInput.firstName);
            callableStatement.setString(4, registerUserInput.lastName);

            long time = sdf.parse(registerUserInput.birthday).getTime();
            callableStatement.setDate(5, new Date(time));

            callableStatement.registerOutParameter(6, OracleTypes.CURSOR);

            callableStatement.executeUpdate();

            resultSet = (ResultSet) callableStatement.getObject(6);

            while (resultSet.next()) {
                String messageName = resultSet.getString("MSG_NAME");
                String description = resultSet.getString("MSG_DESCR");

                LOG.info("messageName: " + messageName + " description: " + description);

                if (!StringUtils.equals("SUCCESS", messageName)) {
                    throw new BadRequestException("400", description);
                }

                return true;
            }
        } catch (SQLException e) {
            LOG.error("SQL EXCEPTION", e);
        } catch (BadRequestException e) {
            LOG.error("BAD REQUEST EXCEPTION", e);
        } finally {
            closeResources(callableStatement, resultSet, null, connection);
        }
        return false;
    }

    public boolean isEmailAvailable(String email) throws SQLException {

        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        try {
            connection = super.getConnection();
             callableStatement = createCallableStatement(connection, VALIDATE_USEREMAIL_PROCEDURE_NAME);

            callableStatement.setString(1, email);
            callableStatement.registerOutParameter(2, OracleTypes.CURSOR);


            callableStatement.executeUpdate();

             resultSet = (ResultSet) callableStatement.getObject(2);

            while (resultSet.next()) {
                String messageName = resultSet.getString("MSG_NAME");
                String description = resultSet.getString("MSG_DESCR");

                LOG.info("messageName: " + messageName + " description: " + description);

                if (!StringUtils.equals("SUCCESS", messageName)) {
                    throw new BadRequestException("400", description);
                }

                return true;
            }
        } catch (SQLException e) {
            LOG.error("SQL EXCEPTION", e);
        } catch (BadRequestException e) {
            LOG.error("BAD REQUEST EXCEPTION", e);
        } finally {
            closeResources(callableStatement, resultSet, null, connection);
        }

        return false;
    }
}
