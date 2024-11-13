CREATE TABLE judge_calendar_rules (

    judge_id                varchar(64),
    id                      varchar(64),
    rule_type               varchar(64),
    date                    bigint,
    notes                   text,
    created_by              character varying(64),
    created_time            bigint,
    last_modified_by        character varying(64),
    last_modified_time      bigint,
    row_version             bigint,
    tenant_id               character varying(64),

    CONSTRAINT pk_judge_calendar_rules_id PRIMARY KEY (id),
    CONSTRAINT unique_judge_date_constraint UNIQUE(judge_id, date)


);