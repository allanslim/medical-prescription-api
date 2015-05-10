package com.codewarrior.csc686.project.service;

import com.codewarrior.csc686.project.exception.BadRequestException;
import com.codewarrior.csc686.project.model.DrugDetail;
import com.codewarrior.csc686.project.model.DrugPrice;
import com.codewarrior.csc686.project.model.Pharmacy;
import oracle.jdbc.OracleTypes;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PharmacyService extends BaseService {

    private static final Logger LOG = LoggerFactory.getLogger(PharmacyService.class);


    private static String LOCATE_PHARMACY_PROCEDURE_NAME = "{ call RMP_MBRBEN_PKG.LOCATE_PHARMACY(?,?,?,?,?)}";

    private static String RETRIEVE_DRUG_PROCEDURE_NAME = "{ call RMP_MBRBEN_PKG.DRUGNAME_LIST(?,?,?,?,?)}";

    private static String RETRIEVE_DRUG_DETAILS_PROCEDURE_NAME = "{ call RMP_MBRBEN_PKG.Drug_DosageFormStren(?,?,?,?,?)}";

    private static String DRUG_PRICING_PROCEDURE_NAME = "{ call RMP_MBRBEN_PKG.Drug_Pricing(?,?,?,?,?) }";


    public DrugPrice retrieveDrugPrice(String token, String drugNdc, Integer pharmacyId) throws SQLException {

        DrugPrice drugPrice = new DrugPrice();

        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        ResultSet resultSet2 = null;

        Connection connection = null;

        try {
            connection = super.getConnection();

            callableStatement = createCallableStatement(connection, DRUG_PRICING_PROCEDURE_NAME);

            callableStatement.setString(1, token);
            callableStatement.setString(2, drugNdc);
            callableStatement.setInt(3, pharmacyId);

            callableStatement.registerOutParameter(4, OracleTypes.CURSOR);
            callableStatement.registerOutParameter(5, OracleTypes.CURSOR);

            callableStatement.executeUpdate();

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
                    drugPrice.brandGen = resultSet2.getString("BRAND_GEN");
                    drugPrice.drugName = resultSet2.getString("DRUG_NAME");
                    drugPrice.formularyStatus = resultSet2.getString("FORMULARY_STATUS");
                    drugPrice.retail = resultSet2.getString("RETAIL");
                    drugPrice.eds90 = resultSet2.getString("EDS90");
                    drugPrice.mailOrder = resultSet2.getString("MAIL_ORDER");
                    return drugPrice;
                }
            }

        } catch (Exception e) {
            LOG.error("EXCEPTION", e);
        } finally {
            closeResources(callableStatement, resultSet, resultSet2, connection);
        }
        return drugPrice;
    }


    public List<DrugDetail> retrieveDrugDetails(String token, String drugDescription) throws SQLException {

        List<DrugDetail> drugDetails = new ArrayList<>();

        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        ResultSet resultSet2 = null;
        Connection connection = null;

        try {
            connection = super.getConnection();
            callableStatement = createCallableStatement(connection, RETRIEVE_DRUG_DETAILS_PROCEDURE_NAME);

            callableStatement.setString(1, token);
            callableStatement.setString(2, drugDescription);
            callableStatement.setInt(3, 20);

            callableStatement.registerOutParameter(4, OracleTypes.CURSOR);
            callableStatement.registerOutParameter(5, OracleTypes.CURSOR);

            callableStatement.executeUpdate();

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
                    DrugDetail drugDetail = new DrugDetail();
                    drugDetail.vNdc = resultSet2.getString("NDC");
                    drugDetail.drugDetail = resultSet2.getString("DRUG_DETAIL");
                    drugDetails.add(drugDetail);
                }
            }

        } catch (Exception e) {
            LOG.error("EXCEPTION", e);
        } finally {
            closeResources(callableStatement, resultSet, resultSet2, connection);
        }

        return drugDetails;

    }

    public List<String> retrieveDrugs(String token, String drug) throws SQLException {

        List<String> drugs = new ArrayList<>();

        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        ResultSet resultSet2 = null;
        Connection connection = null;

        try {
            connection = super.getConnection();
            callableStatement = createCallableStatement(connection, RETRIEVE_DRUG_PROCEDURE_NAME);

            callableStatement.setString(1, token);
            callableStatement.setString(2, drug);
            callableStatement.setInt(3, 20);

            callableStatement.registerOutParameter(4, OracleTypes.CURSOR);
            callableStatement.registerOutParameter(5, OracleTypes.CURSOR);

            callableStatement.executeUpdate();

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
                    drugs.add(resultSet2.getString("DRUG_NAME"));
                }
            }

        } catch (Exception e) {
            LOG.error("EXCEPTION", e);
        } finally {
            closeResources(callableStatement, resultSet, resultSet2, connection);
        }

        return drugs;
    }


    public List<Pharmacy> retrievePharmacy(String token, Integer zipcode, Integer radius) throws SQLException {


        List<Pharmacy> pharmacies = new ArrayList<>();

        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        ResultSet resultSet2 = null;
        Connection connection = null;
        try {
            connection = super.getConnection();
            callableStatement = getCallableStatement(connection, token, zipcode, radius);

            callableStatement.executeUpdate();

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

                    Pharmacy pharmacy = new Pharmacy();

                    pharmacy.address1 = resultSet2.getString("ADDR1");
                    pharmacy.address2 = resultSet2.getString("ADDR2");
                    pharmacy.name = resultSet2.getString("NAME");
                    pharmacy.pharmacyId = resultSet2.getInt("PHR_ID");
                    pharmacy.city = resultSet2.getString("STD_CITY");
                    pharmacy.state = resultSet2.getString("STD_STATE");
                    pharmacy.zipcode = resultSet2.getInt("ZIPCODE");
                    pharmacy.description = resultSet2.getString("OPEN24H");
                    pharmacy.phone = resultSet2.getString("PHONE_EXT");
                    pharmacy.phoneExtension = resultSet2.getString("PHONE_EXT");
                    pharmacy.county = resultSet2.getString("COUNTY");
                    pharmacy.fax = resultSet2.getString("FAX");
                    pharmacy.latitude = resultSet2.getString("LATITUDE");
                    pharmacy.longitude = resultSet2.getString("LONGITUDE");
                    pharmacy.milesAway = resultSet2.getString("MILES_AWAY");

                    pharmacies.add(pharmacy);

                }

            }
        } catch (SQLException e) {
            LOG.error("SQL EXCEPTION", e);
        } catch (BadRequestException e) {
            LOG.error("BAD REQUEST EXCEPTION", e);
        } finally {
            closeResources(callableStatement, resultSet, resultSet2, connection);
        }

        return pharmacies;
    }

    protected CallableStatement getCallableStatement(Connection connection, String token, Integer zipcode, Integer radius) throws SQLException {

        CallableStatement callableStatement = createCallableStatement(connection, LOCATE_PHARMACY_PROCEDURE_NAME);

        callableStatement.setString(1, token);
        callableStatement.setInt(2, zipcode);
        callableStatement.setInt(3, radius);

        callableStatement.registerOutParameter(4, OracleTypes.CURSOR);
        callableStatement.registerOutParameter(5, OracleTypes.CURSOR);
        return callableStatement;
    }

}

