CREATE TABLE cause_list (
    court_id             varchar(64),
    judge_id             varchar(64),
    tenant_id            varchar(64),
    case_id              varchar(64),
    case_title           varchar(255),
    litigant_names       varchar(500),
    hearing_type         varchar(64),
    tentative_slot       VARCHAR(255),
    case_date            varchar(64),
    PRIMARY KEY (case_id, case_date)
);