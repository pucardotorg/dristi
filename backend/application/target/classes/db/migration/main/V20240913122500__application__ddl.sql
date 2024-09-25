CREATE INDEX IF NOT EXISTS idx_dristi_application_filing_number ON dristi_application(filingNumber);
CREATE INDEX IF NOT EXISTS idx_dristi_application_cnr_number ON dristi_application(cnrNumber);
CREATE INDEX IF NOT EXISTS idx_dristi_application_application_type ON dristi_application(applicationType);
CREATE INDEX IF NOT EXISTS idx_drist_application_application_number ON dristi_application(applicationNumber);
CREATE INDEX IF NOT EXISTS idx_dristi_application_status ON dristi_application(status);

CREATE INDEX IF NOT EXISTS idx_dristi_application_document_application_id ON dristi_application_document(application_id);
