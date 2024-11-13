CREATE TABLE dristi_ocr (
                              id varchar(64) NOT NULL PRIMARY KEY,
                              tenantId varchar(64) NOT NULL,
                              filingNumber varchar(64),
                              fileStoreId varchar(64),
                              documentType varchar(64),
                              message varchar(1000),
                              extractedData JSONB NULL
);