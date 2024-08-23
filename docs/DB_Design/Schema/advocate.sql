CREATE TABLE dristi_advocate_clerk (
    id VARCHAR(36),
    tenantId VARCHAR(64) NOT NULL,
    applicationNumber VARCHAR(64),
    wfStatus VARCHAR(64),
    individualId VARCHAR(36),
    isActive BOOLEAN DEFAULT true,
    createdBy varchar(64) NOT NULL,
    stateRegnNumber varchar(64) NOT NULL,
    lastModifiedBy varchar(64) NOT NULL,
    createdTime int8 NOT NULL,
    lastModifiedTime int8 NOT NULL,
    additionalDetails JSONB,
    CONSTRAINT pk_advocate_clerk PRIMARY KEY (id)
);

CREATE INDEX idx_dristi_advocate_clerk_tenant ON dristi_advocate_clerk(tenantId, applicationNumber);
CREATE INDEX idx_dristi_advocate_clerk_status ON dristi_advocate_clerk(tenantId, wfStatus);
CREATE INDEX idx_dristi_advocate_clerk_individual_id ON dristi_advocate_clerk(individualId);

CREATE TABLE dristi_advocate (
                             id varchar(36) NOT NULL PRIMARY KEY,
                             tenantId varchar(64) NOT NULL,
                             applicationNumber varchar(64) NOT NULL,
                             wfStatus VARCHAR(64),
                             barRegistrationNumber varchar(64),
                             advocateType varchar(36),
                             organisationID varchar(36),
                             individualId varchar(36) NOT NULL,
                             isActive bool DEFAULT true,
                             additionalDetails jsonb,
                             createdBy varchar(36) NOT NULL,
                             lastModifiedBy varchar(36) NOT NULL,
                             createdTime int8 NOT NULL,
                             lastModifiedTime int8 NOT NULL
);

CREATE INDEX idx_dristi_advocate_tenant ON dristi_advocate(tenantId, applicationNumber);
CREATE INDEX idx_dristi_advocate_status ON dristi_advocate(tenantId, wfStatus);
CREATE INDEX idx_dristi_advocate_individual_id ON dristi_advocate(individualId);
CREATE INDEX idx_dristi_advocate_barregnnumber ON dristi_advocate(barRegistrationNumber);


CREATE TABLE dristi_advocate_document (
                             id varchar(36) NOT NULL PRIMARY KEY,
                             fileStoreId varchar(36) NOT NULL,
                             documentUid varchar(36) ,
                             documentType varchar(64),
                             advocateId varchar(36) NOT NULL,
                             isActive bool DEFAULT true,
                             additionalDetails jsonb,
                             createdBy varchar(36) NOT NULL,
                             lastModifiedBy varchar(36) NOT NULL,
                             createdTime int8 NOT NULL,
                             lastModifiedTime int8 NOT NULL,
                             CONSTRAINT fk_documents_advocate
                                                        FOREIGN KEY(advocateId) 
                                                        REFERENCES dristi_advocate(id)
);

CREATE INDEX idx_dristi_advocate_document_filestoreId ON dristi_advocate_document(fileStoreId);
CREATE INDEX idx_dristi_advocate_document_advocateId ON dristi_advocate_document(advocateId);

CREATE TABLE dristi_advocate_clerk_document (
                             id varchar(36) NOT NULL PRIMARY KEY,
                             fileStoreId varchar(36) NOT NULL,
                             documentUid varchar(36) ,
                             documentType varchar(64),
                             clerk_id varchar(36) NOT NULL,
                             isActive bool DEFAULT true,
                             additionalDetails jsonb,
                             createdBy varchar(36) NOT NULL,
                             lastModifiedBy varchar(36) NOT NULL,
                             createdTime int8 NOT NULL,
                             lastModifiedTime int8 NOT NULL,
                             CONSTRAINT fk_documents_advocate_clerk
                                                        FOREIGN KEY(clerk_id) 
                                                        REFERENCES dristi_advocate_clerk(id)
);

CREATE INDEX idx_dristi_advocate_clerk_document_filestoreId ON dristi_advocate_clerk_document(fileStoreId);
CREATE INDEX idx_dristi_advocate_document_clerkId ON dristi_advocate_document(clerk_id);
