CREATE TABLE dristi_application (
                                    id varchar(64) NOT NULL PRIMARY KEY,
                                    tenantId varchar(64)  NULL ,
                                    filingNumber varchar(64) NULL,
                                    cnrNumber varchar(64)  NULL ,
                                    referenceId varchar(64)  NULL ,
                                    createdDate varchar(64)  NULL,
                                    onBehalfOf varchar(64)  NULL,
                                    applicationType varchar(64)  NULL,
                                    applicationNumber varchar(64)  NULL,
                                    status varchar(64)  NULL,
                                    comment varchar(64)  NULL,
                                    isActive bool NULL,
                                    statuteSection JSONB(64)  NULL,
                                    auditDetails JSONB(64)  NULL,
                                    documents varchar(64)  NULL,
                                    isActive bool NULL,
                                    additionalDetails JSONB NULL,       //TODO CHECK
                                    issuedBy JSONB NULL,
                                    createdBy varchar(64) NULL,
                                    lastModifiedBy varchar(64) NULL, //
                                    createdTime int8 NULL,
                                    lastModifiedTime int8 NULL
);