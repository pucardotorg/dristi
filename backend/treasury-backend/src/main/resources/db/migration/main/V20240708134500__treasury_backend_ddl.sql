CREATE TABLE auth_sek_session_data (
    auth_token VARCHAR(64) PRIMARY KEY,
    decrypted_sek VARCHAR(64),
    bill_id VARCHAR(64),
    business_service VARCHAR(64),
    service_number VARCHAR(64),
    total_due NUMERIC(8,2),
    mobile_number VARCHAR(64),
    paid_by VARCHAR(64),
    session_time bigint
);