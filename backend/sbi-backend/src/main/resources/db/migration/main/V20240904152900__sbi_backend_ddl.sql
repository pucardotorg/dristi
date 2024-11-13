ALTER TABLE transaction_details
ADD COLUMN IF NOT EXISTS total_due NUMERIC(12,2);

ALTER TABLE transaction_details
ADD COLUMN IF NOT EXISTS business_service VARCHAR(64);

ALTER TABLE transaction_details
ADD COLUMN IF NOT EXISTS service_number VARCHAR(64);

ALTER TABLE transaction_details
ADD COLUMN IF NOT EXISTS payer_name VARCHAR(64);

ALTER TABLE transaction_details
ADD COLUMN IF NOT EXISTS paid_by VARCHAR(64);

ALTER TABLE transaction_details
ADD COLUMN IF NOT EXISTS mobile_number VARCHAR(30);