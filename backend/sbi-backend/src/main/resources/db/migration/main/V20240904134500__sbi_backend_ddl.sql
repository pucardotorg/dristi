ALTER TABLE transaction_details
DROP COLUMN IF EXISTS success_url;

ALTER TABLE transaction_details
DROP COLUMN IF EXISTS fail_url;

ALTER TABLE transaction_details
ADD COLUMN IF NOT EXISTS tenant_id VARCHAR(30);

