CREATE TABLE lock (
    id                VARCHAR(64) PRIMARY KEY,
    tenantId          VARCHAR(64),
    lockDate          BIGINT,
    individualId      VARCHAR(64),
    isLocked          BOOLEAN,
    lockReleaseTime   BIGINT,
    uniqueId          VARCHAR(64),
    createdBy         VARCHAR(64) NULL,
    lastModifiedBy    VARCHAR(64) NULL,
    createdTime       int8 NULL,
    lastModifiedTime  int8 NULL
);

CREATE INDEX idx_unique_tenant ON lock (uniqueId, tenantId);
