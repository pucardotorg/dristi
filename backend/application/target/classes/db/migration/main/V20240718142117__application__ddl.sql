ALTER TABLE dristi_application
ADD COLUMN statuteSection jsonb;

DROP TABLE IF EXISTS dristi_application_statute_section;
