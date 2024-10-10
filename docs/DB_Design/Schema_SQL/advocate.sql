CREATE TABLE dristi_advocate_clerk (
    id uuid,
    tenant_id varchar(64) NOT NULL,
    application_number varchar(64),
    wf_status varchar(64),
    individual_id varchar(36),
    is_active boolean DEFAULT true,
    state_regn_number varchar(64) NOT NULL,
    -- Audit details
    created_by varchar(36) NOT NULL,
    last_modified_by varchar(36) NOT NULL,
    created_time int8 NOT NULL,
    last_modified_time int8 NOT NULL,
    -- To be used in implementations
    additional_details jsonb,
    CONSTRAINT pk_dristi_advocate_clerk PRIMARY KEY (id)
);

CREATE INDEX idx_dristi_advocate_clerk_tenant_application ON dristi_advocate_clerk(tenant_id, application_number);
CREATE INDEX idx_dristi_advocate_clerk_status ON dristi_advocate_clerk(tenant_id, wf_status);
CREATE INDEX idx_dristi_advocate_clerk_individual_id ON dristi_advocate_clerk(individual_id);

CREATE TABLE dristi_advocate (
    id uuid NOT NULL PRIMARY KEY,
    tenant_id varchar(64) NOT NULL,
    application_number varchar(64) NOT NULL,
    wf_status varchar(64),
    bar_registration_number varchar(64),
    advocate_type varchar(64),
    organisation_id varchar(36),
    individual_id varchar(36) NOT NULL,
    is_active bool DEFAULT true,
    additional_details jsonb,
    -- Audit details
    created_by varchar(36) NOT NULL,
    last_modified_by varchar(36) NOT NULL,
    created_time int8 NOT NULL,
    last_modified_time int8 NOT NULL
);

CREATE INDEX idx_dristi_advocate_tenant_application ON dristi_advocate(tenant_id, application_number);
CREATE INDEX idx_dristi_advocate_status ON dristi_advocate(tenant_id, wf_status);
CREATE INDEX idx_dristi_advocate_individual_id ON dristi_advocate(individual_id);
CREATE INDEX idx_dristi_advocate_bar_registration_number ON dristi_advocate(bar_registration_number);

CREATE TABLE dristi_advocate_document (
    id uuid NOT NULL PRIMARY KEY,
    filestore_id varchar(36) NOT NULL,
    document_uid varchar(36),
    document_type varchar(64),
    advocate_id uuid NOT NULL,
    is_active bool DEFAULT true,
    additional_details jsonb,
    -- Audit details
    created_by varchar(36) NOT NULL,
    last_modified_by varchar(36) NOT NULL,
    created_time int8 NOT NULL,
    last_modified_time int8 NOT NULL,
    CONSTRAINT fk_dristi_advocate_document_advocate
        FOREIGN KEY(advocate_id) 
        REFERENCES dristi_advocate(id)
);

CREATE INDEX idx_dristi_advocate_document_filestore_id ON dristi_advocate_document(filestore_id);
CREATE INDEX idx_dristi_advocate_document_advocate_id ON dristi_advocate_document(advocate_id);

CREATE TABLE dristi_advocate_clerk_document (
    id uuid NOT NULL PRIMARY KEY,
    filestore_id varchar(36) NOT NULL,
    document_uid varchar(36),
    document_type varchar(64),
    clerk_id uuid NOT NULL,
    is_active bool DEFAULT true,
    additional_details jsonb,

    -- Audit details
    created_by varchar(36) NOT NULL,
    last_modified_by varchar(36) NOT NULL,
    created_time int8 NOT NULL,
    last_modified_time int8 NOT NULL,
    CONSTRAINT fk_dristi_advocate_clerk_document_clerk
        FOREIGN KEY(clerk_id) 
        REFERENCES dristi_advocate_clerk(id)
);

CREATE INDEX idx_dristi_advocate_clerk_document_filestore_id ON dristi_advocate_clerk_document(filestore_id);
CREATE INDEX idx_dristi_advocate_clerk_document_clerk_id ON dristi_advocate_clerk_document(clerk_id);
