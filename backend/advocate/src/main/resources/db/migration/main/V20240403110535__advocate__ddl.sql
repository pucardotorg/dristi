CREATE TABLE dristi_advocate (
                             id varchar(64) NOT NULL PRIMARY KEY,
                             tenantId varchar(1000) NOT NULL,
                             applicationNumber varchar(64) NULL,
                             barRegistrationNumber varchar(64) NULL,
                             organisationID varchar(64) NULL,
                             individualId varchar(64) NULL,
                             isActive bool NULL,
                             additionalDetails jsonb NULL,
                             createdBy varchar(64) NULL,
                             lastModifiedBy varchar(64) NULL,
                             createdTime int8 NULL,
                             lastModifiedTime int8 NULL
);
CREATE TABLE dristi_document (
                             id varchar(64) NOT NULL PRIMARY KEY,
                             fileStore varchar(64) NULL,
                             advocateId varchar(64) NOT NULL
);
