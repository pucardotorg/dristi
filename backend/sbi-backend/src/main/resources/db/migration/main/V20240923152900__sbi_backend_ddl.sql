ALTER TABLE transaction_details
ADD COLUMN IF NOT EXISTS amount_details jsonb NULL;