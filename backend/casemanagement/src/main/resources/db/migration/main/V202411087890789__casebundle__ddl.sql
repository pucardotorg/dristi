
CREATE TABLE IF NOT EXISTS case_bundle_tracker(
  id character varying(64) NOT NULL PRIMARY KEY,
  startTime bigint NOT NULL,
  endTime bigint NOT NULL,
  pageCount bigint NOT NULL,
  errorLog   character varying(64),
  createdBy character varying(64) NOT NULL,
  lastModifiedBy character varying(64) NOT NULL,
  createdTime bigint NOT NULL,
  lastModifiedTime bigint NOT NULL
);

