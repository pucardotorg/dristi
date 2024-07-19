ALTER TABLE dristi_task
DROP COLUMN assignedTo;
ALTER TABLE dristi_task
ADD COLUMN assignedTo jsonb NULL;
