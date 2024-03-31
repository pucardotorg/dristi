package org.pucar.util;

import static org.pucar.config.ServiceConstants.CITIZEN_LOWER;
import static org.pucar.config.ServiceConstants.CITIZEN_UPPER;
import static org.pucar.config.ServiceConstants.CREATED_DATE;
import static org.pucar.config.ServiceConstants.DOB;
import static org.pucar.config.ServiceConstants.DOB_FORMAT_D_M_Y;
import static org.pucar.config.ServiceConstants.DOB_FORMAT_D_M_Y_H_M_S;
import static org.pucar.config.ServiceConstants.DOB_FORMAT_Y_M_D;
import static org.pucar.config.ServiceConstants.ILLEGAL_ARGUMENT_EXCEPTION_CODE;
import static org.pucar.config.ServiceConstants.INVALID_DATE_FORMAT_CODE;
import static org.pucar.config.ServiceConstants.INVALID_DATE_FORMAT_MESSAGE;
import static org.pucar.config.ServiceConstants.LAST_MODIFIED_DATE;
import static org.pucar.config.ServiceConstants.OBJECTMAPPER_UNABLE_TO_CONVERT;
import static org.pucar.config.ServiceConstants.PWD_EXPIRY_DATE;
import static org.pucar.config.ServiceConstants.USER;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.User;
import org.egov.common.contract.user.UserDetailResponse;
import org.egov.tracer.model.CustomException;
import org.pucar.config.Configuration;
import org.pucar.repository.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class UserUtil {

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private ServiceRequestRepository serviceRequestRepository;

	@Autowired
	private Configuration configs;

	@Autowired
	public UserUtil(ObjectMapper mapper, ServiceRequestRepository serviceRequestRepository) {
		this.mapper = mapper;
		this.serviceRequestRepository = serviceRequestRepository;
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
			LinkedHashMap responseMap = (LinkedHashMap) serviceRequestRepository.fetchResult(uri, userRequest);
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

	public void parseResponse(LinkedHashMap responseMap, String dobFormat) {
		List<LinkedHashMap> users = (List<LinkedHashMap>) responseMap.get(USER);
		String format1 = DOB_FORMAT_D_M_Y_H_M_S;
		if (users != null) {
			users.forEach(map -> {
				map.put(CREATED_DATE, dateTolong((String) map.get(CREATED_DATE), format1));
				if ((String) map.get(LAST_MODIFIED_DATE) != null)
					map.put(LAST_MODIFIED_DATE, dateTolong((String) map.get(LAST_MODIFIED_DATE), format1));
				if ((String) map.get(DOB) != null)
					map.put(DOB, dateTolong((String) map.get(DOB), dobFormat));
				if ((String) map.get(PWD_EXPIRY_DATE) != null)
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

	/**
	 * enriches the userInfo with statelevel tenantId and other fields The function
	 * creates user with username as mobile number.
	 * 
	 * @param mobileNumber
	 * @param tenantId
	 * @param userInfo
	 */
	public void addUserDefaultFields(String mobileNumber, String tenantId, User userInfo, String userType) {
		Role role = getCitizenRole(tenantId);
		userInfo.setRoles((List<Role>) Collections.singleton(role));
		userInfo.setType(userType);
		userInfo.setUserName(mobileNumber);
		userInfo.setTenantId(getStateLevelTenant(tenantId));
		// userInfo.setActive(true);
	}

	/**
	 * Returns role object for citizen
	 * 
	 * @param tenantId
	 * @return
	 */
	private Role getCitizenRole(String tenantId) {
		Role role = Role.builder().build();
		role.setCode(CITIZEN_UPPER);
		role.setName(CITIZEN_LOWER);
		role.setTenantId(getStateLevelTenant(tenantId));
		return role;
	}

	public String getStateLevelTenant(String tenantId) {
		return tenantId.split("\\.")[0];
	}

}