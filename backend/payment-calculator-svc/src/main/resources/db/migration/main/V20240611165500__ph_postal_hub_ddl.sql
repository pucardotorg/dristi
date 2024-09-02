CREATE TABLE POSTAL_HUB (

    hub_id                  character varying(64),
    pincode                 bigint,
    name                    character varying(64),
    distance_km             double precision,
    created_by              character varying(64),
    created_time            bigint,
    last_modified_by        character varying(64),
    last_modified_time      bigint,
    row_version             bigint,
    tenant_id               character varying(64),
    addressId               character varying(64),

    CONSTRAINT uk_postal_hub_id PRIMARY KEY (hub_id)

);


CREATE TABLE IF NOT EXISTS ADDRESS
(
    id                character varying(64),
    tenantId          character varying(1000),
    doorNo            character varying(64),
    latitude          double precision,
    longitude         double precision,
    locationAccuracy  int,
    type              character varying(64),
    addressLine1      character varying(256),
    addressLine2      character varying(256),
    landmark          character varying(256),
    city              character varying(256),
    pincode           character varying(64),
    buildingName      character varying(256),
    street            character varying(256),
    localityCode      character varying(256),
    CONSTRAINT uk_address_id PRIMARY KEY (id)
)