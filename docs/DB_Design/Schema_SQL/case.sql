CREATE TABLE dristi_case (
    id varchar(36) NOT NULL PRIMARY KEY,
    tenant_id varchar(64) NOT NULL,
    resolution_mechanism varchar(128),
    case_title varchar(512),
    case_description varchar(1028),
    filing_number varchar(64) NOT NULL,
    cnr_number varchar(32),
    court_case_number varchar(24),
    access_code varchar(10),
    court_id varchar(64),
    bench_id varchar(64),
    judge_id varchar(64),
    stage varchar(128),
    substage varchar(128),
    filing_date int8,
    registration_date int8,
    case_category varchar(36),
    nature_of_pleading varchar(36),
    wf_status varchar(64),
    remarks varchar(2056),
    outcome varchar(128),
    is_active bool NOT NULL,
    case_details jsonb,
    witnesses jsonb,
    additional_details jsonb,
    created_by varchar(64) NOT NULL,
    last_modified_by varchar(64) NOT NULL,
    created_time int8 NOT NULL,
    last_modified_time int8 NOT NULL
);

CREATE UNIQUE INDEX idx_dristi_case_filing_number ON dristi_case(tenant_id, filing_number);
CREATE UNIQUE INDEX idx_dristi_case_cnr_number ON dristi_case(tenant_id, cnr_number);
CREATE UNIQUE INDEX idx_dristi_case_court_case_number ON dristi_case(tenant_id, court_case_number);
CREATE INDEX idx_dristi_case_tenant_id ON dristi_case(tenant_id);
CREATE INDEX idx_dristi_case_filing_date ON dristi_case(filing_date);
CREATE INDEX idx_dristi_case_registration_date ON dristi_case(registration_date);
CREATE INDEX idx_dristi_case_court_id ON dristi_case(court_id);
CREATE INDEX idx_dristi_case_bench_id ON dristi_case(bench_id);
CREATE INDEX idx_dristi_case_judge_id ON dristi_case(judge_id);
CREATE INDEX idx_dristi_case_wf_status ON dristi_case(wf_status);
CREATE INDEX idx_dristi_case_stage ON dristi_case(stage, substage);

CREATE TABLE dristi_case_documents (
    id varchar(36) NOT NULL PRIMARY KEY,
    tenant_id varchar(64) NOT NULL,
    filestore_id varchar(64),
    document_uid varchar(64),
    document_name varchar(128),
    document_type varchar(64),
    case_id varchar(36) NOT NULL,
    is_active bool NOT NULL,
    additional_details jsonb,
    created_by varchar(36) NOT NULL,
    last_modified_by varchar(36) NOT NULL,
    created_time int8 NOT NULL,
    last_modified_time int8 NOT NULL,
    CONSTRAINT fk_case_documents_case
        FOREIGN KEY(case_id) 
        REFERENCES dristi_case(id)
);

CREATE INDEX idx_dristi_case_documents_case_id ON dristi_case_documents(tenant_id, case_id);
CREATE INDEX idx_dristi_case_documents_filestore_id ON dristi_case_documents(tenant_id, filestore_id);
CREATE INDEX idx_dristi_case_document_name ON dristi_case_documents(case_id, document_name);
CREATE INDEX idx_dristi_case_document_type ON dristi_case_documents(case_id, document_type);

-- Table to store other linked cases
CREATE TABLE dristi_linked_case (
    id varchar(36) NOT NULL PRIMARY KEY,
    -- MDMS ID of the relationship type between cases
    relationship_type varchar(64) NULL,
    -- CNR number of the linked case
    cnr_number varchar(64) NULL,
    is_active bool NULL,
    case_id varchar(36) NOT NULL,
    additional_details jsonb NULL,
    created_by varchar(64) NULL,
    last_modified_by varchar(64) NULL,
    created_time int8 NULL,
    last_modified_time int8 NULL,
    CONSTRAINT fk_linked_case_case
        FOREIGN KEY(case_id) 
        REFERENCES dristi_case(id)
);

