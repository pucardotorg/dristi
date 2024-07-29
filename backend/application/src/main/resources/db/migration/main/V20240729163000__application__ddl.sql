
ALTER TABLE dristi_application
ALTER COLUMN comment TYPE jsonb
USING comment::jsonb;