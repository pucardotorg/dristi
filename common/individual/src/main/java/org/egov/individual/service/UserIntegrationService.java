package org.egov.individual.service;

import digit.models.coremodels.UserSearchRequest;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.models.user.CreateUserRequest;
import org.egov.common.models.user.UserRequest;
import org.egov.common.service.UserService;
import org.egov.individual.config.IndividualProperties;
import org.egov.individual.web.models.Individual;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserIntegrationService {

    private final UserService userService;

    private final IndividualProperties individualProperties;

    @Autowired
    public UserIntegrationService(UserService userService, IndividualProperties individualProperties) {
        this.userService = userService;
        this.individualProperties = individualProperties;
    }

    public List<UserRequest> createUser(List<Individual> validIndividuals,
                                            RequestInfo requestInfo) {
        log.info("integrating with user service");
        List<UserRequest> userRequests = validIndividuals.stream()
                .filter(Individual::getIsSystemUser).map(toUserRequest())
                .collect(Collectors.toList());
        return userRequests.stream().flatMap(userRequest -> userService.create(
                new CreateUserRequest(requestInfo,
                        userRequest)).stream()).collect(Collectors.toList());
    }


    public List<UserRequest> updateUser(List<Individual> validIndividuals,
                                            RequestInfo requestInfo) {
        log.info("updating the user in user service");
        List<UserRequest> userRequests = validIndividuals.stream()
                .filter(Individual::getIsSystemUser).map(toUserRequest())
                .collect(Collectors.toList());
        return userRequests.stream().flatMap(userRequest -> userService.update(
                new CreateUserRequest(requestInfo,
                        userRequest)).stream()).collect(Collectors.toList());
    }

    public List<UserRequest> deleteUser(List<Individual> validIndividuals,
                                            RequestInfo requestInfo) {
        log.info("deleting the user in user service");
        List<UserRequest> userRequests = validIndividuals.stream()
                .filter(Individual::getIsSystemUser).map(toUserRequest())
                .peek(userRequest -> userRequest.setActive(Boolean.FALSE))
                .collect(Collectors.toList());
        return userRequests.stream().flatMap(userRequest -> userService.update(
                new CreateUserRequest(requestInfo,
                        userRequest)).stream()).collect(Collectors.toList());
    }
    public List<UserRequest> searchUser(List<Individual> validIndividuals,
                                        RequestInfo requestInfo ) {
        log.info("search the user in user service");
        List<String> userRequests = validIndividuals.stream()
                .filter(Individual::getIsSystemUser).map(Individual::getUserUuid)
                .collect(Collectors.toList());
        UserSearchRequest userSearchRequest = new UserSearchRequest();
        userSearchRequest.setRequestInfo(requestInfo);
        userSearchRequest.setUuid(userRequests);
        return userService.search(userSearchRequest).stream().map(userToUserRequest()).collect(Collectors.toList());

    }

    private Function<Individual, UserRequest> toUserRequest() {
        return individual -> IndividualMapper
                .toUserRequest(individual,
                        individualProperties);
    }
    private Function<User,UserRequest> userToUserRequest() {
        return user -> {UserRequest userRequest = new UserRequest();
            userRequest.setId(user.getId());
            userRequest.setUuid(user.getUuid());
            return userRequest;
        };
    }
}
