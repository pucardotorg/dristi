DROP TABLE IF EXISTS dristi_evidence_document;
DROP TABLE IF EXISTS dristi_evidence_comment;

ALTER TABLE dristi_evidence_artifact
ADD COLUMN comments JSONB,
ADD COLUMN file JSONB;

ALTER TABLE dristi_evidence_artifact
DROP COLUMN IF EXISTS applicableTo;

ALTER TABLE dristi_evidence_artifact
ADD COLUMN applicableTo JSONB;