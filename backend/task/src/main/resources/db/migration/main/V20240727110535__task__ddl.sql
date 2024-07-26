ALTER TABLE dristi_orders
DROP COLUMN createdDate;

ALTER TABLE dristi_orders
DROP COLUMN dateCloseBy;

ALTER TABLE dristi_orders
DROP COLUMN dateClosed;

ALTER TABLE dristi_orders
ADD COLUMN createdDate int8 NULL;

ALTER TABLE dristi_orders
ADD COLUMN dateCloseBy int8 NULL;

ALTER TABLE dristi_orders
ADD COLUMN dateClosed int8 NULL;