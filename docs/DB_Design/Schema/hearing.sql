CREATE TABLE dristi_hearing (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    tenantId VARCHAR(64) NOT NULL,
    -- Formatted ID
    hearingId VARCHAR(64),
    -- Master data ID for hearing type
    hearingType VARCHAR(64) NOT NULL,
    wfStatus VARCHAR(64) NOT NULL,
    -- Epoch time
    startTime int8,
    endTime int8,
    vcLink VARCHAR(255),
    isActive BOOLEAN DEFAULT TRUE,
    notes VARCHAR(255) NULL,
    -- Audit details
    createdBy VARCHAR(64) NULL,
    lastModifiedBy VARCHAR(64) NULL,
    createdTime int8 NULL,
    lastModifiedTime int8 NULL,
    CONSTRAINT chk_startTime_endTime CHECK (startTime IS NULL OR endTime IS NULL OR startTime <= endTime)
);

-- Indexes on frequently queried columns
CREATE INDEX idx_dristi_hearing_tenantId ON dristi_hearing(tenantId);
CREATE INDEX idx_dristi_hearing_hearingId ON dristi_hearing(hearingId);
CREATE INDEX idx_dristi_hearing_hearingType ON dristi_hearing(hearingType);
CREATE INDEX idx_dristi_hearing_status ON dristi_hearing(wfStatus);

CREATE TABLE hearing_filing_numbers (
    id SERIAL PRIMARY KEY,
    hearing_id VARCHAR(36) NOT NULL,
    filing_number VARCHAR(64) NOT NULL,
    FOREIGN KEY (hearing_id) REFERENCES dristi_hearing(id)
);

-- Foreign key index
CREATE INDEX idx_hearing_filing_numbers_hearing_id ON hearing_filing_numbers(hearing_id);

-- Index on filing_number for quick searches
CREATE INDEX idx_hearing_filing_numbers_filing_number ON hearing_filing_numbers(filing_number);

CREATE TABLE hearing_cnr_numbers (
    id SERIAL PRIMARY KEY,
    hearing_id VARCHAR(36) NOT NULL,
    cnr_number VARCHAR(64) NOT NULL,
    FOREIGN KEY (hearing_id) REFERENCES dristi_hearing(id)
);

-- Foreign key index
CREATE INDEX idx_hearing_cnr_numbers_hearing_id ON hearing_cnr_numbers(hearing_id);

-- Index on cnr_number for quick searches
CREATE INDEX idx_hearing_cnr_numbers_cnr_number ON hearing_cnr_numbers(cnr_number);

CREATE TABLE hearing_application_numbers (
    id SERIAL PRIMARY KEY,
    hearing_id VARCHAR(36) NOT NULL,
    application_number VARCHAR(64) NOT NULL,
    FOREIGN KEY (hearing_id) REFERENCES dristi_hearing(id)
);

-- Foreign key index
CREATE INDEX idx_hearing_application_numbers_hearing_id ON hearing_application_numbers(hearing_id);

-- Index on application_number for quick searches
CREATE INDEX idx_hearing_application_numbers_application_number ON hearing_application_numbers(application_number);


CREATE TABLE hearing_presided_by (
    id SERIAL PRIMARY KEY,
    hearing_id VARCHAR(36) NOT NULL,
    presided_by_judge_id VARCHAR(64) NOT NULL,
    FOREIGN KEY (hearing_id) REFERENCES dristi_hearing(id)
);

-- Foreign key index
CREATE INDEX idx_hearing_presided_by_hearing_id ON hearing_presided_by(hearing_id);

-- Index on presided_by for quick searches
CREATE INDEX idx_hearing_presided_by_presided_by ON hearing_presided_by(presided_by);

CREATE TABLE hearing_attendees (
    id SERIAL PRIMARY KEY,
    hearing_id VARCHAR(36) NOT NULL,
    attendee_individual_id VARCHAR(64) NOT NULL,
    FOREIGN KEY (hearing_id) REFERENCES dristi_hearing(id)
);

-- Foreign key index
CREATE INDEX idx_hearing_attendees_hearing_id ON hearing_attendees(hearing_id);

-- Index on attendee for quick searches
CREATE INDEX idx_hearing_attendees_attendee ON hearing_attendees(attendee_individual_id);


CREATE TABLE hearing_transcripts (
    id SERIAL PRIMARY KEY,
    hearing_id VARCHAR(36) NOT NULL,
    transcript_text TEXT NOT NULL,
    FOREIGN KEY (hearing_id) REFERENCES dristi_hearing(id)
);

-- Foreign key index
CREATE INDEX idx_hearing_transcripts_hearing_id ON hearing_transcripts(hearing_id);


CREATE TABLE dristi_hearing_documents (
    id VARCHAR(64) NOT NULL PRIMARY KEY,
    fileStore_id VARCHAR(64),
    documentUid VARCHAR(64),
    documentType VARCHAR(64),
    hearingId VARCHAR(36) NOT NULL,
    additionalDetails JSONB
);

CREATE INDEX idx_hearing_documents_hearing_id ON dristi_hearing(hearingId);
CREATE INDEX idx_hearing_docs_filestore_id ON dristi_hearing(fileStore_id);