CREATE INDEX idx_dristi_linked_case_case_id ON dristi_linked_case(case_id);

CREATE TABLE dristi_linked_case_documents (
    id varchar(36) PRIMARY KEY,
    tenant_id varchar(64) NOT NULL,
    filestore_id varchar(64) NOT NULL,
    document_uid varchar(64),
    document_name varchar(128),
    document_type varchar(64),
    case_id varchar(36) NOT NULL,
    is_active bool NOT NULL,
    additional_details jsonb,
    created_by varchar(36) NOT NULL,
    last_modified_by varchar(36) NOT NULL,
    created_time int8 NOT NULL,
    last_modified_time int8 NOT NULL,
    CONSTRAINT fk_linked_case_documents_case
        FOREIGN KEY(case_id) 
        REFERENCES dristi_linked_case(id)
);

CREATE TABLE dristi_case_statutes_and_sections (
    id varchar(36) NOT NULL PRIMARY KEY,
    tenant_id varchar(64) NOT NULL,
    -- Master data IDs of statute and section
    statute_id varchar(64) NOT NULL,
    section_id varchar(64),
    -- varchar(36) of case
    case_id varchar(36) NOT NULL,
    additional_details jsonb,
    -- Audit details
    created_by varchar(36) NOT NULL,
    last_modified_by varchar(36) NOT NULL,
    created_time int8 NOT NULL,
    last_modified_time int8 NOT NULL,
    CONSTRAINT fk_statute_case
        FOREIGN KEY(case_id) 
        REFERENCES dristi_case(id)
);

CREATE INDEX idx_dristi_case_statutes_combination ON dristi_case_statutes_and_sections(statute_id, section_id);
CREATE INDEX idx_dristi_case_statutes_tenant_id ON dristi_case_statutes_and_sections(tenant_id);
CREATE INDEX idx_dristi_case_statutes_case_id ON dristi_case_statutes_and_sections(case_id);
CREATE INDEX idx_dristi_case_statutes_statute_id ON dristi_case_statutes_and_sections(statute_id);
CREATE INDEX idx_dristi_case_statutes_section_id ON dristi_case_statutes_and_sections(section_id);

CREATE TABLE dristi_case_litigants (
    id varchar(36) NOT NULL PRIMARY KEY,
    tenant_id varchar(64) NOT NULL,
    party_category_id varchar(36) NOT NULL,
    individual_id varchar(36),
    organisation_id varchar(36),
    party_type_id varchar(36),
    is_representing_self bool DEFAULT FALSE,
    is_active bool NOT NULL,
    case_id varchar(36) NOT NULL,
    additional_details jsonb,
    created_by varchar(36),
    last_modified_by varchar(36),
    created_time int8,
    last_modified_time int8,
    CONSTRAINT fk_litigant_case
        FOREIGN KEY(case_id) 
        REFERENCES dristi_case(id)
);

CREATE UNIQUE INDEX idx_dristi_case_litigant_combination ON dristi_case_litigants(case_id, individual_id);
CREATE INDEX idx_dristi_case_litigants_case_id ON dristi_case_litigants(case_id);
CREATE INDEX idx_dristi_case_litigants_tenant_id ON dristi_case_litigants(tenant_id);
CREATE INDEX idx_dristi_case_litigants_party_category_id ON dristi_case_litigants(party_category_id);
CREATE INDEX idx_dristi_case_litigants_party_type_id ON dristi_case_litigants(party_type_id);
CREATE INDEX idx_dristi_case_litigants_individual_id ON dristi_case_litigants(individual_id);
CREATE INDEX idx_dristi_case_litigants_organisation_id ON dristi_case_litigants(organisation_id);

