CREATE TABLE dristi_evidence_artifact (
    id uuid NOT NULL PRIMARY KEY,
    tenant_id varchar(36) NOT NULL,
    artifact_number varchar(64) NOT NULL,
    evidence_number varchar(64),
    filing_number varchar(64),
    external_ref_number varchar(128), -- External evidence number
    case_id varchar(36) NOT NULL,
    application_id varchar(36), -- One of application, hearing, or order can be present.
    hearing_id varchar(36),
    order_id varchar(36),
    media_type varchar(64), -- MDMS IDs
    artifact_type varchar(64),
    source_type varchar(36),
    is_evidence boolean DEFAULT FALSE, -- True when judge marks it as evidence
    source_id varchar(36), -- Who has provided this artifact. Source ID or name is mandatory
    source_name varchar(100), -- In cases where the individual is not modeled in the system, capture name
    applicable_to jsonb, -- Who does the evidence pertain to? complainant or litigant individual ID etc.
    created_date int8, -- Date the artifact was uploaded in epoch time
    comments jsonb, -- should contain the individual_id/name of the person commenting and the comment
    is_active bool DEFAULT TRUE,
    wf_status varchar(64),
    description varchar(64),
    artifact_details jsonb, -- Each artifact type has a specific schema defined in MDMS v2
    additional_details jsonb,
    created_by varchar(36) NOT NULL, -- Audit details
    last_modified_by varchar(36) NOT NULL,
    created_time int8 NOT NULL,
    last_modified_time int8 NOT NULL
);

CREATE INDEX idx_dristi_evidence_artifact_number ON dristi_evidence_artifact(artifact_number);
CREATE INDEX idx_dristi_evidence_evidence_number ON dristi_evidence_artifact(evidence_number);
CREATE INDEX idx_dristi_evidence_case_id ON dristi_evidence_artifact(case_id);
CREATE INDEX idx_dristi_evidence_hearing_id ON dristi_evidence_artifact(hearing_id);
CREATE INDEX idx_dristi_evidence_order_id ON dristi_evidence_artifact(order_id);
CREATE INDEX idx_dristi_evidence_application_id ON dristi_evidence_artifact(application_id);
CREATE INDEX idx_dristi_evidence_artifact_type ON dristi_evidence_artifact(artifact_type);
CREATE INDEX idx_dristi_evidence_wf_status ON dristi_evidence_artifact(wf_status);
CREATE INDEX idx_dristi_evidence_is_evidence ON dristi_evidence_artifact(is_evidence);

CREATE TABLE dristi_evidence_document (
    id varchar(64) NOT NULL PRIMARY KEY,
    filestore_id varchar(64),
    document_uid varchar(64),
    document_type varchar(64),
    artifact_id uuid,
    additional_details jsonb,
    CONSTRAINT fk_document_artifact_id FOREIGN KEY(artifact_id) REFERENCES dristi_evidence_artifact(id)
);

CREATE INDEX idx_dristi_evidence_document_filestore ON dristi_evidence_document(filestore_id);
CREATE INDEX idx_dristi_evidence_document_type ON dristi_evidence_document(document_type);

CREATE SEQUENCE seq_dristi_artifact
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_doc_complainant
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_doc_accused
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_doc_court
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_witness_complainant
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_witness_accused
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_witness_court
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
