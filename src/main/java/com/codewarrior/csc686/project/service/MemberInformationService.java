package com.codewarrior.csc686.project.service;

import com.codewarrior.csc686.project.exception.BadRequestException;
import com.codewarrior.csc686.project.model.Dependent;
import com.codewarrior.csc686.project.model.PrescriptionHistory;
import oracle.jdbc.OracleTypes;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MemberInformationService extends BaseService {

    private static final Logger LOG = LoggerFactory.getLogger(MemberInformationService.class);


    private static String WELCOME_MEMBER_PROCEDURE_NAME = "{ call RMP_MBRBEN_PKG.Welcome_Member(?,?, ?)}";

    private static String WELCOME_ANNUAL_BENEFIT_SUMMARY_NAME = "{ call RMP_MBRBEN_PKG.Annual_Benefit_Summary(?,?,?)}";

    private static String MEMBER_DEPENDENTS_NAME = "{ call RMP_MBRBEN_PKG.Member_Dependents(?,?,?)}";

    private static String PRESCRIPTION_HISTORY_NAME = "{ call RMP_MBRBEN_PKG.Prescription_History(?,?,?,?,?)}";


    private CallableStatement extractResultSetFor(String storedProcedure, String token) throws SQLException {

        CallableStatement callableStatement = createCallableStatement(storedProcedure);

        callableStatement.setString(1, token);

        callableStatement.registerOutParameter(2, OracleTypes.CURSOR);
        callableStatement.registerOutParameter(3, OracleTypes.CURSOR);

        callableStatement.executeUpdate();

        return callableStatement;

    }


    private CallableStatement extractResultSetPrescriptionHistory(String prescriptionHistoryName, String token, int mrbId, int period) throws SQLException {

        CallableStatement callableStatement = createCallableStatement(prescriptionHistoryName);


        callableStatement.setInt(1, mrbId);
        callableStatement.setInt(2, period);
        callableStatement.setString(3, token);

        callableStatement.registerOutParameter(4, OracleTypes.CURSOR);
        callableStatement.registerOutParameter(5, OracleTypes.CURSOR);

        callableStatement.executeUpdate();

        return callableStatement;
    }


    public Map<String, String> retrieveMemberProfile(String token) throws SQLException {

        Map<String, String> memberInfoMap = new HashMap<>();

        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        ResultSet resultSet2 = null;


        try {

            callableStatement = extractResultSetFor(WELCOME_MEMBER_PROCEDURE_NAME, token);


            resultSet = (ResultSet) callableStatement.getObject(2);
            resultSet2 = (ResultSet) callableStatement.getObject(3);

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
        } catch (SQLException e) {
            LOG.error("SQL EXCEPTION", e);
        } catch (BadRequestException e) {
            LOG.error("BAD REQUEST EXCEPTION", e);
        } finally {

            closeResources(callableStatement, resultSet, resultSet2);
        }
        return memberInfoMap;
    }


    // PF9H76WF3J8J26LJ4VZN2SB36LIQNOYJ3BFZID0WHU1C0Z0K92
    public Map<String, String> retrieveMemberAnnualBenefitSumary(String token) throws SQLException {

        Map<String, String> memberInfoMap = new HashMap<>();

        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        ResultSet resultSet2 = null;

        try {
            callableStatement = extractResultSetFor(WELCOME_ANNUAL_BENEFIT_SUMMARY_NAME, token);

            resultSet = (ResultSet) callableStatement.getObject(2);
            resultSet2 = (ResultSet) callableStatement.getObject(3);

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
        } catch (SQLException e) {
            LOG.error("DATABASE ERROR!!! ", e);
        } finally {
            closeResources(callableStatement, resultSet, resultSet2);

        }

        return memberInfoMap;

    }

    public List<Dependent> retrieveMemberDependents(String token) throws SQLException {

        List<Dependent> dependents = new ArrayList<>();

        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        ResultSet resultSet2 = null;


        try {
            callableStatement = extractResultSetFor(MEMBER_DEPENDENTS_NAME, token);

            resultSet = (ResultSet) callableStatement.getObject(2);
            resultSet2 = (ResultSet) callableStatement.getObject(3);

            while (resultSet.next()) {
                String messageName = resultSet.getString("MSG_NAME");
                String description = resultSet.getString("MSG_DESCR");

                LOG.info("messageName: " + messageName + " description: " + description);

                if (!StringUtils.equals("SUCCESS", messageName)) {
                    throw new BadRequestException("400", description);
                }


                while (resultSet2.next()) {
                    Dependent dependent = new Dependent();
                    dependent.mbrId = resultSet2.getString("MBR_ID");
                    dependent.mbrName = resultSet2.getString("MBR_NAME");
                    dependent.relationship = resultSet2.getString("RELATIONSHIP");

                    dependents.add(dependent);


                }
            }
        } catch (SQLException e) {
            LOG.error("SQL EXCEPTION", e);
        } catch (BadRequestException e) {
            LOG.error("BAD REQUEST EXCEPTION", e);
        } finally {
            closeResources(callableStatement, resultSet, resultSet2);
        }
        return dependents;
    }

    public List<PrescriptionHistory> retrievePrescriptionHistory(String token, int mrbId, int period) throws SQLException {

        List<PrescriptionHistory> prescriptionHistories = new ArrayList<>();

        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        ResultSet resultSet2 = null;


        try {
            callableStatement = extractResultSetPrescriptionHistory(PRESCRIPTION_HISTORY_NAME, token, mrbId, period);

            resultSet = (ResultSet) callableStatement.getObject(4);
            resultSet2 = (ResultSet) callableStatement.getObject(5);


            while (resultSet.next()) {
                String messageName = resultSet.getString("MSG_NAME");
                String description = resultSet.getString("MSG_DESCR");

                LOG.info("messageName: " + messageName + " description: " + description);

                if (!StringUtils.equals("SUCCESS", messageName)) {
                    throw new BadRequestException("400", description);
                }


                while (resultSet2.next()) {
                    PrescriptionHistory prescriptionHistory = new PrescriptionHistory();
                    prescriptionHistory.rxNumber = resultSet2.getString("RX_NUMBER");
                    prescriptionHistory.dateFilled = resultSet2.getDate("DATE_FILLED");
                    prescriptionHistory.drug = resultSet2.getString("DRUG");
                    prescriptionHistory.mbrPaid = resultSet2.getString("MBR_PAID");
                    prescriptionHistory.pharmacy = resultSet2.getString("PHARMACY");

                    prescriptionHistories.add(prescriptionHistory);


                }
            }
        } catch (SQLException e) {
            LOG.error("SQL EXCEPTION", e);
        } catch (BadRequestException e) {
            LOG.error("BAD REQUEST EXCEPTION", e);
        } finally {
            closeResources(callableStatement, resultSet, resultSet2);
        }

        return prescriptionHistories;
    }

}