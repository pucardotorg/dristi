ALTER TABLE dristi_task
DROP COLUMN createdDate;

ALTER TABLE dristi_task
DROP COLUMN dateCloseBy;

ALTER TABLE dristi_task
DROP COLUMN dateClosed;

ALTER TABLE dristi_task
ADD COLUMN createdDate int8 NULL;

ALTER TABLE dristi_task
ADD COLUMN dateCloseBy int8 NULL;

ALTER TABLE dristi_task
ADD COLUMN dateClosed int8 NULL;