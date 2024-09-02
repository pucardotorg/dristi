CREATE TABLE POSTAL_SERVICE (

    postal_service_id       character varying(64),
    pincode                 bigint,
    postal_hub_id           character varying(64),
    distance_km             character varying(64),
    created_by              character varying(64),
    created_time            bigint,
    last_modified_by        character varying(64),
    last_modified_time      bigint,
    row_version             bigint,
    tenant_id               character varying(64),

    CONSTRAINT uk_postal_service_id PRIMARY KEY (postal_service_id)

);