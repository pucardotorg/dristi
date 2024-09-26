CREATE TABLE cause_list_document (
    file_store_id varchar(64) NOT NULL,
    court_id varchar(64),
    judge_id varchar(64),
    hearing_date varchar(64) NOT NULL,
    PRIMARY KEY(file_store_id)
);