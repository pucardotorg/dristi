CREATE TABLE dristi_hearing (
    id VARCHAR(64) NOT NULL PRIMARY KEY,
    tenantId VARCHAR(10) NOT NULL,
    hearingId varchar(64) NULL,
    filingNumber JSONB NULL,
    cnrNumbers JSONB NULL,
    applicationNumbers JSONB NULL,
    hearingType VARCHAR(64) NOT NULL,
    status VARCHAR(64) NOT NULL,
    startTime int8 NULL,
    endTime int8 NULL,
    presidedBy JSONB NULL,
    attendees JSONB NULL,
    transcript JSONB NULL,
    vcLink VARCHAR(255) NULL,
    isActive BOOLEAN NULL,
    additionalDetails JSONB NULL,
    notes VARCHAR(255) NULL,
    createdBy varchar(64) NULL,
    lastModifiedBy varchar(64) NULL,
    createdTime int8 NULL,
    lastModifiedTime int8 NULL
    CONSTRAINT chk_startTime_endTime CHECK (startTime IS NULL OR endTime IS NULL OR startTime <= endTime)
);

CREATE TABLE dristi_hearing_document (
    id VARCHAR(64) NOT NULL PRIMARY KEY,
    fileStore VARCHAR(64),
    documentUid VARCHAR(64),
    documentType VARCHAR(64),
    hearingId VARCHAR(64) NOT NULL,
    additionalDetails JSONB
);
