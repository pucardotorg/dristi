CREATE TABLE dristi_application (
    id uuid NOT NULL PRIMARY KEY,
    tenant_id varchar(64) NOT NULL,
    case_id varchar(36) NOT NULL,
    filing_number varchar(64),
    cnr_number varchar(64),
    reference_id varchar(64), -- Can be order or hearing number.
    created_date int8 NOT NULL,
    application_created_by varchar(36) NOT NULL, -- Individual ID of the person who created the application
    on_behalf_of varchar(36), -- Individual ID of the person on behalf of whom this application is being filed (parties)
    application_type varchar(64), -- Master data ID of the application type
    application_number varchar(64) NOT NULL, -- Formatted application number
    comments jsonb, -- Comments on the application
    wf_status varchar(64) NOT NULL, -- Workflow status
    is_active bool DEFAULT TRUE,
    additional_details jsonb,
    created_by varchar(36) NOT NULL, -- Audit details
    last_modified_by varchar(36) NOT NULL,
    created_time int8 NOT NULL,
    last_modified_time int8 NOT NULL
);

CREATE INDEX idx_dristi_application_case_id ON dristi_application(tenant_id, case_id);
CREATE INDEX idx_dristi_application_cnr_number ON dristi_application(tenant_id, cnr_number);
CREATE INDEX idx_dristi_application_reference_id ON dristi_application(reference_id);
CREATE INDEX idx_dristi_application_type ON dristi_application(tenant_id, application_type);
CREATE INDEX idx_dristi_application_number ON dristi_application(application_number);
CREATE INDEX idx_dristi_application_wf_status ON dristi_application(wf_status);

CREATE TABLE dristi_statutes_and_sections (
    id uuid NOT NULL PRIMARY KEY,
    tenant_id varchar(64),
    statute_id varchar(64) NOT NULL,
    section_id varchar(64) NOT NULL,
    application_id uuid NOT NULL,
    additional_details jsonb,
    created_by varchar(36) NOT NULL,
    last_modified_by varchar(36) NOT NULL,
    created_time int8 NOT NULL,
    last_modified_time int8 NOT NULL,
    CONSTRAINT fk_statute_application
        FOREIGN KEY(application_id) 
        REFERENCES dristi_application(id)
);

CREATE INDEX idx_statutes_sections_statute_id ON dristi_statutes_and_sections(tenant_id, statute_id);
CREATE INDEX idx_statutes_sections_section_id ON dristi_statutes_and_sections(tenant_id, statute_id, section_id);
CREATE INDEX idx_statutes_sections_application_id ON dristi_statutes_and_sections(statute_id, application_id);

CREATE TABLE dristi_application_document (
    id uuid NOT NULL PRIMARY KEY,
    filestore_id varchar(64) NOT NULL,
    document_uid varchar(64),
    document_type varchar(64), -- Doc type ID from MDMS
    application_id uuid,
    additional_details jsonb,
    created_by varchar(36) NOT NULL,
    last_modified_by varchar(36) NOT NULL,
    created_time int8 NOT NULL,
    last_modified_time int8 NOT NULL,
    CONSTRAINT fk_documents_application 
        FOREIGN KEY(application_id) 
        REFERENCES dristi_application(id)
);

CREATE INDEX idx_dristi_application_document_filestore ON dristi_application_document(filestore_id);
CREATE INDEX idx_dristi_application_document_type ON dristi_application_document(document_type);
CREATE INDEX idx_dristi_application_document_application_id ON dristi_application_document(application_id);

CREATE TABLE dristi_application_approved_by (
    id uuid NOT NULL PRIMARY KEY,
    tenant_id varchar(64) NOT NULL,
    application_id uuid NOT NULL,
    judge_id varchar(36),
    court_id varchar(36) NOT NULL,
    bench_id varchar(36),
    created_by varchar(36) NOT NULL,
    last_modified_by varchar(36) NOT NULL,
    created_time int8 NOT NULL,
    last_modified_time int8 NOT NULL,
    CONSTRAINT fk_approver_application
        FOREIGN KEY(application_id)
        REFERENCES dristi_application(id)
);

CREATE INDEX idx_application_approved_judge ON dristi_application_approved_by(application_id, judge_id);
CREATE INDEX idx_application_approved_court ON dristi_application_approved_by(application_id, court_id);
CREATE INDEX idx_application_approved_bench ON dristi_application_approved_by(application_id, bench_id);

CREATE SEQUENCE seq_dristi_application
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