CREATE TABLE dristi_case_representatives (
    id varchar(36) NOT NULL PRIMARY KEY,
    tenant_id varchar(64) NOT NULL,
    case_id varchar(36),
    party_id varchar(36) NOT NULL,
    individual_id varchar(36),
    advocate_id varchar(36),
    is_active BOOLEAN DEFAULT true,
    created_by varchar(36),
    last_modified_by varchar(36),
    created_time int8,
    last_modified_time int8,
    CONSTRAINT fk_case_representative_litigant_case
        FOREIGN KEY(party_id)
        REFERENCES dristi_case_litigants(id),
    CONSTRAINT fk_case_representative_case
        FOREIGN KEY(case_id)
        REFERENCES dristi_case(id)
);

CREATE INDEX idx_dristi_case_rep_case_id ON dristi_case_representatives(tenant_id, case_id);
CREATE INDEX idx_dristi_case_rep_party_id ON dristi_case_representatives(case_id, party_id);
CREATE INDEX idx_dristi_case_rep_individual_id ON dristi_case_representatives(case_id, individual_id);
CREATE INDEX idx_dristi_case_rep_advocate_id ON dristi_case_representatives(case_id, advocate_id);

CREATE TABLE dristi_representative_documents (
    id varchar(36) NOT NULL PRIMARY KEY,
    tenant_id varchar(64) NOT NULL,
    case_id varchar(36) NOT NULL,
    filestore_id varchar(64) NOT NULL,
    document_uid varchar(64),
    document_name varchar(128),
    document_type varchar(64),
    representative_id varchar(36) NOT NULL,
    is_active bool NOT NULL,
    additional_details jsonb,
    created_by varchar(36) NOT NULL,
    last_modified_by varchar(36) NOT NULL,
    created_time int8 NOT NULL,
    last_modified_time int8 NOT NULL,
    CONSTRAINT fk_case_representative_document_case
        FOREIGN KEY(representative_id) 
        REFERENCES dristi_case_representatives(id),
    CONSTRAINT fk_case_representative_document_case_id
        FOREIGN KEY(case_id)
        REFERENCES dristi_case(id)
);

-- Composite Index on tenant_id and case_id
CREATE INDEX idx_dristi_rep_documents_tenant_case ON dristi_representative_documents(tenant_id, case_id);

-- Composite Index on tenant_id and representative_id
CREATE INDEX idx_dristi_rep_documents_tenant_rep ON dristi_representative_documents(tenant_id, representative_id);

-- Composite Index on filestore_id and document_uid
CREATE INDEX idx_dristi_rep_documents_filestore_document ON dristi_representative_documents(filestore_id, document_uid);
CREATE INDEX idx_dristi_rep_documents_case_document_name ON dristi_representative_documents(case_id, document_name);
CREATE INDEX idx_dristi_rep_documents_case_document_type ON dristi_representative_documents(case_id, document_type);

-- Individual Indexes
CREATE INDEX idx_dristi_rep_documents_tenant_id ON dristi_representative_documents(tenant_id);
CREATE INDEX idx_dristi_rep_documents_case_id ON dristi_representative_documents(case_id);
CREATE INDEX idx_dristi_rep_documents_filestore_id ON dristi_representative_documents(filestore_id);
CREATE INDEX idx_dristi_rep_documents_document_uid ON dristi_representative_documents(document_uid);
CREATE INDEX idx_dristi_rep_documents_rep_id ON dristi_representative_documents(representative_id);
CREATE INDEX idx_dristi_rep_documents_doc_type ON dristi_representative_documents(document_type);
CREATE INDEX idx_dristi_rep_documents_doc_name ON dristi_representative_documents(document_name);



