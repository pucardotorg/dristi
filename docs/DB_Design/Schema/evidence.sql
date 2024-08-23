CREATE TABLE dristi_evidence_artifact (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    tenantId varchar(36) NOT NULL,
    artifactNumber VARCHAR(64) NOT NULL,
    evidenceNumber VARCHAR(64),
    filingNumber VARCHAR(64),
    externalRefNumber VARCHAR(128),
    caseId varchar(36) NOT NULL,
    -- One of application, hearing or order can be present. 
    application_id VARCHAR(36),
    hearing_id VARCHAR(36),
    order_id VARCHAR(36),
    -- MDMS IDs
    mediaType VARCHAR(64),
    artifactType VARCHAR(64),
    sourceType varchar(36),
    -- True when judge marks it as evidence
    isEvidence boolean DEFAULT FALSE,
    -- Who has provided this artifact. Source ID or name is mandatory
    sourceID VARCHAR(36),
    -- In cases where the individual is not modeled in the system, capture name
    sourceName VARCHAR(100),
    -- Who does the evidence pertain to? complainant or litigant individual ID etc..
    applicableTo VARCHAR(36),
    -- Date the artifact was uploaded in epoch time
    createdDate int8,
    -- Comments
    comments JSONB, -- should contain the individualId/name of the person commenting and the comment
    isActive bool DEFAULT TRUE,
    wfStatus VARCHAR(64),
    description VARCHAR(64),
    -- Each artifact type has a specific schema defined in MDMS v2
    artifactDetails jsonb,
    additionalDetails JSONB,
    -- Audit details
    createdBy varchar(36) NOT NULL,
    lastModifiedBy varchar(36) NOT NULL,
    createdTime int8 NOT NULL,
    lastModifiedTime int8 NOT NULL
);

CREATE INDEX idx_dristi_evidence_artifactNumber ON dristi_evidence_artifact(artifactNumber);
CREATE INDEX idx_dristi_evidence_evidenceNumber ON dristi_evidence_artifact(evidenceNumber);
CREATE INDEX idx_dristi_evidence_case_id ON dristi_evidence_artifact(caseId);
CREATE INDEX idx_dristi_evidence_hearing ON dristi_evidence_artifact(hearing_id);
CREATE INDEX idx_dristi_evidence_order ON dristi_evidence_artifact(order_id);
CREATE INDEX idx_dristi_evidence_application ON dristi_evidence_artifact(application_id);
CREATE INDEX idx_dristi_evidence_artifact_type ON dristi_evidence_artifact(artifactType);
CREATE INDEX idx_dristi_evidence_wf_status ON dristi_evidence_artifact(wfStatus);
CREATE INDEX idx_dristi_evidence_is_evidence ON dristi_evidence_artifact(isEvidence);

CREATE TABLE dristi_evidence_document (
    id varchar(64) NOT NULL PRIMARY KEY,
    fileStoreId varchar(64),
    documentUid varchar(64),
    documentType varchar(64),
    artifactId varchar(64),
    additionalDetails JSONB,
    CONSTRAINT fk_document_artifact_id FOREIGN KEY(artifactId) ON dristi_evidence_artifact(id)
);

CREATE INDEX idx_dristi_evidence_doc_filestore ON dristi_evidence_document(fileStoreId);
CREATE INDEX idx_dristi_evidence_doc_type ON dristi_evidence_document(documentType);


CREATE SEQUENCE SEQ_DRISTI_ARTIFACT
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE SEQ_DOC_COMPLAINANT
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE SEQ_DOC_ACCUSED
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE SEQ_DOC_COURT
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE SEQ_WITNESS_COMPLAINANT
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE SEQ_WITNESS_ACCUSED
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE SEQ_WITNESS_COURT
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;