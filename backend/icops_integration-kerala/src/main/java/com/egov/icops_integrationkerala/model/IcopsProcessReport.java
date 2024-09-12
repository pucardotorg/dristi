package com.egov.icops_integrationkerala.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class IcopsProcessReport {

    private String processUniqueId;
    private String processCourtCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private String processActionDate;

    private String processActionStatusCd;
    private String processActionStatus;
    private String processActionSubStatusCd;
    private String processActionSubStatus;
    private String processFailureReason;
    private String processMethodOfExecution;
    private String processExecutedTo;
    private String processExecutedToRelation;
    private String processExecutionPlace;
    private String processActionRemarks;
    private String processExecutingOfficerName;
    private String processExecutingOfficerRank;
    private String processExecutingOfficeCode;
    private String processExecutingOffice;
    private String processSubmittingOfficerName;
    private String processSubmittingOfficerRank;
    private String processSubmittingOfficeCode;
    private String processSubmittingOffice;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String processReportSubmittingDateTime;

    private String processReport;
}
