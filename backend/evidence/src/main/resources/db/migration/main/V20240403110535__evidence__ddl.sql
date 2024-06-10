CREATE TABLE dristi_evidence_artifact (
    id VARCHAR(64) NOT NULL PRIMARY KEY,
    tenantId varchar(1000) NOT NULL,
    artifactNumber VARCHAR(64) NULL,
    evidenceNumber VARCHAR(64) NULL,
    externalRefNumber VARCHAR(128) NULL,
    caseId varchar(64) NULL,
    application VARCHAR(255) NULL,
    hearing VARCHAR(255) NULL,
    orders VARCHAR(255) NULL,
    mediaType VARCHAR(255) NULL,
    artifactType VARCHAR(255) NULL,
    sourceID VARCHAR(255) NULL,
    sourceName VARCHAR(255) NULL,
    applicableTo VARCHAR(255) NULL,
    createdDate int8 NULL,
    isActive bool NULL,
    status VARCHAR(64),
    description VARCHAR(64) NULL,
    artifactDetails jsonb NULL,
    additionalDetails jsonb NULL,
    createdBy varchar(64) NULL,
    lastModifiedBy varchar(64) NULL,
    createdTime int8 NULL,
    lastModifiedTime int8 NULL
);
CREATE TABLE dristi_evidence_document (
    id varchar(64) NOT NULL PRIMARY KEY,
    fileStore varchar(64) NULL,
    documentUid varchar(64)  NULL ,
    documentType varchar(64) NULL,
    artifactId varchar(64)  NULL,
    additionalDetails JSONB NULL
);
CREATE TABLE dristi_evidence_comment (
    id VARCHAR(64) NOT NULL PRIMARY KEY,
    tenantId varchar(1000) NOT NULL,
    artifactId varchar(64) NOT NULL ,
    individualId varchar(64) NOT NULL,
    comment varchar(64) NULL,
    isActive bool NULL,
    additionalDetails jsonb NULL,
    createdBy varchar(64) NULL,
    lastModifiedBy varchar(64) NULL,
    createdTime int8 NULL,
    lastModifiedTime int8 NULL
);

CREATE SEQUENCE SEQ_DRISTI_ARTIFACT
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE SEQ_DOC_COMPLAINANT
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE SEQ_DOC_ACCUSED
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE SEQ_DOC_COURT
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE SEQ_WITNESS_COMPLAINANT
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE SEQ_WITNESS_ACCUSED
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE SEQ_WITNESS_COURT
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;