CREATE TABLE dristi_litigant_documents (
    id varchar(36) NOT NULL PRIMARY KEY,
    tenant_id varchar(64) NOT NULL,
    filestore_id varchar(64),
    document_uid varchar(64),
    document_name varchar(128),
    document_type varchar(64),
    case_id varchar(36) NOT NULL,
    party_id varchar(36) NOT NULL,
    representative_id varchar(64) NOT NULL,
    is_active bool DEFAULT true,
    additional_details jsonb,
    created_by varchar(36) NOT NULL,
    last_modified_by varchar(36) NOT NULL,
    created_time int8 NOT NULL,
    last_modified_time int8 NOT NULL,
    CONSTRAINT fk_case_document_case
        FOREIGN KEY(case_id) 
        REFERENCES dristi_case(id),
    CONSTRAINT fk_case_document_party
        FOREIGN KEY(party_id)
        REFERENCES dristi_case_litigants(id)
);

CREATE INDEX idx_dristi_litigant_docs_case_id ON dristi_litigant_documents(tenant_id, case_id);
CREATE INDEX idx_dristi_litigant_docs_advocate_id ON dristi_litigant_documents(representative_id);
CREATE INDEX idx_dristi_litigant_docs_party_id ON dristi_litigant_documents(party_id);

-- Holds the sequence number for CNR for each courtID. A courtID represent StateDistrictEstablishment (court room) and a CNR is specific to that. Eample for courtID - KLKM52
CREATE TABLE dristi_cnr_master (
    id varchar(36) NOT NULL PRIMARY KEY,
    tenant_id varchar(64) NOT NULL,
    court_id varchar(64),
    cnr_seq_num integer DEFAULT 1,
    created_by varchar(64) NOT NULL,
    last_modified_by varchar(64) NOT NULL,
    created_time int8 NULL,
    last_modified_time int8 NULL,
    CONSTRAINT fk_cnr_master_case
        FOREIGN KEY(court_id)
        REFERENCES dristi_case(court_id)
);
CREATE INDEX idx_dristi_cnr_master_court_id ON dristi_cnr_master(tenant_id, court_id);

-- Holds the sequence numbers for different types of numbers used in a Case
CREATE TABLE dristi_case_numbering(
    id varchar(36) NOT NULL PRIMARY KEY,
    tenant_id varchar(64) NOT NULL,
    cnr_number varchar(32) NOT NULL,
    seq_num_lable varchar(64) NOT NULL,
    case_seq_num integer DEFAULT 1,
    created_by varchar(64) NOT NULL,
    last_modified_by varchar(64) NOT NULL,
    created_time int8 NULL,
    last_modified_time int8 NULL,
    CONSTRAINT fk_case_numbering_case
        FOREIGN KEY(case_id) 
        REFERENCES dristi_case(cnr_number)
);
CREATE INDEX idx_dristi_case_numbering_seq_num ON dristi_case_numbering(tenant_id, cnr_number,seq_num_lable);

CREATE TABLE dristi_witness (
    id varchar(36) NOT NULL PRIMARY KEY,
    case_id varchar(36) NOT NULL,
    filing_number varchar(64),
    cnr_number varchar(64),
    witness_identifier varchar(64),
    witness_name varchar(100),
    witness_phone_number varchar(20),
    witness_email varchar(100),
    witness_address jsonb,
    individual_id varchar(64),
    remarks varchar(64),
    is_active bool NOT NULL,
    additional_details jsonb,
    created_by varchar(64) NOT NULL,
    last_modified_by varchar(64) NOT NULL,
    created_time int8 NULL,
    last_modified_time int8 NULL,
    CONSTRAINT fk_witness_case
        FOREIGN KEY(case_id) 
        REFERENCES dristi_case(id)
);

CREATE INDEX idx_dristi_witness_case_id ON dristi_witness(case_id);
CREATE INDEX idx_dristi_witness_individual_id ON dristi_witness(individual_id);
CREATE INDEX idx_dristi_witness_name ON dristi_witness(witness_name);
CREATE INDEX idx_dristi_witness_filing_number ON dristi_witness(filing_number);
CREATE INDEX idx_dristi_witness_cnr_number ON dristi_witness(cnr_number);
CREATE INDEX idx_dristi_witness_witness_identifier ON dristi_witness(witness_identifier);
