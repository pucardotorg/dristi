CREATE TABLE dristi_orders (
    id varchar(36) NOT NULL PRIMARY KEY,
    tenantId varchar(64) NOT NULL,
    hearingNumber varchar(64) NULL,
    orderNumber varchar(64) NOT NULL,
    -- Formatted order number to which this is linked
    linkedOrderNumber varchar(64) NULL,
    filingNumber varchar(64) NULL,
    cnrNumber varchar(64) NULL,
    -- Order type and category Master IDs
    order_type_id varchar(64),
    order_category_id varchar(64),
    -- Schema per order type defined in MDMS
    orderDetails JSONB,
    createdDate int8 NULL,
    -- Statute and section (master IDs) under which this order was filed
    statute_id varchar(36),
    section_id varchar(36),
    -- Workflow status stored here for convenience
    wfStatus varchar(64) NULL,
    -- Soft delete flag
    isActive bool DEFAULT TRUE,
    -- Extra field for implementation
    additionalDetails JSONB,
    -- Audit details
    createdBy varchar(36) NOT NULL,
    lastModifiedBy varchar(36) NOT NULL,
    createdTime int8 NOT NULL,
    lastModifiedTime int8 NOT NULL
);

-- Indexes
CREATE INDEX idx_dristi_orders_tenantId ON dristi_orders(tenantId);
CREATE INDEX idx_dristi_orders_hearingNumber ON dristi_orders(hearingNumber);
CREATE INDEX idx_dristi_orders_orderNumber ON dristi_orders(orderNumber);
CREATE INDEX idx_dristi_orders_filingNumber ON dristi_orders(filingNumber);
CREATE INDEX idx_dristi_orders_cnr ON dristi_orders(cnrNumber);
CREATE INDEX idx_dristi_orders_order_type ON dristi_orders(order_type_id);
CREATE INDEX idx_dristi_orders_order_category ON dristi_orders(order_category_id);
CREATE INDEX idx_dristi_orders_status ON dristi_orders(wfStatus);

CREATE TABLE dristi_order_application_numbers (
    id VARCHAR(36) PRIMARY KEY,
    tenantId NOT NULL,
    order_id varchar(36) NOT NULL,
    application_number varchar(64) NOT NULL,
    createdBy varchar(36) NOT NULL,
    lastModifiedBy varchar(36) NOT NULL,
    createdTime int8 NOT NULL,
    lastModifiedTime int8 NOT NULL
    FOREIGN KEY (order_id) REFERENCES dristi_orders(id)
);

-- Indexes
CREATE INDEX idx_dristi_order_application_numbers_order_id ON dristi_order_application_numbers(order_id);
CREATE INDEX idx_dristi_order_application_numbers_app_number ON dristi_order_application_numbers(application_number);


CREATE TABLE dristi_order_issued_by (
    id VARCHAR(36) PRIMARY KEY,
    tenantId VARCHAR(36) NOT NULL,
    order_id varchar(36) NOT NULL,
    court_id varchar(36) NOT NULL,
    judge_id varchar(36),
    bench_id varchar(36),
    createdBy varchar(36) NULL,
    lastModifiedBy varchar(36) NULL,
    createdTime int8 NULL,
    lastModifiedTime int8 NULL,
    FOREIGN KEY (order_id) REFERENCES dristi_orders(id)
);

-- Indexes
CREATE INDEX idx_dristi_order_issued_by_order_id ON dristi_order_issued_by(order_id);
CREATE INDEX idx_dristi_order_court ON dristi_order_issued_by(court_id);
CREATE INDEX idx_dristi_order_bench ON dristi_order_issued_by(bench_id);
CREATE INDEX idx_dristi_order_judge ON dristi_order_issued_by(judge_id);


CREATE TABLE dristi_order_document (
    id varchar(36) PRIMARY KEY,
    tenantId VARCHAR,
    fileStore varchar(64) NULL,
    documentUid varchar(64) NULL,
    documentType varchar(64) NULL,
    order_id varchar(36) NOT NULL,
    createdBy varchar(36) NULL,
    lastModifiedBy varchar(36) NULL,
    createdTime int8 NULL,
    lastModifiedTime int8 NULL,
    FOREIGN KEY (order_id) REFERENCES dristi_orders(id)
);

-- Indexes
CREATE INDEX idx_dristi_order_document_order_id ON dristi_order_document(order_id);
CREATE INDEX idx_dristi_order_document_docUid ON dristi_order_document(documentUid);

CREATE TABLE dristi_order_statute_section (
    id varchar(36) NOT NULL PRIMARY KEY,
    tenantId varchar(64) NOT NULL,
    order_id varchar(36) NOT NULL,
    statute_id varchar(64) NULL,
    section_id varchar(64) NULL,
    createdBy varchar(36) NULL,
    lastModifiedBy varchar(36) NULL,
    createdTime int8 NULL,
    lastModifiedTime int8 NULL,
    FOREIGN KEY (order_id) REFERENCES dristi_orders(id)
);

-- Indexes
CREATE INDEX idx_dristi_order_statute_section_order_id ON dristi_order_statute_section(order_id);
CREATE INDEX idx_dristi_order_statute_section_statute_id ON dristi_order_statute_section(statute_id);
CREATE INDEX idx_dristi_order_statute_section_section_id ON dristi_order_statute_section(section_id);
CREATE INDEX idx_dristi_order_statute_section_tenantId ON dristi_order_statute_section(tenantId);
