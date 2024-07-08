CREATE TABLE dristi_orders (
                              id varchar(64) NOT NULL PRIMARY KEY,
                              tenantId varchar(1000) NOT NULL,
                              hearingNumber varchar(64) NULL,
                              applicationNumber jsonb NULL,
                              orderNumber varchar(64) NULL,
                              linkedOrderNumber varchar(64) NULL,
                              filingNumber varchar(64) NULL,
                              cnrNumber varchar(64) NULL,
                              orderType varchar(64) NULL,
                              orderCategory varchar(64) NULL,
                              createdDate varchar(64) NULL,
                              comments varchar(64) NULL,
                              status varchar(64) NULL,
                              isActive bool NULL,
                              issuedBy JSONB NULL,
                              additionalDetails jsonb NULL,
                              createdBy varchar(64) NULL,
                              lastModifiedBy varchar(64) NULL,
                              createdTime int8 NULL,
                              lastModifiedTime int8 NULL
);
CREATE TABLE dristi_order_document (
                              id varchar(64) NOT NULL PRIMARY KEY,
                              fileStore varchar(64) NULL,
                              documentUid varchar(64)  NULL ,
                              documentType varchar(64) NULL,
                              order_id varchar(64)  NULL,
                              additionalDetails JSONB NULL
);

CREATE TABLE dristi_order_statute_section (
                              id varchar(64) NOT NULL PRIMARY KEY,
                              tenantId varchar(64) NOT NULL,
                              order_id varchar(64) NOT NULL,
                              statute varchar(64)  NULL ,
                              sections jsonb NULL,
                              strSections varchar(64) NULL,
                              subsections jsonb  NULL,
                              strSubsections varchar(64)  NULL,
                              additionalDetails jsonb NULL,
                              createdBy varchar(64) NULL,
                              lastModifiedBy varchar(64) NULL,
                              createdTime int8 NULL,
                              lastModifiedTime int8 NULL
);

CREATE SEQUENCE seq_dristi_order
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;