ALTER TABLE dristi_orders
DROP COLUMN createdDate;

ALTER TABLE dristi_orders
ADD COLUMN createdDate int8 NULL;