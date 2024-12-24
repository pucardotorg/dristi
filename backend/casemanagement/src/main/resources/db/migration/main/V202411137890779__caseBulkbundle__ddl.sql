CREATE TABLE IF NOT EXISTS case_bundle_bulk_tracker(
  id character varying(64) NOT NULL PRIMARY KEY,
  startTime bigint NOT NULL,
  endTime bigint NOT NULL,
  caseCount bigint NOT NULL
);

