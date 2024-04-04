CREATE TABLE advocate_clerk (
    id VARCHAR(64),
    tenantId VARCHAR(128),
    applicationNumber VARCHAR(64),
    individualId VARCHAR(36),
    isActive BOOLEAN DEFAULT true,
    workflow JSONB,
    documents JSONB,
    auditDetails JSONB,
    additionalDetails JSONB,
    CONSTRAINT pk_advocate_clerk PRIMARY KEY (id)
);

CREATE TABLE document (
    id VARCHAR(64),
    documentType VARCHAR(64),
    fileStore VARCHAR(255),
    documentUid VARCHAR(64),
    additionalDetails JSONB,
    CONSTRAINT pk_document PRIMARY KEY (id)
);
