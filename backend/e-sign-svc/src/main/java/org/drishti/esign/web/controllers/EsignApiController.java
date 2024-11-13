package org.drishti.esign.web.controllers;


import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.drishti.esign.service.ESignService;
import org.drishti.esign.util.ResponseInfoFactory;
import org.drishti.esign.web.models.ESignRequest;
import org.drishti.esign.web.models.ESignResponse;
import org.drishti.esign.web.models.ESignXmlForm;
import org.drishti.esign.web.models.SignDocRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-06-19T13:37:37.165763478+05:30[Asia/Kolkata]")
@RestController
@RequestMapping("")
@Slf4j
public class EsignApiController {

    private final ESignService eSignService;

    @Autowired
    public EsignApiController(ESignService eSignService) {
        this.eSignService = eSignService;
    }

    @PostMapping("/v1/_esign")
    public ResponseEntity<ESignResponse> eSignDoc(@Parameter(in = ParameterIn.DEFAULT, description = "ESign Doc Details and Request Info", required = true, schema = @Schema()) @Valid @RequestBody ESignRequest request) {
        log.info("api=/v1/_esign, result = IN_PROGRESS");
        ESignXmlForm eSignXmlForm = eSignService.signDoc(request);
        ESignResponse response = ESignResponse.builder().responseInfo(ResponseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), true))
                .eSignForm(eSignXmlForm).build();
        log.info("api=/v1/_esign, result = SUCCESS");
        return ResponseEntity.accepted().body(response);
    }


    @PostMapping("/v1/_signed")
    public ResponseEntity<String> eSignDOC(@Parameter(in = ParameterIn.DEFAULT, description = "ESign Doc Details and Request Info", required = true, schema = @Schema()) @Valid @RequestBody SignDocRequest request) {
        log.info("api=/v1/_signed, result = IN_PROGRESS");
        String fileStoreId = eSignService.signDocWithDigitalSignature(request);
        log.info("api=/v1/_signed, result = SUCCESS");
        return ResponseEntity.accepted().body(fileStoreId);
    }


}


