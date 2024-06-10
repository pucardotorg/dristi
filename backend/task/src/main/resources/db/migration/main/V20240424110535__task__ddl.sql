CREATE TABLE dristi_task (
                              id varchar(64) NOT NULL PRIMARY KEY,
                              tenantId varchar(1000) NOT NULL,
                              resolutionMechanism varchar(64) NULL,
                              caseTitle VARCHAR(64),
                              caseDescription varchar(64) NULL,
                              filingNumber varchar(64) NULL,
                              caseNumber varchar(64) NULL,
                              cnrNumber varchar(64) NULL,
                              courtCaseNumber varchar(64) NULL,
                              accessCode varchar(64) NULL,
                              courtId varchar(64) NULL,
                              benchId varchar(64) NULL,
                              filingDate varchar(64) NULL,
                              registrationDate varchar(64) NULL,
                              caseCategory varchar(64) NULL,
                              natureOfPleading varchar(64) NULL,
                              status varchar(64) NULL,
                              remarks varchar(64) NULL,
                              isActive bool NULL,
                              caseDetails JSONB NULL,
                              additionalDetails jsonb NULL,
                              createdBy varchar(64) NULL,
                              lastModifiedBy varchar(64) NULL,
                              createdTime int8 NULL,
                              lastModifiedTime int8 NULL
);
CREATE TABLE dristi_task_document (
                                      id varchar(64) NOT NULL PRIMARY KEY,
                                      fileStore varchar(64) NULL,
                                      documentUid varchar(64)  NULL ,
                                      documentType varchar(64) NULL,
                                      task_id varchar(64)  NULL,
                                      additionalDetails JSONB NULL
);

CREATE TABLE dristi_task_amount (
                                    id varchar(64) NOT NULL PRIMARY KEY,
                                    relationshipType varchar(64) NULL,
                                    caseNumbers varchar(64)  NULL ,
                                    isActive bool NULL,
                                    task_id varchar(64)  NULL,
                                    additionalDetails JSONB NULL,
                                    createdBy varchar(64) NULL,
                                    lastModifiedBy varchar(64) NULL,
                                    createdTime int8 NULL,
                                    lastModifiedTime int8 NULL
);