CREATE TABLE dristi_case (
                              id varchar(36) NOT NULL PRIMARY KEY,
                              tenantId varchar(64) NOT NULL,
                              resolutionMechanism varchar(128),
                              caseTitle VARCHAR(512),
                              caseDescription varchar,
                              filingNumber varchar(64) NOT NULL,
                              cnrNumber varchar(32),
                              courtCaseNumber varchar(24),
                              accessCode varchar(10),
                              courtId varchar(64),
                              benchId varchar(64),
                              judgeId varchar(64),
                              stage varchar(128),
                              substage varchar(128),
                              filingDate int8,
                              registrationDate int8,
                              caseCategory varchar(36),
                              natureOfPleading varchar(36),
                              wfStatus varchar(64),
                              remarks varchar(2056),
                              outcome varchar(128),
                              isActive bool NOT NULL,
                              caseDetails JSONB,
                              additionalDetails jsonb,
                              createdBy varchar(64) NOT NULL,
                              lastModifiedBy varchar(64) NOT NULL,
                              createdTime int8 NOT NULL,
                              lastModifiedTime int8 NOT NULL
);

CREATE UNIQUE INDEX idx_unique_filingnum_combination ON dristi_case(tenantId, filingNumber);
CREATE UNIQUE INDEX idx_unique_casenum_combination ON dristi_case(tenantId, cnrNumber);
CREATE UNIQUE INDEX idx_unique_courtcasenum_combination ON dristi_case(tenantId, courtCaseNumber);
CREATE INDEX idx_dristi_case_tenantId ON dristi_case(tenantId);
CREATE INDEX idx_dristi_case_filingDate ON dristi_case(filingDate);
CREATE INDEX idx_dristi_case_registrationDate ON dristi_case(registrationDate);
CREATE INDEX idx_dristi_case_courtId ON dristi_case(courtId);
CREATE INDEX idx_dristi_case_benchId ON dristi_case(benchId);
CREATE INDEX idx_dristi_case_judgeId ON dristi_case(judgeId);
CREATE INDEX idx_dristi_case_status ON dristi_case(wfStatus);
CREATE INDEX idx_dristi_case_stage ON dristi_case(stage, substage);

CREATE TABLE dristi_case_documents (
                                    id varchar(36) NOT NULL PRIMARY KEY,
                                    tenantId varchar(64) NOT NULL,
                                    filestore_id varchar(64),
                                    documentUid varchar(64),
                                    documentName varchar(128),
                                    documentType varchar(64),
                                    case_id varchar(64) NOT NULL,
                                    isActive bool NOT NULL,
                                    additionalDetails JSONB,
                                    createdBy varchar(36) NOT NULL,
                                    lastModifiedBy varchar(36) NOT NULL,
                                    createdTime int8 NOT NULL,
                                    lastModifiedTime int8 NOT NULL,
                                    CONSTRAINT fk_documents_case
                                                        FOREIGN KEY(case_id) 
                                                        REFERENCES dristi_case(id)
);

CREATE INDEX idx_dristi_case_documents_case_id ON dristi_case_documents(tenantId,case_id);
CREATE INDEX idx_dristi_case_documents_filestore_id ON dristi_case_documents(tenantId,filestore_id);

CREATE TABLE dristi_linked_case (
                                    id varchar(36) NOT NULL PRIMARY KEY,
                                    relationshipType varchar(64) NULL,
                                    caseNumber varchar(64)  NULL ,
                                    isActive bool NULL,
                                    case_id varchar(36)  NULL,
                                    additionalDetails JSONB NULL,
                                    createdBy varchar(64) NULL,
                                    lastModifiedBy varchar(64) NULL,
                                    createdTime int8 NULL,
                                    lastModifiedTime int8 NULL,
                                    CONSTRAINT fk_linkedcase_case
                                                        FOREIGN KEY(case_id) 
                                                        REFERENCES dristi_case(id)
);

CREATE INDEX idx_dristi_linked_case_case_id ON dristi_linked_case(case_id);

CREATE TABLE dristi_linked_case_documents (
                                    id varchar(36) NOT NULL PRIMARY KEY,
                                    tenantId varchar(64) NOT NULL,
                                    filestore_id varchar(64),
                                    documentUid varchar(64),
                                    documentName varchar(128),
                                    documentType varchar(64),
                                    case_id varchar(64) NOT NULL,
                                    isActive bool NOT NULL,
                                    additionalDetails JSONB,
                                    createdBy varchar(36) NOT NULL,
                                    lastModifiedBy varchar(36) NOT NULL,
                                    createdTime int8 NOT NULL,
                                    lastModifiedTime int8 NOT NULL,
                                    CONSTRAINT fk_documents_case
                                                        FOREIGN KEY(case_id) 
                                                        REFERENCES dristi_linked_case(id)
);


CREATE TABLE dristi_case_statutes_and_sections (
                                                   id varchar(36) NOT NULL PRIMARY KEY,
                                                   tenantId varchar(64),
                                                   statute_id varchar(64) NOT NULL,
                                                   section_id varchar(64) NOT NULL,
                                                   case_id varchar(36) NOT NULL,
                                                   additionalDetails JSONB,
                                                   createdBy varchar(36) NOT NULL,
                                                   lastModifiedBy varchar(36) NOT NULL,
                                                   createdTime int8 NOT NULL,
                                                   lastModifiedTime int8 NOT NULL,
                                                   CONSTRAINT fk_statute_case
                                                        FOREIGN KEY(case_id) 
                                                        REFERENCES dristi_case(id)
);

