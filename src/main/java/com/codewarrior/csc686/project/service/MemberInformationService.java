package com.codewarrior.csc686.project.service;

import com.codewarrior.csc686.project.exception.BadRequestException;
import com.codewarrior.csc686.project.util.Tuple;
import oracle.jdbc.OracleTypes;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class MemberInformationService extends BaseService {

    private static final Logger LOG = LoggerFactory.getLogger(MemberInformationService.class);


    private static String WELCOME_MEMBER_PROCEDURE_NAME = "{ call RMP_MBRBEN_PKG.Welcome_Member(?,?, ?)}";

    private static String WELCOME_ANNUAL_BENEFIT_SUMMARY_NAME = "{ call RMP_MBRBEN_PKG.Annual_Benefit_Summary(?,?,?)}";


    private Tuple<ResultSet,ResultSet> extractResultSetFor(String storedProcedure, String token) throws SQLException {
        CallableStatement callableStatement = createCallableStatement(storedProcedure);

          callableStatement.setString(1, token);

          callableStatement.registerOutParameter(2, OracleTypes.CURSOR);
          callableStatement.registerOutParameter(3, OracleTypes.CURSOR);

          callableStatement.executeUpdate();

          return new Tuple((ResultSet) callableStatement.getObject(2), (ResultSet) callableStatement.getObject(3));
     }


    public Map<String, String> retrieveMemberProfile(String token) throws SQLException {

        Map<String, String> memberInfoMap = new HashMap<>();

        Tuple<ResultSet, ResultSet> resultSetTuple = extractResultSetFor(WELCOME_MEMBER_PROCEDURE_NAME, token);

        ResultSet resultSet = resultSetTuple.x;
        ResultSet resultSet2 = resultSetTuple.y;

        while (resultSet.next()) {
            String messageName = resultSet.getString("MSG_NAME");
            String description = resultSet.getString("MSG_DESCR");

            LOG.info("messageName: " + messageName + " description: " + description);

            if (!StringUtils.equals("SUCCESS", messageName)) {
                throw new BadRequestException("400", description);
            }

            while (resultSet2.next()) {
                memberInfoMap.put("insuranceId", resultSet2.getString("INSURANCE_ID"));
                memberInfoMap.put("lastName", resultSet2.getString("LAST_NAME"));
                memberInfoMap.put("firstName", resultSet2.getString("FIRST_NAME"));
                memberInfoMap.put("middleInitial", resultSet2.getString("MI"));
                memberInfoMap.put("groupId", resultSet2.getString("RXGRPID"));
                memberInfoMap.put("planName", resultSet2.getString("PLAN_NAME"));

            }
        }
        return memberInfoMap;
    }

    // PF9H76WF3J8J26LJ4VZN2SB36LIQNOYJ3BFZID0WHU1C0Z0K92
    public Map<String, String> retrieveMemberAnnualBenefitSumary(String token) throws SQLException {

        Map<String, String> memberInfoMap = new HashMap<>();

        Tuple<ResultSet, ResultSet> resultSetTuple = extractResultSetFor(WELCOME_ANNUAL_BENEFIT_SUMMARY_NAME, token);

        ResultSet resultSet = resultSetTuple.x;
        ResultSet resultSet2 = resultSetTuple.y;

        while (resultSet.next()) {
            String messageName = resultSet.getString("MSG_NAME");
            String description = resultSet.getString("MSG_DESCR");

            LOG.info("messageName: " + messageName + " description: " + description);

            if (!StringUtils.equals("SUCCESS", messageName)) {
                throw new BadRequestException("400", description);
            }


            while (resultSet2.next()) {
                memberInfoMap.put("insuranceId", resultSet2.getString("INSURANCE_ID"));
                memberInfoMap.put("lastName", resultSet2.getString("LAST_NAME"));
                memberInfoMap.put("firstName", resultSet2.getString("FIRST_NAME"));
                memberInfoMap.put("middleInitial", resultSet2.getString("MI"));
                memberInfoMap.put("groupId", resultSet2.getString("RXGRPID"));
                memberInfoMap.put("planName", resultSet2.getString("PLAN_NAME"));
                memberInfoMap.put("deductibleAmountIndividual", resultSet2.getString("DEDUCTIBLE_AMT_IND"));
                memberInfoMap.put("deductibleAmountFamily", resultSet2.getString("DEDUCTIBLE_AMT_FAM"));
                memberInfoMap.put("maximumOutOfPocketIndividual", resultSet2.getString("MAX_OOP_IND"));
                memberInfoMap.put("maximumOutOfPocketFamily", resultSet2.getString("MAX_OOP_FAM"));
                memberInfoMap.put("benefitAmountIndividual", resultSet2.getString("BENEFIT_AMOUNT_IND"));
                memberInfoMap.put("benefitAmountFamily", resultSet2.getString("BENEFIT_AMOUNT_FAM"));
                memberInfoMap.put("coInsuranceIndividual", resultSet2.getString("COINSURANCE_IND"));
                memberInfoMap.put("coInsuranceFamily", resultSet2.getString("COINSURANCE_FAM"));

            }
        }
        return memberInfoMap;

    }
}