package org.pucar.dristi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.response.ResponseInfo;
import org.pucar.dristi.model.*;
import org.pucar.dristi.service.EPostService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
@Slf4j
public class EPostController {

    private final EPostService ePostService;

    private final ResponseInfoFactory responseInfoFactory;

    @Autowired
    public EPostController(EPostService ePostService, ResponseInfoFactory responseInfoFactory) {
        this.ePostService = ePostService;
        this.responseInfoFactory = responseInfoFactory;
    }

    @PostMapping("/epost/v1/_sendEPost")
    public ResponseEntity<ChannelResponse> sendEPost(@RequestBody TaskRequest body) throws JsonProcessingException {
        ChannelMessage channelMessage = ePostService.sendEPost(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        ChannelResponse channelResponse = ChannelResponse.builder().channelMessage(channelMessage).responseInfo(responseInfo).build();
        return new ResponseEntity<>(channelResponse, HttpStatus.OK);
    }
    @PostMapping("/epost/v1/_getEPost")
    public ResponseEntity<EPostResponse> getEPost(@Parameter(in = ParameterIn.DEFAULT, description = "Hearing Details and Request Info", required = true, schema = @Schema()) @Valid @RequestBody EPostTrackerSearchRequest request,
                                                  @RequestParam(value = "limit", defaultValue = "10") int limit,
                                                  @RequestParam(value = "offset", defaultValue = "0") int offset) {
        EPostResponse ePostResponse = ePostService.getEPost(request,limit,offset);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), true);
        ePostResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(ePostResponse, HttpStatus.OK);
    }

    @PostMapping("/epost/v1/_updateEPost")
    public ResponseEntity<EPostResponse> updateEPost(@RequestBody EPostRequest body){
        EPostTracker ePostTracker = ePostService.updateEPost(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        EPostResponse ePostResponse = EPostResponse.builder().ePostTrackers(Collections.singletonList(ePostTracker)).responseInfo(responseInfo).build();
        return new ResponseEntity<>(ePostResponse, HttpStatus.OK);
    }
}
