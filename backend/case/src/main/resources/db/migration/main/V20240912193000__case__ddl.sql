CREATE INDEX IF NOT EXISTS idx_dristi_cases_dristi_cases_tenant_id ON dristi_cases(id, tenantId);
CREATE INDEX IF NOT EXISTS idx_dristi_cases_cnr_number ON dristi_cases(cnrNumber);
CREATE INDEX IF NOT EXISTS idx_dristi_cases_filing_number ON dristi_cases(filingNumber);
CREATE INDEX IF NOT EXISTS idx_dristi_cases_court_case_number ON dristi_cases(courtCaseNumber);
CREATE INDEX IF NOT EXISTS idx_dristi_cases_filing_date ON dristi_cases(filingDate);
CREATE INDEX IF NOT EXISTS idx_dristi_cases_registration_date ON dristi_cases(registrationDate);
CREATE INDEX IF NOT EXISTS idx_dristi_cases_judge_id ON dristi_cases(judgeId);
CREATE INDEX IF NOT EXISTS idx_dristi_cases_substage ON dristi_cases(substage);

CREATE INDEX IF NOT EXISTS idx_dristi_case_documents_case_id ON dristi_case_document(case_id);
CREATE INDEX IF NOT EXISTS idx_dristi_case_documents_filestore_id ON dristi_case_document(fileStore);
CREATE INDEX IF NOT EXISTS idx_dristi_case_document_type ON dristi_case_document(documentType);

CREATE INDEX IF NOT EXISTS idx_dristi_linked_case_case_id ON dristi_linked_case(case_id);

CREATE INDEX IF NOT EXISTS idx_dristi_case_statutes_and_sections_case_id ON dristi_case_statutes_and_sections(case_id);
CREATE INDEX IF NOT EXISTS idx_dristi_case_statutes_and_sections_statutes ON dristi_case_statutes_and_sections(statutes);

CREATE INDEX IF NOT EXISTS idx_dristi_case_litigants_case_id ON dristi_case_litigants(case_id);
CREATE INDEX IF NOT EXISTS idx_dristi_case_litigants_individual_id ON dristi_case_litigants(individualId);


CREATE INDEX IF NOT EXISTS idx_dristi_case_representatives_case_id ON dristi_case_representatives(case_id);
CREATE INDEX IF NOT EXISTS idx_dristi_case_representatives_advocate_id ON dristi_case_representatives (advocateId);

CREATE INDEX IF NOT EXISTS idx_dristi_case_representing_representative_id ON dristi_case_representing(representative_id);
