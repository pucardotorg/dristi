CREATE INDEX IF NOT EXISTS idx_dristi_evidence_artifact_artifact_number ON dristi_evidence_artifact(artifactNumber);
CREATE INDEX IF NOT EXISTS idx_dristi_evidence_artifact_case_id ON dristi_evidence_artifact(caseId);
CREATE INDEX IF NOT EXISTS idx_dristi_evidence_artifact_application ON dristi_evidence_artifact(application);
CREATE INDEX IF NOT EXISTS idx_dristi_evidence_artifact_hearing ON dristi_evidence_artifact(hearing);
CREATE INDEX IF NOT EXISTS idx_dristi_evidence_artifact_orders ON dristi_evidence_artifact(orders);
CREATE INDEX IF NOT EXISTS idx_dristi_evidence_artifact_artifact_type ON dristi_evidence_artifact(artifactType);
CREATE INDEX IF NOT EXISTS idx_dristi_evidence_status ON dristi_evidence_artifact(status);
CREATE INDEX IF NOT EXISTS idx_dristi_evidence_owner ON dristi_evidence_artifact(createdBy);