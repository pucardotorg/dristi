package org.pucar.dristi.web.controllers;


import org.egov.common.contract.response.ResponseInfo;
import org.pucar.dristi.service.ApplicationService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.models.*;

import java.util.UUID;
    import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;
import java.util.*;

    import jakarta.validation.constraints.*;
    import jakarta.validation.Valid;
    import jakarta.servlet.http.HttpServletRequest;
        import java.util.Optional;
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:12:15.132164900+05:30[Asia/Calcutta]")
@Controller
@RequestMapping("")
public class ApplicationApiController{

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    public ApplicationApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @RequestMapping(value="/application/v1/create", method = RequestMethod.POST)
    public ResponseEntity<ApplicationResponse> applicationV1CreatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the new application + RequestInfo meta data.", required=true, schema=@Schema()) @Valid @RequestBody ApplicationRequest body) {
                    Application application = applicationService.createApplication(body);
                    ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
                    ApplicationResponse applicationResponse = ApplicationResponse.builder().application(application).responseInfo(responseInfo).build();
                    return new ResponseEntity<>(applicationResponse, HttpStatus.OK);
    }

    @RequestMapping(value="/application/v1/exists", method = RequestMethod.POST)
    public ResponseEntity<ApplicationExistsResponse> applicationV1ExistsPost(@Parameter(in = ParameterIn.DEFAULT, description = "check if the application(S) exists", required=true, schema=@Schema()) @Valid @RequestBody ApplicationExistsRequest body) {
            List<ApplicationExists> applicationExistsList = applicationService.existsApplication(body);
            ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
            ApplicationExistsResponse applicationExistsResponse = ApplicationExistsResponse.builder().applicationExists(applicationExistsList).responseInfo(responseInfo).build();
            return new ResponseEntity<>(applicationExistsResponse, HttpStatus.OK);
    }

    @RequestMapping(value="/application/v1/search", method = RequestMethod.POST)
    public ResponseEntity<ApplicationListResponse> applicationV1SearchPost(
            @Parameter(in = ParameterIn.QUERY, description = "No of records return" ,schema=@Schema()) @Valid @RequestParam(value = "limit", required = false) Integer limit,
            @Parameter(in = ParameterIn.QUERY, description = "offset" ,schema=@Schema()) @Valid @RequestParam(value = "offset", required = false) Integer offset,
            @Parameter(in = ParameterIn.QUERY, description = "sorted by ascending by default if this parameter is not provided" , schema=@Schema()) @Valid @RequestParam(value = "sortBy", required = false) String sortBy,
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


    @RequestMapping(value="/application/v1/update", method = RequestMethod.POST)
    public ResponseEntity<ApplicationResponse> applicationV1UpdatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the update application(s) + RequestInfo meta data.", required=true, schema=@Schema()) @Valid @RequestBody ApplicationRequest body) {
                Application application = applicationService.updateApplication(body);
                ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
                ApplicationResponse applicationResponse = ApplicationResponse.builder().application(application).responseInfo(responseInfo).build();
                return new ResponseEntity<>(applicationResponse, HttpStatus.OK);
    }

}
