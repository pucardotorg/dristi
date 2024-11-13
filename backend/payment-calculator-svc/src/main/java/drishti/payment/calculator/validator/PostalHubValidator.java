package drishti.payment.calculator.validator;

import drishti.payment.calculator.repository.PostalHubRepository;
import drishti.payment.calculator.web.models.HubSearchCriteria;
import drishti.payment.calculator.web.models.PostalHub;
import drishti.payment.calculator.web.models.PostalHubRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static drishti.payment.calculator.config.ServiceConstants.*;

@Component
public class PostalHubValidator {

    private final PostalHubRepository hubRepository;

    @Autowired
    public PostalHubValidator(PostalHubRepository hubRepository) {
        this.hubRepository = hubRepository;
    }


    public void validateExistingPostalHubRequest(PostalHubRequest request) {
        request.getPostalHubs().forEach(hub -> {
            if (ObjectUtils.isEmpty(hub.getHubId()))
                throw new CustomException(DK_PC_ID_ERR, DK_PC_ID_ERR_MSG);
        });
    }

    public void validateCreateHubRequest(PostalHubRequest request) {

        List<String> pincodes = request.getPostalHubs()
                .stream()
                .map(PostalHub::getPincode)
                .toList();

        HubSearchCriteria hubSearchCriteria = HubSearchCriteria.builder().pincode(pincodes).build();
        List<PostalHub> postalHub = hubRepository.getPostalHub(hubSearchCriteria);

        if (!postalHub.isEmpty()) {
            throw new CustomException(HUB_ALREADY_EXIST, HUB_ALREADY_EXIST_MSG);
        }
    }


}
