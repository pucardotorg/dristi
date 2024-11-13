CREATE INDEX IF NOT EXISTS idx_dristi_hearing_hearing_id ON dristi_hearing (hearingId);
CREATE INDEX IF NOT EXISTS idx_dristi_hearing_filing_number ON dristi_hearing USING GIN (filingNumber);
CREATE INDEX IF NOT EXISTS idx_dristi_hearing_cnr_number ON dristi_hearing USING GIN (cnrNumbers);
CREATE INDEX IF NOT EXISTS idx_dristi_hearing_application_number ON dristi_hearing USING GIN (applicationNumbers);
CREATE INDEX IF NOT EXISTS idx_dristi_hearing_hearing_type ON dristi_hearing (hearingType);
CREATE INDEX IF NOT EXISTS idx_dristi_hearing_individual_id ON dristi_hearing USING gin (attendees jsonb_path_ops);
CREATE INDEX IF NOT EXISTS idx_drist_hearing_start_time ON dristi_hearing (startTime);

CREATE INDEX IF NOT EXISTS idx_dristi_hearing_document_hearing_id ON dristi_hearing_document (hearingId);
CREATE INDEX IF NOT EXISTS idx_dristi_hearing_document_filestore_id ON dristi_hearing_document (fileStore);