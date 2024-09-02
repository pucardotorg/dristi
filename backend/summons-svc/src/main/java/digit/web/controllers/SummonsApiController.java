package digit.web.controllers;


import digit.service.SummonsService;
import digit.util.ResponseInfoFactory;
import digit.web.models.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-05-29T13:38:04.562296+05:30[Asia/Calcutta]")
@Controller
@RequestMapping
@Slf4j
public class SummonsApiController {

    private final SummonsService summonsService;

    private final ResponseInfoFactory responseInfoFactory;


    @Autowired
    public SummonsApiController(SummonsService summonsService, ResponseInfoFactory responseInfoFactory) {
        this.summonsService = summonsService;
        this.responseInfoFactory = responseInfoFactory;
    }

    @RequestMapping(value = "summons/v1/_generateSummons", method = RequestMethod.POST)
    public ResponseEntity<TaskResponse> generateSummons(@Parameter(in = ParameterIn.DEFAULT, description = "Details for generating a summon.", required = true, schema = @Schema()) @Valid @RequestBody TaskRequest request) {
        TaskResponse taskResponse = summonsService.generateSummonsDocument(request);
        return new ResponseEntity<>(taskResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "summons/v1/_sendSummons", method = RequestMethod.POST)
    public ResponseEntity<SummonsResponse> sendSummons(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the Sending Summons + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody TaskRequest request) {
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), true);
        SummonsDelivery summonsDelivery = summonsService.sendSummonsViaChannels(request);
        SummonsResponse response = SummonsResponse.builder().summonsDelivery(summonsDelivery).responseInfo(responseInfo).build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @RequestMapping(value = "summons/v1/_getSummons", method = RequestMethod.POST)
    public ResponseEntity<SummonsDeliverySearchResponse> getSummons(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the Searching Summons + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody SummonsDeliverySearchRequest request) {
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), true);
        List<SummonsDelivery> summonsDeliveryList = summonsService.getSummonsDelivery(request);
        SummonsDeliverySearchResponse response = SummonsDeliverySearchResponse.builder()
                .summonsDeliveryList(summonsDeliveryList).responseInfo(responseInfo).build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @RequestMapping(value = "summons/v1/_updateSummons", method = RequestMethod.POST)
    public ResponseEntity<UpdateSummonsResponse> updateSummonsStatus(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the Updating Summons + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody UpdateSummonsRequest request) {
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), true);
        ChannelMessage channelMessage = summonsService.updateSummonsDeliveryStatus(request);
        UpdateSummonsResponse response = UpdateSummonsResponse.builder().channelMessage(channelMessage).responseInfo(responseInfo).build();
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
}
