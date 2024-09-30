ALTER TABLE cause_list_document ADD COLUMN created_time BIGINT;
ALTER TABLE cause_list_document ADD COLUMN tenant_id VARCHAR(255);
ALTER TABLE cause_list_document ADD COLUMN created_by VARCHAR(255);