ALTER TABLE lock
ADD COLUMN entity varchar(64),
ADD COLUMN userId varchar(64),
ADD CONSTRAINT unique_key_tenantid_constraint UNIQUE (tenantId, uniqueId);