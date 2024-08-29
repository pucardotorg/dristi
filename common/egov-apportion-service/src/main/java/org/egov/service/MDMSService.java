package org.egov.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.config.ApportionConfig;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.egov.repository.ServiceRequestRepository;
import org.egov.web.models.ApportionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.egov.util.ApportionConstants.*;

@Service
public class MDMSService {


    private ServiceRequestRepository serviceRequestRepository;

    private ApportionConfig config;

    @Autowired
    public MDMSService(ServiceRequestRepository serviceRequestRepository,ApportionConfig config) {
        this.serviceRequestRepository = serviceRequestRepository;
        this.config = config;
    }

    /**
     * Fetches MDMS data based on the apportion request
     * @param requestInfo The apportion request for which master data is required
     * @return MasterData from MDMS
     */
    public Object mDMSCall(RequestInfo requestInfo,String tenantId){
        MdmsCriteriaReq mdmsCriteriaReq = getMDMSRequest(requestInfo,tenantId);
        Object result = serviceRequestRepository.fetchResult(getMdmsSearchUrl(), mdmsCriteriaReq);
        return result;
    }

    /**
     * Creates MDMS search criteria object MdmsCriteriaReq
     * @param requestInfo The requestInfo of the apportion request
     * @param tenantId TenantId of the request
     * @return MDMS search criteria
     */
    private MdmsCriteriaReq getMDMSRequest(RequestInfo requestInfo, String tenantId){
        ModuleDetail moduleDetail = getBillingModuleDetail();

        List<ModuleDetail> moduleDetails = new LinkedList<>();
        moduleDetails.add(moduleDetail);

        MdmsCriteria mdmsCriteria = MdmsCriteria.builder().moduleDetails(moduleDetails).tenantId(tenantId)
                .build();

        MdmsCriteriaReq mdmsCriteriaReq = MdmsCriteriaReq.builder().mdmsCriteria(mdmsCriteria)
                .requestInfo(requestInfo).build();
        return mdmsCriteriaReq;
    }



    /**
     * Returns the url for mdms search endpoint
     *
     * @return url for mdms search endpoint
     */
    public StringBuilder getMdmsSearchUrl() {
        return new StringBuilder().append(config.getMdmsHost()).append(config.getMdmsEndPoint());
    }


    /**
     * Creates request to search taxhead in mdms
     * @return MDMS request for taxhead
     */
    private ModuleDetail getTaxHeadMasterRequest() {
        // filter to only get code field from master data

      //  final String filterCodeForUom = "$.[?(@.active==true)]";

        ModuleDetail tlModuleDtls = ModuleDetail.builder()
                .masterDetails(Collections.singletonList(MasterDetail.builder().name(MDMS_TAXHEAD).build()))
                .moduleName(MDMS_BILLING_SERVICE).build();

        return tlModuleDtls;
    }


    /**
     * Creates request to search businessService in mdms
     * @return MDMS request for businessService
     */
    private ModuleDetail getBusinessServiceRequest() {
        // filter to only get code field from master data

        //  final String filterCodeForUom = "$.[?(@.active==true)]";

        ModuleDetail tlModuleDtls = ModuleDetail.builder()
                .masterDetails(Collections.singletonList(MasterDetail.builder().name(MDMS_BUSINESSSERVICE).build()))
                .moduleName(MDMS_BILLING_SERVICE).build();

        return tlModuleDtls;
    }


    /**
     * Creates request to search businessService in mdms
     * @return MDMS request for businessService
     */
    private ModuleDetail getBillingModuleDetail() {

        List<MasterDetail> masterDetails = new ArrayList<>();

        MasterDetail businessServiceMasterDetail = MasterDetail.builder().name(MDMS_BUSINESSSERVICE).build();
        MasterDetail taxHeadMasterDetail = MasterDetail.builder().name(MDMS_TAXHEAD).build();

        masterDetails.add(businessServiceMasterDetail);
        masterDetails.add(taxHeadMasterDetail);

        ModuleDetail tlModuleDtls = ModuleDetail.builder()
                .masterDetails(masterDetails)
                .moduleName(MDMS_BILLING_SERVICE).build();

        return tlModuleDtls;
    }







}
