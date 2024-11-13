CREATE TABLE dristi_epost_tracker (
    process_number varchar(64) NOT NULL PRIMARY KEY,
    tenant_id varchar(64) NOT NULL,
    file_store_id varchar(64) NULL,
    task_number varchar(64) NULL,
    tracking_number varchar(64) NULL,
    pincode varchar(64) NULL,
    address varchar(1000) NULL,
    delivery_status varchar(64) NULL,
    remarks varchar(64) NULL,
    additional_details jsonb NULL,
    row_version int4 NULL,
    booking_date varchar(64) NULL,
    received_date varchar(64) NULL,
    createdBy varchar(64) NULL,
    lastModifiedBy varchar(64) NULL,
    createdTime int8 NULL,
    lastModifiedTime int8 NULL
);