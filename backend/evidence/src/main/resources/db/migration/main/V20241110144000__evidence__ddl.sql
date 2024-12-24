ALTER TABLE dristi_evidence_artifact
ADD COLUMN isVoid bool NULL;

ALTER TABLE dristi_evidence_artifact
ADD COLUMN reason VARCHAR(255) NULL;

ALTER TABLE dristi_evidence_artifact
ADD COLUMN filingType VARCHAR(255) NULL;