ALTER TABLE dristi_application
DROP COLUMN createdDate;

ALTER TABLE dristi_application
ADD COLUMN createdDate int8 NULL;
