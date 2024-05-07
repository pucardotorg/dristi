CREATE TABLE dristi_cases (
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
CREATE TABLE dristi_case_document (
                                      id varchar(64) NOT NULL PRIMARY KEY,
                                      fileStore varchar(64) NULL,
                                      documentUid varchar(64)  NULL ,
                                      documentType varchar(64) NULL,
                                      case_id varchar(64)  NULL,
                                      linked_case_id varchar(64)  NULL,
                                      litigant_id varchar(64)  NULL,
                                      representative_id varchar(64)  NULL,
                                      representing_id varchar(64)  NULL,
                                      additionalDetails JSONB NULL
);

CREATE TABLE dristi_linked_case (
                                    id varchar(64) NOT NULL PRIMARY KEY,
                                    relationshipType varchar(64) NULL,
                                    caseNumbers varchar(64)  NULL ,
                                    isActive bool NULL,
                                    case_id varchar(64)  NULL,
                                    additionalDetails JSONB NULL,
                                    createdBy varchar(64) NULL,
                                    lastModifiedBy varchar(64) NULL,
                                    createdTime int8 NULL,
                                    lastModifiedTime int8 NULL
);

CREATE TABLE dristi_case_statutes_and_sections (
                                                   id varchar(64) NOT NULL PRIMARY KEY,
                                                   tenantId varchar(64) NULL,
                                                   statutes varchar(64)  NULL ,
                                                   sections varchar(64) NULL,
                                                   subsections varchar(64)  NULL,
                                                   case_id varchar(64)  NULL,
                                                   additionalDetails JSONB NULL,
                                                   createdBy varchar(64) NULL,
                                                   lastModifiedBy varchar(64) NULL,
                                                   createdTime int8 NULL,
                                                   lastModifiedTime int8 NULL
);


CREATE TABLE dristi_case_litigants (
                                       id varchar(64) NOT NULL PRIMARY KEY,
                                       tenantId varchar(64) NULL,
                                       partyCategory varchar(64)  NULL ,
                                       individualId varchar(64)  NULL ,
                                       organisationID varchar(64)  NULL ,
                                       partyType varchar(64)  NULL ,
                                       isActive bool NULL,
                                       case_id varchar(64)  NULL,
                                       additionalDetails JSONB NULL,
                                       createdBy varchar(64) NULL,
                                       lastModifiedBy varchar(64) NULL,
                                       createdTime int8 NULL,
                                       lastModifiedTime int8 NULL
);

CREATE TABLE dristi_case_representatives (
                                             id varchar(64) NOT NULL PRIMARY KEY,
                                             tenantId varchar(64) NULL,
                                             advocateId varchar(64)  NULL ,
                                             isActive bool NULL,
                                             case_id varchar(64)  NULL,
                                             additionalDetails JSONB NULL,
                                             createdBy varchar(64) NULL,
                                             lastModifiedBy varchar(64) NULL,
                                             createdTime int8 NULL,
                                             lastModifiedTime int8 NULL
);

CREATE TABLE dristi_case_representing (
                                          id varchar(64) NOT NULL PRIMARY KEY,
                                          tenantId varchar(64) NULL,
                                          partyCategory varchar(64)  NULL ,
                                          individualId varchar(64)  NULL ,
                                          organisationId varchar(64)  NULL ,
                                          caseId varchar(64)  NULL ,
                                          partyType varchar(64)  NULL ,
                                          isActive bool NULL,
                                          representative_id varchar(64)  NULL,
                                          additionalDetails JSONB NULL,
                                          createdBy varchar(64) NULL,
                                          lastModifiedBy varchar(64) NULL,
                                          createdTime int8 NULL,
                                          lastModifiedTime int8 NULL
);