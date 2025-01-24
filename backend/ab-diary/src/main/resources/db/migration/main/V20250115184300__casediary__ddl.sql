CREATE TABLE dristi_casediary (
    id varchar(36) NOT NULL PRIMARY KEY,
    tenant_id varchar(64) NOT NULL,
    -- varchar(36) of case number
    case_number varchar(36),
    diary_date int8,
    -- master data for diary type - ADiary, BDiary
    diary_type varchar(36) NOT NULL,
    -- varchar(36) of judge Id
    judge_id varchar(36),
    additional_details jsonb,
    created_by varchar(36) NOT NULL,
    last_modified_by varchar(36) NOT NULL,
    created_time int8 NOT NULL,
    last_modified_time int8 NOT NULL
);

CREATE INDEX idx_dristi_casediary_type_judge ON dristi_casediary(tenant_id, diary_type, judge_id);
CREATE INDEX idx_dristi_casediary_date ON dristi_casediary(tenant_id, judge_id, diary_date);

CREATE TABLE dristi_casediary_documents (
    id varchar(36) NOT NULL PRIMARY KEY,
    tenant_id varchar(64) NOT NULL,
    filestore_id varchar(64),
    document_uid varchar(36),
    document_name varchar(128),
    document_type varchar(36),
    casediary_id varchar(36) NOT NULL,
    is_active bool NOT NULL,
    additional_details jsonb,
    created_by varchar(36) NOT NULL,
    last_modified_by varchar(36) NOT NULL,
    created_time int8 NOT NULL,
    last_modified_time int8 NOT NULL,
    CONSTRAINT fk_case_documents_casediary
        FOREIGN KEY(casediary_id)
        REFERENCES dristi_casediary(id)
);

CREATE INDEX idx_dristi_casediary_documents_casediary_id ON dristi_casediary_documents(tenant_id, casediary_id);
CREATE UNIQUE INDEX idx_dristi_casediary_documents_filestore_id ON dristi_casediary_documents(tenant_id, filestore_id);

CREATE TABLE dristi_diaryentries (
    id varchar(36) NOT NULL PRIMARY KEY,
    tenant_id varchar(64) NOT NULL,
    case_number varchar(36),
    entry_date int8 NOT NULL,
    businessOfDay varchar(1024) NOT NULL,
    reference_id varchar(64),
    -- master ID for reference type --
    reference_type varchar(64),
    hearingDate int8 NULL,
    additional_details jsonb,
    created_by varchar(36) NOT NULL,
    last_modified_by varchar(36) NOT NULL,
    created_time int8 NOT NULL,
    last_modified_time int8 NOT NULL,
    judge_id varchar(36) NOT NULL
);

CREATE INDEX idx_dristi_diaryentries_casediary_id ON dristi_diaryentries(tenant_id, case_number);
CREATE INDEX idx_dristi_diaryentries_entry_date ON dristi_diaryentries(tenant_id, entry_date);