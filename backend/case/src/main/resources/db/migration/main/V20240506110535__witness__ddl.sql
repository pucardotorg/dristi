CREATE TABLE dristi_witness (
                                    id varchar(64) NOT NULL PRIMARY KEY,
                                    caseID varchar(64)  NULL ,
                                    filingNumber varchar(64) NULL,
                                    cnrNumber varchar(64)  NULL ,
                                    witnessIdentifier varchar(64)  NULL,
                                    individualId varchar(64)  NULL,
                                    remarks varchar(64)  NULL,
                                    isActive bool NULL,
                                    additionalDetails JSONB NULL,
                                    createdBy varchar(64) NULL,
                                    lastModifiedBy varchar(64) NULL,
                                    createdTime int8 NULL,
                                    lastModifiedTime int8 NULL
);