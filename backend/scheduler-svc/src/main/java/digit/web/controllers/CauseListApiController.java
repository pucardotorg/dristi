package digit.web.controllers;


import digit.service.CauseListService;
import digit.util.ResponseInfoFactory;
import digit.web.models.CauseList;
import digit.web.models.CauseListSearchRequest;
import digit.web.models.CauseListResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static digit.config.ServiceConstants.CAUSE_LIST_NOT_FOUND;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-25T11:13:21.813391200+05:30[Asia/Calcutta]")
@Controller
@RequestMapping("/causelist")
@Slf4j
public class CauseListApiController {

    private final CauseListService causeListService;

    @Autowired
    public CauseListApiController(CauseListService causeListService) {
        this.causeListService = causeListService;
    }

    @RequestMapping(value = "/v1/_view", method = RequestMethod.POST)
    public ResponseEntity<CauseListResponse> viewCauseList(@Parameter(in = ParameterIn.DEFAULT, description = "CauseList Search criteria + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody CauseListSearchRequest searchRequest) {
        log.info("api = /causelist/v1/_view, result = IN_PROGRESS");
        List<CauseList> causeLists = causeListService.viewCauseListForTomorrow(searchRequest);
        CauseListResponse causeListResponse = CauseListResponse.builder()
                .responseInfo(ResponseInfoFactory.createResponseInfo(searchRequest.getRequestInfo(), true))
                .causeList(causeLists)
                .build();
        log.info("api = /causelist/v1/_view, result = SUCCESS");
        return new ResponseEntity<>(causeListResponse, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/v1/_download", method = RequestMethod.POST)
    public ResponseEntity<Object> downloadCauseList(@Parameter(in = ParameterIn.DEFAULT, description = "CauseList Search criteria + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody CauseListSearchRequest searchRequest) {
        log.info("api = /causelist/v1/_download, result = IN_PROGRESS");
        try {
            ByteArrayResource resource = causeListService.downloadCauseListForTomorrow(searchRequest);
            String fileName = "causelist" + LocalDate.now().plusDays(1).toString() + ".pdf";
            log.info("api = /causelist/v1/_download, result = SUCCESS");
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (Exception e) {
            log.error("api = /causelist/v1/_download, result = FAILED, error = {}", e.getMessage());
            return new ResponseEntity<>(CAUSE_LIST_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/v1/_generate", method = RequestMethod.POST)
    public ResponseEntity<Object> generateCauseList(@Valid @RequestBody CauseListSearchRequest request) {
        log.info("api = /causelist/v1/_generate, result = IN_PROGRESS");
        try {
            String hearingDate = request.getCauseListSearchCriteria().getSearchDate()!= null ? request.getCauseListSearchCriteria().getSearchDate().toString() : null;
            causeListService.generateCauseList(request.getCauseListSearchCriteria().getCourtId(),
                    new ArrayList<>(),
                    hearingDate,
                    request.getRequestInfo().getUserInfo().getUuid());
            log.info("api = /causelist/v1/_generate, result = SUCCESS");
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("api = /causelist/v1/_generate, result = FAILED, error = {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
