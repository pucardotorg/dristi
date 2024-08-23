CREATE TABLE dristi_application (
                                    id varchar(36) NOT NULL PRIMARY KEY,
                                    tenantId varchar(64) NOT NULL ,
                                    caseId varchar(36) NOT NULL,
                                    filingNumber varchar(64),
                                    cnrNumber varchar(64),
                                    referenceId varchar(64),
                                    createdDate int8 NOT NULL,
                                    statute_id varchar(36),
                                    section_id varchar(36),
                                    subsection_id varchar(36),
                                    -- Individual ID of the person who created the application
                                    applicationCreatedBy varchar(36) NOT NULL,
                                    --individual ID of the person on behalf of whom this application is being filed (parties)
                                    onBehalfOf varchar(36),
                                    --Master data ID of the application type
                                    applicationType varchar(64),
                                    applicationNumber varchar(64) NOT NULL,
                                    -- To be filled when application is approved
                                    issuedByCourtId varchar(36) NOT NULL,
                                    issuedByBenchId varchar(36),
                                    issuedByJudgeId varchar(36),
                                    -- Comments on the application
                                    comments JSONB,
                                    -- Workflow status
                                    wfStatus varchar(64) NOT NULL,
                                    isActive bool DEFAULT TRUE,
                                    additionalDetails JSONB,
                                    -- Audit details
                                    createdBy varchar(36) NOT NULL,
                                    lastModifiedBy varchar(36) NOT NULL,
                                    createdTime int8 NOT NULL,
                                    lastModifiedTime int8 NOT NULL
);

CREATE INDEX idx_dristi_application_case_id ON dristi_application(tenantId,caseId);
CREATE INDEX idx_dristi_application_case_number ON dristi_application(cnrNumber);
CREATE INDEX idx_dristi_application_ref_id ON dristi_application(referenceId);
CREATE INDEX idx_dristi_application_app_type ON dristi_application(applicationType);
CREATE INDEX idx_dristi_application_app_number ON dristi_application(applicationNumber);
CREATE INDEX idx_dristi_application_wf_status ON dristi_application(wfStatus);

CREATE TABLE dristi_application_document (
                              id varchar(36) NOT NULL PRIMARY KEY,
                              fileStoreId varchar(64) NOT NULL,
                              documentUid varchar(64),
                              -- Doc type ID from MDMS
                              documentType varchar(64),
                              application_id varchar(64),
                              additionalDetails JSONB,
                              createdBy varchar(36) NOT NULL,
                              lastModifiedBy varchar(36) NOT NULL,
                              createdTime int8 NOT NULL,
                              lastModifiedTime int8 NOT NULL
                              CONSTRAINT fk_documents_application 
                                FOREIGN KEY(application_id) 
                                REFERENCES dristi_application(id)
);

CREATE INDEX idx_dristi_application_document_filestore ON dristi_application_document(fileStoreId);
CREATE INDEX idx_dristi_application_document_type ON dristi_application_document(documentType);
CREATE INDEX idx_dristi_application_application_id ON dristi_application_document(application_id);


CREATE SEQUENCE seq_dristi_application
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
