ALTER TABLE dristi_cases
DROP COLUMN filingDate;
ALTER TABLE dristi_cases
DROP COLUMN registrationDate;
ALTER TABLE dristi_cases
DROP COLUMN judgementDate;

ALTER TABLE dristi_cases
ADD COLUMN filingDate int8 NULL;
ALTER TABLE dristi_cases
ADD COLUMN registrationDate int8 NULL;
ALTER TABLE dristi_cases
ADD COLUMN judgementDate int8 NULL;