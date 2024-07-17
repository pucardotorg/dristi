package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pucar.dristi.config.Configuration;
import static org.pucar.dristi.config.ServiceConstants.*;

import org.egov.common.contract.user.UserDetailResponse;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class UserUtil {
    private final ObjectMapper mapper;
    private final ServiceRequestRepository serviceRequestRepository;
    private final Configuration configs;

    @Autowired
    public UserUtil(ObjectMapper mapper, ServiceRequestRepository serviceRequestRepository, Configuration configs) {
        this.mapper = mapper;
        this.serviceRequestRepository = serviceRequestRepository;
        this.configs = configs;
    }

    /**
     * Returns UserDetailResponse by calling user service with given uri and object
     *
     * @param userRequest Request object for user service
     * @param uri         The address of the endpoint
     * @return Response from user service as parsed as userDetailResponse
     */

    public UserDetailResponse userCall(Object userRequest, StringBuilder uri) {
        String dobFormat = null;
        if (uri.toString().contains(configs.getUserSearchEndpoint())
                || uri.toString().contains(configs.getUserUpdateEndpoint()))
            dobFormat = DOB_FORMAT_Y_M_D;
        else if (uri.toString().contains(configs.getUserCreateEndpoint()))
            dobFormat = DOB_FORMAT_D_M_Y;
        try {
            LinkedHashMap<String, Object> responseMap =(LinkedHashMap)  serviceRequestRepository.fetchResult(uri, userRequest);
            parseResponse(responseMap, dobFormat);
            UserDetailResponse userDetailResponse = mapper.convertValue(responseMap, UserDetailResponse.class);
            return userDetailResponse;
        } catch (IllegalArgumentException e) {
            throw new CustomException(ILLEGAL_ARGUMENT_EXCEPTION_CODE, OBJECTMAPPER_UNABLE_TO_CONVERT);
        }
    }

    /**
     * Parses date formats to long for all users in responseMap
     *
     * @param responseMap LinkedHashMap got from user api response
     */

    public void parseResponse(LinkedHashMap<String, Object> responseMap, String dobFormat) {
        List<LinkedHashMap<String, Object>> users = (List<LinkedHashMap<String, Object>>) responseMap.get(USER);
        String format1 = DOB_FORMAT_D_M_Y_H_M_S;
        if (users != null) {
            users.forEach(map -> {
                map.put(CREATED_DATE, dateTolong((String) map.get(CREATED_DATE), format1));
                if (map.get(LAST_MODIFIED_DATE) != null)
                    map.put(LAST_MODIFIED_DATE, dateTolong((String) map.get(LAST_MODIFIED_DATE), format1));
                if (map.get(DOB) != null)
                    map.put(DOB, dateTolong((String) map.get(DOB), dobFormat));
                if (map.get(PWD_EXPIRY_DATE) != null)
                    map.put(PWD_EXPIRY_DATE, dateTolong((String) map.get(PWD_EXPIRY_DATE), format1));
            });
        }
    }

    /**
     * Converts date to long
     *
     * @param date   date to be parsed
     * @param format Format of the date
     * @return Long value of date
     */
    private Long dateTolong(String date, String format) {
        SimpleDateFormat f = new SimpleDateFormat(format);
        Date d = null;
        try {
            d = f.parse(date);
        } catch (ParseException e) {
            throw new CustomException(INVALID_DATE_FORMAT_CODE, INVALID_DATE_FORMAT_MESSAGE);
        }
        return d.getTime();
    }

}