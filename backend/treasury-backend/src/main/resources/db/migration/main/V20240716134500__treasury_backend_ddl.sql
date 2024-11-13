ALTER TABLE auth_sek_session_data ADD COLUMN department_id VARCHAR(64) NULL;

CREATE TABLE treasury_payment_data (
    department_id VARCHAR(30) PRIMARY KEY,
    grn VARCHAR(30),
    challan_timestamp TIMESTAMP,
    bank_ref_no VARCHAR(30),
    bank_timestamp TIMESTAMP,
    bank_code VARCHAR(30),
    status VARCHAR(10),
    cin VARCHAR(30),
    amount DECIMAL(10, 2),
    party_name VARCHAR(100),
    remark_status VARCHAR(100),
    remarks VARCHAR(255),
    file_store_id VARCHAR(64)
);