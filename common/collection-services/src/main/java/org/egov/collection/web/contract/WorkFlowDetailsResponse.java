package org.egov.collection.web.contract;

import jakarta.validation.constraints.NotNull;
import org.egov.common.contract.response.ResponseInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class WorkFlowDetailsResponse {
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;
	
	@NotNull
	private String tenantId;
	
	private String receiptNumber;
	
	@NotNull
	private long receiptHeaderId;
	
    private long department;

    private long designation;
    
    private String businessKey;

    @NotNull
    private long user;

    private String comments;
    
    private Long initiatorPosition;
    
    private Long assignee;
    
    @NotNull
    private String action;

    
    private String status;
    
    @NotNull
    private String state;
    
    private long stateId;
}
