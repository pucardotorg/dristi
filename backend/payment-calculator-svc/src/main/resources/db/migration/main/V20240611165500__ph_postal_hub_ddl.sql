CREATE TABLE POSTAL_HUB (

    hub_id                  character varying(64),
    pincode                 character varying(64),
    name                    character varying(64),
    classification          character varying(64),
    created_by              character varying(64),
    created_time            bigint,
    last_modified_by        character varying(64),
    last_modified_time      bigint,
    row_version             bigint,
    tenant_id               character varying(64),

    CONSTRAINT uk_postal_hub_id PRIMARY KEY (hub_id)

);


