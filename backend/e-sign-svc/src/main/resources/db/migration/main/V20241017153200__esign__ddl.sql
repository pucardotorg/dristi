CREATE TABLE dristi_esign_pdf (
                              id varchar(64) NOT NULL PRIMARY KEY,
                              tenantId varchar(1000) NOT NULL,
                              filestoreId varchar(64) NULL,
                              signPlaceHolder VARCHAR(64),
                              signedFilestoreId varchar(64) NULL,
                              pageModule varchar(64) NULL,
                              authType varchar(64) NULL
                              );