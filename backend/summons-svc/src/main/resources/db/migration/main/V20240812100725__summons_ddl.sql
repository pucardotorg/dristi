ALTER TABLE summons_delivery ALTER COLUMN case_id DROP NOT NULL;
ALTER TABLE summons_delivery ALTER COLUMN doc_type DROP NOT NULL;
ALTER TABLE summons_delivery ALTER COLUMN doc_sub_type DROP NOT NULL;
ALTER TABLE summons_delivery ALTER COLUMN channel_name DROP NOT NULL;
ALTER TABLE summons_delivery ALTER COLUMN party_type DROP NOT NULL;