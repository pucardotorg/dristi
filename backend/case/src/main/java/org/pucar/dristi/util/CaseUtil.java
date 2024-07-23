package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.idgen.IdGenerationRequest;
import org.egov.common.contract.idgen.IdGenerationResponse;
import org.egov.common.contract.idgen.IdRequest;
import org.egov.common.contract.idgen.IdResponse;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.pucar.dristi.config.ServiceConstants.IDGEN_ERROR;
import static org.pucar.dristi.config.ServiceConstants.NO_IDS_FOUND_ERROR;

@Component
public class CaseUtil {

    public String getCNRNumber(String fillingNumber) {
        String cnrNumber = "";

        String state = "KL";
        String district = "JL";
        String establishmentCode = "01";

        String resp[] = fillingNumber.split("-");
        String sequenceNumber = resp[resp.length-1];
        String year = resp[resp.length-2];
        cnrNumber = state+district+establishmentCode+"-"+sequenceNumber+"-"+year;

        return cnrNumber;
    }
}