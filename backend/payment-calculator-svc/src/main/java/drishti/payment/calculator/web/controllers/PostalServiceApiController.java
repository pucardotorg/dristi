package drishti.payment.calculator.web.controllers;

import drishti.payment.calculator.service.PostalPinService;
import drishti.payment.calculator.util.ResponseInfoFactory;
import drishti.payment.calculator.web.models.PostalService;
import drishti.payment.calculator.web.models.PostalServiceRequest;
import drishti.payment.calculator.web.models.PostalServiceResponse;
import drishti.payment.calculator.web.models.PostalServiceSearchRequest;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-06-10T14:05:42.847785340+05:30[Asia/Kolkata]")
@Controller
@RequestMapping("")
@Slf4j
public class PostalServiceApiController {

    private final PostalPinService postalService;

    @Autowired
    public PostalServiceApiController(PostalPinService postalService) {
        this.postalService = postalService;
    }

    @PostMapping(value = "/postal/v1/_create")
    public ResponseEntity<PostalServiceResponse> createHub(@Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody PostalServiceRequest request) {
        log.info("api=/postal/v1/_create, result=IN_PROGRESS");
        List<PostalService> postalServices = postalService.create(request);
        PostalServiceResponse response = PostalServiceResponse.builder().postal(postalServices).responseInfo(ResponseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), true)).build();
        log.info("api=/postal/v1/_create, result=SUCCESS");
        return ResponseEntity.accepted().body(response);

    }

    @PostMapping(value = "/postal/v1/_search")
    public ResponseEntity<PostalServiceResponse> searchHub(@Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody PostalServiceSearchRequest request) {
        log.info("api=/postal/v1/_search, result=IN_PROGRESS");
        List<PostalService> search = postalService.search(request);
        PostalServiceResponse response = PostalServiceResponse.builder().postal(search).responseInfo(ResponseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), true)).build();
        log.info("api=/postal/v1/_search, result=SUCCESS");
        return ResponseEntity.accepted().body(response);
    }

    @PostMapping(value = "/postal/v1/_update")
    public ResponseEntity<PostalServiceResponse> updateHub(@Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody PostalServiceRequest request) {
        log.info("api=/postal/v1/_update, result=IN_PROGRESS");
        List<PostalService> update = postalService.update(request);
        PostalServiceResponse response = PostalServiceResponse.builder().postal(update).responseInfo(ResponseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), true)).build();
        log.info("api=/postal/v1/_update, result=SUCCESS");
        return ResponseEntity.accepted().body(response);

    }
}
