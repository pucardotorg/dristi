package drishti.payment.calculator.validator;

import drishti.payment.calculator.web.models.PostalServiceRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;

@Component
public class PostalServiceValidator {
    public void validatePostalServiceRequest(PostalServiceRequest request) {
        request.getPostalServices().forEach(hub -> {
            if (ObjectUtils.isEmpty(hub.getTenantId()))
                throw new CustomException("DK_PC_TENANT_ERR", "tenantId is mandatory for creating postal.");

            if (ObjectUtils.isEmpty(hub.getPostalHubId()))
                throw new CustomException("DK_PC_NAME_ERR", "hub id is mandatory for creating postal.");

            if (ObjectUtils.isEmpty(hub.getPincode()))
                throw new CustomException("DK_PC_PINCODE_ERR", "pincode is mandatory for creating postal.");

            if (ObjectUtils.isEmpty(hub.getDistanceKM()))
                throw new CustomException("DK_PC_DIS_ERR", "distance is mandatory for creating postal.");

        });

    }

    public void validateExistingPostalServiceRequest(PostalServiceRequest request) {
        request.getPostalServices().forEach(postalService -> {
          if(ObjectUtils.isEmpty(postalService.getPostalHubId())) {
              throw new CustomException("DK_PC_HUB_ERR", "hub id is mandatory for updating postal service.");
          }
        });
    }
}
