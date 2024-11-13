CREATE TABLE hearing_booking_reschedule_request (

    hearing_booking_id          character varying(64),
    reschedule_request_id       character varying(64),
    requester_id                character varying(64),
    status                      character varying(64),
    reason                      text,
    created_by                  character varying(64),
    created_time                bigint,
    last_modified_by            character varying(64),
    last_modified_time          bigint,
    row_version                 bigint,
    tenant_id                   character varying(64),
    case_id                    character varying(64),
    judge_id                    character varying(64),
    representatives             jsonb,
    litigants                   jsonb,
    suggested_days              jsonb,
    available_days              jsonb,

    CONSTRAINT pk_hearing_booking_reschedule_request_id PRIMARY KEY (reschedule_request_id)

);