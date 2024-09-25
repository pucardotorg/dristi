CREATE TABLE dristi_task (
                              id varchar(64) NOT NULL PRIMARY KEY,
                              tenantId varchar(1000) NOT NULL,
                              orderId varchar(64) NULL,
                              filingNumber VARCHAR(64),
                              cnrNumber varchar(64) NULL,
                              taskNumber varchar(64) NULL,
                              createdDate varchar(64) NULL,
                              dateCloseBy varchar(64) NULL,
                              dateClosed varchar(64) NULL,
                              taskDescription varchar(64) NULL,
                              taskType varchar(64) NULL,
                              taskDetails jsonb NULL,
                              status varchar(64) NULL,
                              assignedTo varchar(64) NULL,
                              isActive bool NULL,
                              additionalDetails jsonb NULL,
                              createdBy varchar(64) NULL,
                              lastModifiedBy varchar(64) NULL,
                              createdTime int8 NULL,
                              lastModifiedTime int8 NULL
);
CREATE TABLE dristi_task_document (
                              id varchar(64) NOT NULL PRIMARY KEY,
                              fileStore varchar(64) NULL,
                              documentUid varchar(64)  NULL ,
                              documentType varchar(64) NULL,
                              task_id varchar(64)  NULL,
                              additionalDetails JSONB NULL
);

CREATE TABLE dristi_task_amount (
                              id varchar(64) NOT NULL PRIMARY KEY,
                              amount varchar(64) NULL,
                              type varchar(64)  NULL ,
                              paymentRefNumber varchar(64) NULL,
                              task_id varchar(64)  NULL,
                              status varchar(64)  NULL,
                              additionalDetails JSONB NULL
);

CREATE SEQUENCE seq_dristi_task
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;