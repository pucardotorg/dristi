package pucar.web.controllers;


import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.egov.common.contract.models.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pucar.service.LockService;
import pucar.util.ResponseInfoFactory;
import pucar.web.models.Lock;
import pucar.web.models.LockRequest;
import pucar.web.models.LockResponse;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2025-01-17T15:07:55.861454512+05:30[Asia/Kolkata]")
@RestController
@RequestMapping("")
public class LockApiController {

    private final LockService lockService;

    @Autowired
    public LockApiController(LockService lockService) {
        this.lockService = lockService;
    }


    @RequestMapping(value = "/v1/_get", method = RequestMethod.POST)
    public ResponseEntity<LockResponse> isLocked(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the search of lock + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody RequestInfoWrapper requestInfo,
                                                 @RequestParam(name = "uniqueId") String uniqueId,
                                                 @RequestParam(name = "tenantId") String tenantId) {

        Boolean isLocked = lockService.isLocked(requestInfo.getRequestInfo(), uniqueId, tenantId);

        LockResponse response = LockResponse.builder().responseInfo(
                        ResponseInfoFactory.createResponseInfoFromRequestInfo(requestInfo.getRequestInfo(), true))
                .lock(Lock.builder().uniqueId(uniqueId).tenantId(tenantId).isLocked(isLocked).build()).build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/_set", method = RequestMethod.POST)
    public ResponseEntity<LockResponse> setLock(@Parameter(in = ParameterIn.DEFAULT, description = "Lock table metadata + RequestInfo meta data.", schema = @Schema()) @Valid @RequestBody LockRequest request) {

        Lock lock = lockService.setLock(request.getRequestInfo(), request.getLock());
        LockResponse response = LockResponse.builder()
                .responseInfo(ResponseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), true))
                .lock(lock).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/_release", method = RequestMethod.POST)
    public ResponseEntity<LockResponse> releaseLock(@Parameter(in = ParameterIn.DEFAULT, description = "uniqueId and tenantId + RequestInfo meta data.", schema = @Schema()) @Valid @RequestBody RequestInfoWrapper requestInfo,
                                                    @RequestParam(name = "uniqueId") String uniqueId,
                                                    @RequestParam(name = "tenantId") String tenantId) {

        Boolean releaseLock = lockService.releaseLock(requestInfo.getRequestInfo(), uniqueId, tenantId);

        LockResponse response = LockResponse.builder().responseInfo(
                        ResponseInfoFactory.createResponseInfoFromRequestInfo(requestInfo.getRequestInfo(), releaseLock))
                .lock(Lock.builder().uniqueId(uniqueId).tenantId(tenantId).isLocked(!releaseLock).build()).build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
