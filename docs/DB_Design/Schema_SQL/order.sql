CREATE TABLE dristi_orders (
    id uuid NOT NULL PRIMARY KEY,
    tenant_id varchar(64) NOT NULL,
    order_number varchar(64) NOT NULL, -- Formatted order number
    linked_order_number varchar(64) NULL, -- Formatted order number to which this is linked
    application_number varchar(64), -- Linkages to cases, application and/or hearings
    hearing_number varchar(64),
    filing_number varchar(64),
    cnr_number varchar(64),
    order_type_id varchar(64), -- Order type and category Master IDs
    order_category_id varchar(64),
    order_details jsonb, -- Schema per order type defined in MDMS
    created_date int8,
    statute_id varchar(36), -- Statute and section (master IDs) under which this order was filed
    section_id varchar(36),
    wf_status varchar(64) NULL, -- Workflow status stored here for convenience
    is_active bool DEFAULT TRUE, -- Soft delete flag
    additional_details jsonb, -- Extra field for implementation
    created_by varchar(36) NOT NULL, -- Audit details
    last_modified_by varchar(36) NOT NULL,
    created_time int8 NOT NULL,
    last_modified_time int8 NOT NULL
);

-- Indexes
CREATE INDEX idx_dristi_orders_tenant_id ON dristi_orders(tenant_id);
CREATE INDEX idx_dristi_orders_hearing_number ON dristi_orders(hearing_number);
CREATE INDEX idx_dristi_orders_order_number ON dristi_orders(order_number);
CREATE INDEX idx_dristi_orders_filing_number ON dristi_orders(filing_number);
CREATE INDEX idx_dristi_orders_cnr_number ON dristi_orders(cnr_number);
CREATE INDEX idx_dristi_orders_order_type_id ON dristi_orders(order_type_id);
CREATE INDEX idx_dristi_orders_order_category_id ON dristi_orders(order_category_id);
CREATE INDEX idx_dristi_orders_wf_status ON dristi_orders(wf_status);

CREATE TABLE dristi_order_application_numbers (
    id varchar(36) PRIMARY KEY,
    tenant_id varchar(64) NOT NULL,
    order_id uuid NOT NULL,
    application_number varchar(64) NOT NULL,
    created_by varchar(36) NOT NULL,
    last_modified_by varchar(36) NOT NULL,
    created_time int8 NOT NULL,
    last_modified_time int8 NOT NULL,
    FOREIGN KEY (order_id) REFERENCES dristi_orders(id)
);

-- Indexes
CREATE INDEX idx_dristi_order_application_numbers_order_id ON dristi_order_application_numbers(order_id);
CREATE INDEX idx_dristi_order_application_numbers_app_number ON dristi_order_application_numbers(application_number);

CREATE TABLE dristi_order_issued_by (
    id varchar(36) PRIMARY KEY,
    tenant_id varchar(64) NOT NULL,
    order_id uuid NOT NULL,
    court_id varchar(36) NOT NULL,
    judge_id varchar(36),
    bench_id varchar(36),
    created_by varchar(36) NULL,
    last_modified_by varchar(36) NULL,
    created_time int8 NULL,
    last_modified_time int8 NULL,
    FOREIGN KEY (order_id) REFERENCES dristi_orders(id)
);

-- Indexes
CREATE INDEX idx_dristi_order_issued_by_order_id ON dristi_order_issued_by(order_id);
CREATE INDEX idx_dristi_order_issued_by_court_id ON dristi_order_issued_by(court_id);
CREATE INDEX idx_dristi_order_issued_by_bench_id ON dristi_order_issued_by(bench_id);
CREATE INDEX idx_dristi_order_issued_by_judge_id ON dristi_order_issued_by(judge_id);

CREATE TABLE dristi_order_document (
    id uuid PRIMARY KEY,
    tenant_id varchar(64),
    filestore_id varchar(64) NULL,
    document_uid varchar(64) NULL,
    document_type varchar(64) NULL,
    order_id uuid NOT NULL,
    created_by varchar(36) NULL,
    last_modified_by varchar(36) NULL,
    created_time int8 NULL,
    last_modified_time int8 NULL,
    FOREIGN KEY (order_id) REFERENCES dristi_orders(id)
);

-- Indexes
CREATE INDEX idx_dristi_order_document_order_id ON dristi_order_document(order_id);
CREATE INDEX idx_dristi_order_document_doc_uid ON dristi_order_document(document_uid);

CREATE TABLE dristi_order_statute_section (
    id uuid NOT NULL PRIMARY KEY,
    tenant_id varchar(64) NOT NULL,
    order_id uuid NOT NULL,
    statute_id varchar(64) NULL,
    section_id varchar(64) NULL,
    created_by varchar(36) NULL,
    last_modified_by varchar(36) NULL,
    created_time int8 NULL,
    last_modified_time int8 NULL,
    FOREIGN KEY (order_id) REFERENCES dristi_orders(id)
);

-- Indexes
CREATE INDEX idx_dristi_order_statute_section_order_id ON dristi_order_statute_section(order_id);
CREATE INDEX idx_dristi_order_statute_section_statute_id ON dristi_order_statute_section(statute_id);
CREATE INDEX idx_dristi_order_statute_section_section_id ON dristi_order_statute_section(section_id);
CREATE INDEX idx_dristi_order_statute_section_tenant_id ON dristi_order_statute_section(tenant_id);
