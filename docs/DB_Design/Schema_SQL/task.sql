CREATE TABLE dristi_task (
    id uuid PRIMARY KEY,
    tenant_id varchar(64) NOT NULL,
    order_id varchar(36) NULL,
    filing_number varchar(64),
    cnr_number varchar(64) NULL,
    task_number varchar(64) NULL,
    created_date int8,
    date_close_by int8,
    date_closed int8,
    task_description varchar(512) NULL,
    task_type_id varchar(64) NULL,
    task_details jsonb NULL,
    wf_status varchar(64) NULL,
    assigned_to varchar(64) NULL,
    is_active bool DEFAULT TRUE,
    additional_details jsonb,
    created_by varchar(36) NOT NULL,
    last_modified_by varchar(36) NOT NULL,
    created_time int8 NOT NULL,
    last_modified_time int8 NOT NULL
);

-- Indexes
CREATE INDEX idx_dristi_task_tenant_id ON dristi_task(tenant_id);
CREATE INDEX idx_dristi_task_order_id ON dristi_task(order_id);
CREATE INDEX idx_dristi_task_filing_number ON dristi_task(filing_number);
CREATE INDEX idx_dristi_task_cnr_number ON dristi_task(cnr_number);
CREATE INDEX idx_dristi_task_task_number ON dristi_task(task_number);
CREATE INDEX idx_dristi_task_created_date ON dristi_task(created_date);
CREATE INDEX idx_dristi_task_date_close_by ON dristi_task(date_close_by);
CREATE INDEX idx_dristi_task_date_closed ON dristi_task(date_closed);
CREATE INDEX idx_dristi_task_task_type_id ON dristi_task(task_type_id);
CREATE INDEX idx_dristi_task_wf_status ON dristi_task(wf_status);
CREATE INDEX idx_dristi_task_assigned_to ON dristi_task(assigned_to);
CREATE INDEX idx_dristi_task_is_active ON dristi_task(is_active);

CREATE TABLE dristi_task_document (
    id uuid NOT NULL PRIMARY KEY,
    filestore_id varchar(64) NULL,
    document_uid varchar(64) NULL,
    document_type varchar(64) NULL,
    task_id uuid NULL,
    additional_details jsonb NULL,
    is_active bool DEFAULT TRUE,
    created_by varchar(36) NOT NULL,
    last_modified_by varchar(36) NOT NULL,
    created_time int8 NOT NULL,
    last_modified_time int8 NOT NULL,
    FOREIGN KEY (task_id) REFERENCES dristi_task(id)
);

-- Indexes
CREATE INDEX idx_dristi_task_document_task_id ON dristi_task_document(task_id);
CREATE INDEX idx_dristi_task_document_document_uid ON dristi_task_document(document_uid);
CREATE INDEX idx_dristi_task_document_document_type ON dristi_task_document(document_type);
CREATE INDEX idx_dristi_task_document_is_active ON dristi_task_document(is_active);

CREATE TABLE dristi_task_amount (
    id uuid NOT NULL PRIMARY KEY,
    amount float NULL,
    type varchar(64) NULL,
    payment_ref_number varchar(64) NULL,
    task_id uuid NULL,
    wf_status varchar(64) NULL,
    is_active bool DEFAULT TRUE,
    additional_details jsonb NULL,
    created_by varchar(36) NOT NULL,
    last_modified_by varchar(36) NOT NULL,
    created_time int8 NOT NULL,
    last_modified_time int8 NOT NULL,
    FOREIGN KEY (task_id) REFERENCES dristi_task(id)
);

-- Indexes
CREATE INDEX idx_dristi_task_amount_task_id ON dristi_task_amount(task_id);
CREATE INDEX idx_dristi_task_amount_type ON dristi_task_amount(type);
CREATE INDEX idx_dristi_task_amount_payment_ref_number ON dristi_task_amount(payment_ref_number);
CREATE INDEX idx_dristi_task_amount_wf_status ON dristi_task_amount(wf_status);
CREATE INDEX idx_dristi_task_amount_is_active ON dristi_task_amount(is_active);
