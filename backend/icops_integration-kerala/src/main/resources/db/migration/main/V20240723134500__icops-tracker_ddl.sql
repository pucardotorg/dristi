
CREATE TABLE dristi_kerala_icops (
    process_number varchar(64) NOT NULL PRIMARY KEY,
    tenant_id varchar(64) NOT NULL,
    task_number varchar(64) NULL,
    task_type varchar(64) NULL,
    file_store_id varchar(64) NULL,
    task_details jsonb NULL,
    delivery_status varchar(64) NULL,
    acknowledgement_id varchar(64) NULL,
    remarks varchar(64) NULL,
    additional_details jsonb NULL,
    booking_date varchar(64) NULL,
    received_date varchar(64) NULL,
    row_version int4 NULL
);
 