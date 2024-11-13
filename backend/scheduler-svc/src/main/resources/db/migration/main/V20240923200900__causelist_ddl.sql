DROP TABLE IF EXISTS cause_list;
CREATE TABLE cause_list (
    id SERIAL PRIMARY KEY,
    tenant_id VARCHAR(255),
    hearing_id VARCHAR(255),
    filing_number VARCHAR(255),
    application_number JSONB,
    hearing_type VARCHAR(255),
    start_time BIGINT,
    end_time BIGINT,
    case_type VARCHAR(255),
    case_title VARCHAR(255),
    case_registration_date BIGINT,
    case_number VARCHAR(255),
    cmp_number VARCHAR(255),
    court_id VARCHAR(255),
    judge_id VARCHAR(255),
    advocate_names JSONB,
    slot VARCHAR(255),
    hearing_date VARCHAR(255)
);
