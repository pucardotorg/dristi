CREATE TABLE dristi_advocate_clerk (
    id VARCHAR(64),
    tenantId VARCHAR(128),
    applicationNumber VARCHAR(64),
    individualId VARCHAR(36),
    isActive BOOLEAN DEFAULT true,
    createdBy varchar(64) NULL,
    stateRegnNumber varchar(64) NULL,
    lastModifiedBy varchar(64) NULL,
    createdTime int8 NULL,
    lastModifiedTime int8 NULL,
    additionalDetails JSONB,
    CONSTRAINT pk_advocate_clerk PRIMARY KEY (id)
);

CREATE TABLE dristi_advocate_clerk_document (
                             id varchar(64) NOT NULL PRIMARY KEY,
                             fileStore varchar(64) NULL,
                             documentUid varchar(64) NOT NULL ,
                             additionalDetails JSONB,
                             documentType varchar(64) NULL
);


