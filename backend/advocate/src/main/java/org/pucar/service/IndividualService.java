
package org.pucar.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.User;
import org.pucar.config.Configuration;
import org.pucar.util.UserUtil;
import org.pucar.web.models.AdvocateClerk;
import org.pucar.web.models.AdvocateClerkRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IndividualService {
    private final UserUtil userUtils;
    private final Configuration config;

    @Autowired
    public IndividualService(UserUtil userUtils, Configuration config) {
        this.userUtils = userUtils;
        this.config = config;
    }

}
