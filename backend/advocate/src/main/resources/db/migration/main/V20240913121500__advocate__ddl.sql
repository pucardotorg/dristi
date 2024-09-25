CREATE INDEX IF NOT EXISTS idx_dristi_advocate_application_number ON dristi_advocate(applicationNumber);
CREATE INDEX IF NOT EXISTS idx_dristi_advocate_bar_registration_number ON dristi_advocate(barRegistrationNumber);
CREATE INDEX IF NOT EXISTS idx_dristi_advocate_individual_id ON dristi_advocate(individualId);

CREATE INDEX IF NOT EXISTS idx_dristi_advocate_clerk_application_number ON dristi_advocate_clerk(applicationNumber);
CREATE INDEX IF NOT EXISTS idx_dristi_advocate_clerk_individual_id ON dristi_advocate_clerk(individualId);
CREATE INDEX IF NOT EXISTS idx_dristi_advocate_clerk_state_regn_number ON dristi_advocate_clerk(stateRegnNumber);

CREATE INDEX IF NOT EXISTS idx_dristi_document_advocate_id ON dristi_document(advocateId);
CREATE INDEX IF NOT EXISTS idx_dristi_document_clerk_id ON dristi_document(clerk_id);