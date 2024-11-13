CREATE TABLE dristi_hearing (
    id uuid NOT NULL PRIMARY KEY,
    tenant_id varchar(64) NOT NULL,
    hearing_id varchar(64) NOT NULL, -- Formatted ID
    hearing_type varchar(64) NOT NULL, -- Master data ID for hearing type
    wf_status varchar(64),
    start_time int8, -- Epoch time
    end_time int8, -- Epoch time
    vc_link varchar(255),
    is_active boolean DEFAULT TRUE,
    notes varchar(1028) NULL,
    transcripts jsonb, -- Renamed appropriately
    created_by varchar(36) NOT NULL, -- Audit details
    last_modified_by varchar(36) NOT NULL,
    created_time int8 NOT NULL,
    last_modified_time int8 NOT NULL,
    CONSTRAINT chk_start_time_end_time CHECK (start_time IS NULL OR end_time IS NULL OR start_time <= end_time)
);

-- Indexes on frequently queried columns
CREATE INDEX idx_dristi_hearing_tenant_id ON dristi_hearing(tenant_id);
CREATE INDEX idx_dristi_hearing_hearing_id ON dristi_hearing(hearing_id);
CREATE INDEX idx_dristi_hearing_hearing_type ON dristi_hearing(hearing_type);
CREATE INDEX idx_dristi_hearing_wf_status ON dristi_hearing(wf_status);

CREATE TABLE hearing_presided_by (
    id uuid PRIMARY KEY,
    tenant_id varchar(64) NOT NULL,
    hearing_id uuid NOT NULL,
    case_id varchar(36),
    judge_id varchar(36),
    court_id varchar(36),
    bench_id varchar(36),
    is_active boolean DEFAULT TRUE,
    created_by varchar(36) NOT NULL, -- Audit details
    last_modified_by varchar(36) NOT NULL,
    created_time int8 NOT NULL,
    last_modified_time int8 NOT NULL
    FOREIGN KEY (hearing_id) REFERENCES dristi_hearing(id)
);

-- Composite indexes
CREATE INDEX idx_hearing_presided_tenant_case_combination ON hearing_presided_by(tenant_id, case_id);
CREATE INDEX idx_hearing_presided_case_hearing_combination ON hearing_presided_by(case_id, hearing_id);
CREATE INDEX idx_hearing_presided_case_judge_combination ON hearing_presided_by(case_id, judge_id);
CREATE INDEX idx_hearing_presided_case_court_combination ON hearing_presided_by(case_id, court_id);

-- Individual indexes
CREATE INDEX idx_hearing_presided_case_id ON hearing_presided_by(case_id);
CREATE INDEX idx_hearing_presided_hearing_id ON hearing_presided_by(hearing_id);
CREATE INDEX idx_hearing_presided_court_id ON hearing_presided_by(court_id);
CREATE INDEX idx_hearing_presided_judge_id ON hearing_presided_by(judge_id);

CREATE TABLE hearing_filing_numbers (
    id serial PRIMARY KEY,
    tenant_id varchar(36) NOT NULL,
    hearing_id uuid NOT NULL,
    filing_number varchar(64) NOT NULL,
    created_by varchar(64) NOT  NULL, -- Audit details
    last_modified_by varchar(64) NOT NULL,
    created_time int8 NOT NULL,
    last_modified_time int8 NOT NULL,
    FOREIGN KEY (hearing_id) REFERENCES dristi_hearing(id)
);

-- Foreign key index
CREATE INDEX idx_hearing_filing_numbers_hearing_id ON hearing_filing_numbers(hearing_id);

-- Index on filing_number for quick searches
CREATE INDEX idx_hearing_filing_numbers_filing_number ON hearing_filing_numbers(filing_number);

CREATE TABLE hearing_cnr_numbers (
    id serial PRIMARY KEY,
    hearing_id uuid NOT NULL,
    cnr_number varchar(64) NOT NULL,
    created_by varchar(36) NOT NULL, -- Audit details
    last_modified_by varchar(36) NOT NULL,
    created_time int8 NOT NULL,
    last_modified_time int8 NOT NULL,
    FOREIGN KEY (hearing_id) REFERENCES dristi_hearing(id)
);

-- Foreign key index
CREATE INDEX idx_hearing_cnr_numbers_hearing_id ON hearing_cnr_numbers(hearing_id);

-- Index on cnr_number for quick searches
CREATE INDEX idx_hearing_cnr_numbers_cnr_number ON hearing_cnr_numbers(cnr_number);

CREATE TABLE hearing_application_numbers (
    id serial PRIMARY KEY,
    hearing_id uuid NOT NULL,
    application_number varchar(64) NOT NULL,
    created_by varchar(64) NOT NULL, -- Audit details
    last_modified_by varchar(64) NOT NULL,
    created_time int8 NOT NULL,
    last_modified_time int8 NOT NULL,
    FOREIGN KEY (hearing_id) REFERENCES dristi_hearing(id)
);

-- Foreign key index
CREATE INDEX idx_hearing_application_numbers_hearing_id ON hearing_application_numbers(hearing_id);

-- Index on application_number for quick searches
CREATE INDEX idx_hearing_application_numbers_application_number ON hearing_application_numbers(application_number);

CREATE TABLE hearing_attendees (
    id serial PRIMARY KEY,
    hearing_id uuid NOT NULL,
    attendee_individual_id varchar(64),
    attendee_name varchar(64),
    attendee_type varchar(64), -- Have masters been defined for this? Store master ID below
    associated_with varchar(64), -- Check this field
    is_online boolean,
    present boolean,
    is_active boolean DEFAULT TRUE,
    created_by varchar(64) NULL, -- Audit details
    last_modified_by varchar(64) NULL,
    created_time int8 NULL,
    last_modified_time int8 NULL,
    FOREIGN KEY (hearing_id) REFERENCES dristi_hearing(id)
);

-- Foreign key index
CREATE INDEX idx_hearing_attendees_hearing_id ON hearing_attendees(hearing_id);

-- Index on attendee for quick searches
CREATE INDEX idx_hearing_attendees_attendee_individual_id ON hearing_attendees(attendee_individual_id);
CREATE INDEX idx_hearing_attendees_attendee_name ON hearing_attendees(attendee_name);
CREATE INDEX idx_hearing_attendees_type ON hearing_attendees(attendee_type);
CREATE INDEX idx_hearing_attendees_is_online ON hearing_attendees(is_online);
CREATE INDEX idx_hearing_attendees_present ON hearing_attendees(present);

CREATE TABLE dristi_hearing_documents (
    id uuid NOT NULL PRIMARY KEY,
    filestore_id varchar(64),
    document_uid varchar(64),
    document_type varchar(64),
    hearing_id uuid NOT NULL,
    additional_details jsonb,
    is_active boolean DEFAULT TRUE,
    created_by varchar(64) NULL, -- Audit details
    last_modified_by varchar(64) NULL,
    created_time int8 NULL,
    last_modified_time int8 NULL,
    FOREIGN KEY(hearing_id) REFERENCES dristi_hearing(id)
);

CREATE INDEX idx_hearing_documents_hearing_id ON dristi_hearing_documents(hearing_id);
CREATE INDEX idx_hearing_documents_filestore_id ON dristi_hearing_documents(filestore_id);
