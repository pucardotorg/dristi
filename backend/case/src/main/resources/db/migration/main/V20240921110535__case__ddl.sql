CREATE TABLE dristi_case_hearing_type_priority (
                              id varchar(64) NOT NULL PRIMARY KEY,
                              caseType varchar(1000) NOT NULL,
                              description varchar(64) NULL,
                              priority int8 NULL,
                              isActive bool NULL
);

INSERT INTO dristi_case_hearing_type_priority (id, caseType, description, priority, isActive) VALUES
('1', 'CMP', 'Criminal Miscellaneous Petition', 15, true),
('2', 'ST', 'Summary Trial', 10, true),
('3', 'CC', 'Calendar Case', 5, true);