CREATE INDEX idx_statute_section_combination ON dristi_case_statutes_and_sections(statute_id, section_id);
CREATE INDEX idx_dristi_case_statutes_tenant_id ON dristi_case_statutes_and_sections(tenantId);
CREATE INDEX idx_dristi_case_statutes_case_id ON dristi_case_statutes_and_sections(case_id);
CREATE INDEX idx_dristi_case_statutes_statuteId ON dristi_case_statutes_and_sections(statute_id);
CREATE INDEX idx_dristi_case_statutes_sectionId ON dristi_case_statutes_and_sections(section_id);
CREATE INDEX idx_dristi_case_statutes_subsectionId ON dristi_case_statutes_and_sections(subsection_id);

CREATE TABLE dristi_case_litigants (
                                       id varchar(36) NOT NULL PRIMARY KEY,
                                       tenantId varchar(64) NOT NULL,
                                       -- Master data code or uuid 
                                       party_category_id varchar(36) NOT NULL,
                                       -- For cases where an individual is the litigant
                                       individual_id varchar(36),
                                       -- In case the litigant is an org
                                       organisation_id varchar(36),
                                       -- Master data code or uuid for party type 
                                       party_type_id varchar(36),
                                       is_representing_self bool DEFAULT FALSE,
                                       -- Advocate ID
                                       advocate_id varchar(36),
                                       isActive bool NOT NULL,
                                       case_id varchar(36) NOT NULL,
                                       additionalDetails JSONB,
                                       createdBy varchar(36),
                                       lastModifiedBy varchar(36),
                                       createdTime int8,
                                       lastModifiedTime int8,
                                       CONSTRAINT fk_litigant_case
                                                        FOREIGN KEY(case_id) 
                                                        REFERENCES dristi_case(id)
);

CREATE UNIQUE INDEX idx_unique_case_litigant_combination ON dristi_case(case_id, individual_id);
CREATE INDEX idx_dristi_case_litigants_case_id ON dristi_case_litigants(case_id);
CREATE INDEX idx_dristi_case_litigants_tenantId ON dristi_case_litigants(tenantId);
CREATE INDEX idx_dristi_case_litigants_partyCategory ON dristi_case_litigants(party_category_id);
CREATE INDEX idx_dristi_case_litigants_partytype ON dristi_case_litigants(party_type_id);
CREATE INDEX idx_dristi_case_litigants_individualId ON dristi_case_litigants(individual_id);
CREATE INDEX idx_dristi_case_litigants_organisationID ON dristi_case_litigants(organisation_id);


CREATE TABLE dristi_litigant_documents (
                                    id varchar(36) NOT NULL PRIMARY KEY,
                                    tenantId varchar(64) NOT NULL,
                                    filestore_id varchar(64),
                                    documentUid varchar(64),
                                    documentName varchar(128),
                                    documentType varchar(64),
                                    case_id varchar(64) NOT NULL,
                                    party_id varchar(64) NOT NULL,
                                    representative_id varchar(64) NOT NULL,
                                    isActive bool NOT NULL,
                                    additionalDetails JSONB,
                                    createdBy varchar(36) NOT NULL,
                                    lastModifiedBy varchar(36) NOT NULL,
                                    createdTime int8 NOT NULL,
                                    lastModifiedTime int8 NOT NULL,
                                    CONSTRAINT fk_documents_case
                                                        FOREIGN KEY(case_id) 
                                                        REFERENCES dristi_case(id),
                                    CONSTRAINT fk_documents_party
                                                        FOREIGN KEY(party_id)
);

CREATE INDEX idx_dristi_litigant_docs_case_id ON dristi_litigant_advocate_documents(tenantId, case_id);
CREATE INDEX idx_dristi_litigant_docs_advocate_id ON dristi_litigant_advocate_documents(representative_id);
CREATE INDEX idx_dristi_litigant_docs_party_id ON dristi_litigant_advocate_documents(party_id);


CREATE TABLE dristi_witness (
                                    id varchar(36) NOT NULL PRIMARY KEY,
                                    case_id varchar(36)  NULL ,
                                    filingNumber varchar(64),
                                    cnrNumber varchar(64),
                                    witnessIdentifier varchar(64),
                                    witnessName varchar(100),
                                    individualId varchar(64),
                                    remarks varchar(64),
                                    isActive bool NOT NULL,
                                    additionalDetails JSONB,
                                    createdBy varchar(64) NOT NULL,
                                    lastModifiedBy varchar(64) NOT NULL,
                                    createdTime int8 NULL,
                                    lastModifiedTime int8 NULL,
                                    CONSTRAINT fk_witness_case
                                                        FOREIGN KEY(case_id) 
                                                        REFERENCES dristi_case(id)
);

CREATE INDEX idx_dristi_witness_case_id ON dristi_witness(case_id);
CREATE INDEX idx_dristi_witness_ind_id ON dristi_witness(individualId);
CREATE INDEX idx_dristi_witness_ind_name ON dristi_witness(witnessName);
CREATE INDEX idx_dristi_witness_filingNumber ON dristi_witness(filingNumber);
CREATE INDEX idx_dristi_witness_cnrNumber ON dristi_witness(cnrNumber);
CREATE INDEX idx_dristi_witness_witnessIdentifier ON dristi_witness(witnessIdentifier);
