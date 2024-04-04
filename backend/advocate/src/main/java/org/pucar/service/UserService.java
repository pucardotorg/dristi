
package org.pucar.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.contract.user.CreateUserRequest;
import org.egov.common.contract.user.UserDetailResponse;
import org.egov.common.contract.user.UserSearchRequest;
import org.egov.tracer.model.CustomException;
import org.pucar.config.Configuration;
import org.pucar.util.UserUtil;
import org.pucar.web.models.Advocate;
import org.pucar.web.models.AdvocateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;

@Service
@Slf4j
public class UserService {
//    private final UserUtil userUtils;
//    private final Configuration config;
//
//    @Autowired
//    public UserService(UserUtil userUtils, Configuration config) {
//        this.userUtils = userUtils;
//        this.config = config;
//    }
//
//    public void callUserService(AdvocateRequest request) {
//        request.getCases().forEach(advocate -> {
//            User user = createUser(advocate);
//            advocate.setUuid((upsertUser(user, request.getRequestInfo())));
//
//        });
//    }
//
//    User createUser(Advocate advocate) {
//        User father = advocate;
//        User user = User.builder().userName(father.getUserName())
//                .name(father.getName())
//                .mobileNumber(father.getMobileNumber())
//                .emailId(father.getEmailId())
//               // .altContactNumber(father.getAltContactNumber())
//                .tenantId(father.getTenantId())
//                .type(father.getType())
//                .roles(father.getRoles())
//               // .active(father.getActive())
//               // .correspondenceAddress(father.getCorrespondenceAddress())
//               // .permanentAddress(father.getPermanentAddress())
//               // .gender(father.getGender())
//               // .otpReference(father.getOtpReference())
//                .build();
//        return user;
//    }
//    String upsertUser(User user, RequestInfo requestInfo) {
//        String tenantId = user.getTenantId();
//        User userServiceResponse = null;
//
//        UserDetailResponse userDetailResponse = searchUser(userUtils.getStateLevelTenant(tenantId), null, user.getMobileNumber());
//
//        if (!userDetailResponse.getUser().isEmpty()) {
//            User userFromSearch = userDetailResponse.getUser().get(0);
//            if (!user.getUserName().equalsIgnoreCase(userFromSearch.getUserName()) && (!user.getMobileNumber().equalsIgnoreCase(userFromSearch.getUserName()))) {
//                userServiceResponse = updateUser(requestInfo, user, userFromSearch);
//            } else {
//                userServiceResponse = userFromSearch;
//            }
//        } else {
//            userServiceResponse = createUser(requestInfo, tenantId, user);
//        }
//
//        return userServiceResponse.getUuid() ;
//    }
//
//
//    User createUser(RequestInfo requestInfo, String tenantId, User userInfo) {
//        userUtils.addUserDefaultFields(userInfo.getMobileNumber(),tenantId, userInfo,"ADVOCATE");
//        StringBuilder uri = new StringBuilder(config.getUserHost())
//                .append(config.getUserContextPath())
//                .append(config.getUserCreateEndpoint());
//
//        CreateUserRequest user = new CreateUserRequest(requestInfo, userInfo);
//        UserDetailResponse userDetailResponse = userUtils.userCall(user, uri);
//
//        if (CollectionUtils.isEmpty(userDetailResponse.getUser())) {
//            throw new CustomException("USER_CREATION_FAILED", "Failed to create user.");
//        }
//
//        return userDetailResponse.getUser().get(0);
//    }
//
//    User updateUser(RequestInfo requestInfo, User user, User userFromSearch) {
//        User userBis = new User();
//        userBis.setName(user.getName());
//        //userBis.setActive(true);
//
//        StringBuilder uri = new StringBuilder(config.getUserHost())
//                .append(config.getUserContextPath())
//                .append(config.getUserUpdateEndpoint());
//
//        UserDetailResponse userDetailResponse = userUtils.userCall(new CreateUserRequest(requestInfo, userBis), uri);
//
//        if (CollectionUtils.isEmpty(userDetailResponse.getUser())) {
//            throw new CustomException("USER_UPDATE_FAILED", "Failed to update user.");
//        }
//
//        return userDetailResponse.getUser().get(0);
//    }
//
//    public UserDetailResponse searchUser(String stateLevelTenant, String accountId, String userName){
//        UserSearchRequest userSearchRequest = new UserSearchRequest();
//        userSearchRequest.setActive(true);
//        userSearchRequest.setUserType("CITIZEN");
//        userSearchRequest.setTenantId(stateLevelTenant);
//
//        if(StringUtils.isEmpty(accountId) && StringUtils.isEmpty(userName)) {
//            return null;
//        }
//
//        if(!StringUtils.isEmpty(accountId)) {
//            userSearchRequest.setUuid(Collections.singletonList(accountId));
//        }
//
//        if(!StringUtils.isEmpty(userName)) {
//            userSearchRequest.setUserName(userName);
//        }
//
//        StringBuilder uri = new StringBuilder(config.getUserHost()).append(config.getUserSearchEndpoint());
//        UserDetailResponse userDetailResponse = userUtils.userCall(userSearchRequest, uri);
//
//
//        return userDetailResponse;
//    }
}
