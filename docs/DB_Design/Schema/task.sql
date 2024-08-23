CREATE TABLE dristi_task (
    id varchar(36) PRIMARY KEY,
    tenantId varchar(64) NOT NULL,
    orderId varchar(36) NULL,
    filingNumber VARCHAR(64),
    cnrNumber varchar(64) NULL,
    taskNumber varchar(64) NULL,
    createdDate int8,
    dateCloseBy int8,
    dateClosed int8,
    taskDescription varchar(512) NULL,
    task_type_id varchar(64) NULL,
    taskDetails jsonb NULL,
    wfStatus varchar(64) NULL,
    assignedTo varchar(64) NULL,
    isActive bool DEFAULT TRUE,
    additionalDetails jsonb,
    createdBy varchar(36) NOT NULL,
    lastModifiedBy varchar(36) NOT NULL,
    createdTime int8 NOT NULL,
    lastModifiedTime int8 NOT NULL
);

-- Indexes
CREATE INDEX idx_dristi_task_tenantId ON dristi_task(tenantId);
CREATE INDEX idx_dristi_task_orderId ON dristi_task(orderId);
CREATE INDEX idx_dristi_task_filingNumber ON dristi_task(filingNumber);
CREATE INDEX idx_dristi_task_cnrNumber ON dristi_task(cnrNumber);
CREATE INDEX idx_dristi_task_taskNumber ON dristi_task(taskNumber);
CREATE INDEX idx_dristi_task_createdDate ON dristi_task(createdDate);
CREATE INDEX idx_dristi_task_dateCloseBy ON dristi_task(dateCloseBy);
CREATE INDEX idx_dristi_task_dateClosed ON dristi_task(dateClosed);
CREATE INDEX idx_dristi_task_task_type_id ON dristi_task(task_type_id);
CREATE INDEX idx_dristi_task_wfStatus ON dristi_task(wfStatus);
CREATE INDEX idx_dristi_task_assignedTo ON dristi_task(assignedTo);
CREATE INDEX idx_dristi_task_isActive ON dristi_task(isActive);

CREATE TABLE dristi_task_document (
    id varchar(64) NOT NULL PRIMARY KEY,
    fileStore varchar(64) NULL,
    documentUid varchar(64)  NULL,
    documentType varchar(64) NULL,
    task_id varchar(64)  NULL,
    additionalDetails JSONB NULL,
    isActive bool DEFAULT TRUE,
    createdBy varchar(36) NOT NULL,
    lastModifiedBy varchar(36) NOT NULL,
    createdTime int8 NOT NULL,
    lastModifiedTime int8 NOT NULL,
    FOREIGN KEY (task_id) REFERENCES dristi_task(id)
);

-- Indexes
CREATE INDEX idx_dristi_task_document_task_id ON dristi_task_document(task_id);
CREATE INDEX idx_dristi_task_document_documentUid ON dristi_task_document(documentUid);
CREATE INDEX idx_dristi_task_document_documentType ON dristi_task_document(documentType);
CREATE INDEX idx_dristi_task_document_isActive ON dristi_task_document(isActive);

CREATE TABLE dristi_task_amount (
    id varchar(64) NOT NULL PRIMARY KEY,
    amount varchar(64) NULL,
    type varchar(64)  NULL,
    paymentRefNumber varchar(64) NULL,
    task_id varchar(64)  NULL,
    wfStatus varchar(64)  NULL,
    isActive bool DEFAULT TRUE,
    additionalDetails JSONB NULL,
    createdBy varchar(36) NOT NULL,
    lastModifiedBy varchar(36) NOT NULL,
    createdTime int8 NOT NULL,
    lastModifiedTime int8 NOT NULL,
    FOREIGN KEY (task_id) REFERENCES dristi_task(id)
);

-- Indexes
CREATE INDEX idx_dristi_task_amount_task_id ON dristi_task_amount(task_id);
CREATE INDEX idx_dristi_task_amount_type ON dristi_task_amount(type);
CREATE INDEX idx_dristi_task_amount_paymentRefNumber ON dristi_task_amount(paymentRefNumber);
CREATE INDEX idx_dristi_task_amount_status ON dristi_task_amount(wfStatus);
CREATE INDEX idx_dristi_task_amount_isActive ON dristi_task_amount(isActive);
