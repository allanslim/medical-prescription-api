package com.codewarrior.csc686.project.service;

import com.codewarrior.csc686.project.exception.BadRequestException;
import com.codewarrior.csc686.project.model.Pharmacy;
import oracle.jdbc.OracleTypes;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PharmacyService extends BaseService {

    private static final Logger LOG = LoggerFactory.getLogger(PharmacyService.class);


    private static String LOCATE_PHARMACY_PROCEDURE_NAME = "{ call RMP_MBRBEN_PKG.LOCATE_PHARMACY(?,?,?,?,?)}";


    public List<Pharmacy> retrievePharmacy(String token, Integer zipcode, Integer radius)  throws SQLException  {


        List<Pharmacy> pharmacies = new ArrayList<>();

        CallableStatement callableStatement = getCallableStatement(token, zipcode, radius);

        callableStatement.executeUpdate();

        ResultSet resultSet = (ResultSet) callableStatement.getObject(4);
        ResultSet resultSet2 = (ResultSet) callableStatement.getObject(5);

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

        return pharmacies;
    }

    protected CallableStatement getCallableStatement(String token, Integer zipcode, Integer radius) throws SQLException {

        CallableStatement callableStatement = createCallableStatement(LOCATE_PHARMACY_PROCEDURE_NAME);

        callableStatement.setString(1, token);
        callableStatement.setInt(2, zipcode);
        callableStatement.setInt(3, radius);

        callableStatement.registerOutParameter(4, OracleTypes.CURSOR);
        callableStatement.registerOutParameter(5, OracleTypes.CURSOR);
        return callableStatement;
    }
}