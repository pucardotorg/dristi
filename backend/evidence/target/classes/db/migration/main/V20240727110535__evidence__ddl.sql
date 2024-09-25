ALTER TABLE dristi_evidence_artifact
DROP COLUMN createdDate;

ALTER TABLE dristi_evidence_artifact
ADD COLUMN createdDate int8 NULL;
