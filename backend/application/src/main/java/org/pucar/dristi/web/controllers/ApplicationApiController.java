package org.pucar.dristi.web.controllers;

import org.egov.common.contract.response.ResponseInfo;
import org.pucar.dristi.service.ApplicationService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.models.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.*;
import jakarta.validation.Valid;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:12:15.132164900+05:30[Asia/Calcutta]")
@Controller
@RequestMapping("")
public class ApplicationApiController{

    private ApplicationService applicationService;
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    public ApplicationApiController(ApplicationService applicationService, ResponseInfoFactory responseInfoFactory) {
        this.applicationService = applicationService;
        this.responseInfoFactory = responseInfoFactory;
    }

    @RequestMapping(value="/v1/create", method = RequestMethod.POST)
    public ResponseEntity<ApplicationResponse> applicationV1CreatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the new application + RequestInfo meta data.", required=true, schema=@Schema()) @Valid @RequestBody ApplicationRequest body) {
                    Application application = applicationService.createApplication(body);
                    ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
                    ApplicationResponse applicationResponse = ApplicationResponse.builder().application(application).responseInfo(responseInfo).build();
                    return new ResponseEntity<>(applicationResponse, HttpStatus.OK);
    }

    @RequestMapping(value="/v1/exists", method = RequestMethod.POST)
    public ResponseEntity<ApplicationExistsResponse> applicationV1ExistsPost(@Parameter(in = ParameterIn.DEFAULT, description = "check if the application(S) exists", required=true, schema=@Schema()) @Valid @RequestBody ApplicationExistsRequest body) {
            List<ApplicationExists> applicationExistsList = applicationService.existsApplication(body);
            ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
            ApplicationExistsResponse applicationExistsResponse = ApplicationExistsResponse.builder().applicationExists(applicationExistsList).responseInfo(responseInfo).build();
            return new ResponseEntity<>(applicationExistsResponse, HttpStatus.OK);
    }

    @RequestMapping(value="/v1/search", method = RequestMethod.POST)
    public ResponseEntity<ApplicationListResponse> applicationV1SearchPost(
            @Parameter(in = ParameterIn.DEFAULT, required=true, schema=@Schema()) @Valid @RequestBody ApplicationSearchRequest request) {
                List<Application> applicationList = applicationService.searchApplications(request);
                ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), true);
                int totalCount;
                        if(request.getPagination() != null){
                            totalCount = request.getPagination().getTotalCount().intValue();
                        }else{
                            totalCount = applicationList.size();
                        }
                ApplicationListResponse applicationListResponse = ApplicationListResponse.builder().applicationList(applicationList).totalCount(totalCount).responseInfo(responseInfo).build();
                return new ResponseEntity<>(applicationListResponse, HttpStatus.OK);
        }


    @RequestMapping(value="/v1/update", method = RequestMethod.POST)
    public ResponseEntity<ApplicationResponse> applicationV1UpdatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the update application(s) + RequestInfo meta data.", required=true, schema=@Schema()) @Valid @RequestBody ApplicationRequest body) {
                Application application = applicationService.updateApplication(body,false);
                ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
                ApplicationResponse applicationResponse = ApplicationResponse.builder().application(application).responseInfo(responseInfo).build();
                return new ResponseEntity<>(applicationResponse, HttpStatus.OK);
    }

    @RequestMapping(value="/v1/addcomment", method = RequestMethod.POST)
    public ResponseEntity<ApplicationAddCommentResponse> applicationV1AddCommentPost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the new application + RequestInfo meta data.", required=true, schema=@Schema()) @Valid @RequestBody ApplicationAddCommentRequest body) {
                    applicationService.addComments(body);
                    ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
                    ApplicationAddCommentResponse applicationAddCommentResponse = ApplicationAddCommentResponse.builder().applicationAddComment(body.getApplicationAddComment()).responseInfo(responseInfo).build();
                    return new ResponseEntity<>(applicationAddCommentResponse, HttpStatus.OK);
    }

}
