package digit.web.controllers;

import digit.service.DemandService;
import digit.web.models.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-05-29T13:38:04.562296+05:30[Asia/Calcutta]")
@Controller
@RequestMapping
@Slf4j
public class DemandController {

    private final DemandService demandService;

    @Autowired
    public DemandController(DemandService demandService) {
        this.demandService = demandService;
    }

    @RequestMapping(value = "demand/v1/_generateDemandAndBill", method = RequestMethod.POST)
    public ResponseEntity<BillResponse> generateDemandAndBillForTask(@Parameter(in = ParameterIn.DEFAULT, description = "Details for generating a demand.", required = true, schema = @Schema()) @Valid @RequestBody TaskRequest request) {
        BillResponse billResponse = demandService.fetchPaymentDetailsAndGenerateDemandAndBill(request);
        return new ResponseEntity<>(billResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "demand/v1/_generateBill", method = RequestMethod.POST)
    public ResponseEntity<BillResponse> generateBillForTask(@Parameter(in = ParameterIn.DEFAULT, description = "Details for generating a demand.", required = true, schema = @Schema()) @Valid @RequestBody TaskRequest request) {
        BillResponse billResponse = demandService.getBill(request.getRequestInfo(), request.getTask());
        return new ResponseEntity<>(billResponse, HttpStatus.OK);
    }
}
