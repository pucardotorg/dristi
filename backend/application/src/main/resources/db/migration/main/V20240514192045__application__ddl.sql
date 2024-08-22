CREATE TABLE dristi_application (
                                    id varchar(64) NOT NULL PRIMARY KEY,
                                    tenantId varchar(64)  NULL ,
                                    caseId varchar(64) NOT NULL,
                                    filingNumber varchar(64) NULL,
                                    cnrNumber varchar(64)  NULL ,
                                    referenceId varchar(64)  NULL ,
                                    createdDate varchar(64) NOT NULL,
                                    applicationCreatedBy varchar(64) NULL,
                                    onBehalfOf JSONB  NULL,
                                    applicationType varchar(64)  NULL,
                                    applicationNumber varchar(64)  NULL,
                                    issuedBy JSONB NULL,
                                    status varchar(64) NOT NULL,
                                    comment varchar(64)  NULL,
                                    isActive bool NOT NULL,
                                    documents varchar(64)  NULL,
                                    additionalDetails JSONB NULL,
                                    createdBy varchar(64) NULL,
                                    lastModifiedBy varchar(64) NULL,
                                    createdTime int8 NULL,
                                    lastModifiedTime int8 NULL
);
CREATE TABLE dristi_application_document (
                              id varchar(64) NOT NULL PRIMARY KEY,
                              fileStore varchar(64) NULL,
                              documentUid varchar(64)  NULL ,
                              documentType varchar(64) NULL,
                              application_id varchar(64)  NULL,
                              additionalDetails JSONB NULL
);

CREATE TABLE dristi_application_statute_section (
                              id varchar(64) NOT NULL PRIMARY KEY,
                              tenantId varchar(64) NOT NULL,
                              application_id varchar(64) NOT NULL,
                              statute varchar(64)  NULL ,
                              sections jsonb NULL,
                              strSections varchar(64) NULL,
                              subsections jsonb  NULL,
                              strSubsections varchar(64)  NULL,
                              additionalDetails jsonb NULL,
                              createdBy varchar(64) NULL,
                              lastModifiedBy varchar(64) NULL,
                              createdTime int8 NULL,
                              lastModifiedTime int8 NULL
);

CREATE SEQUENCE seq_dristi_application
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
