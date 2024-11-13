ALTER TABLE reschedule_request_opt_out_detail
ADD COLUMN id varchar(64) NOT NULL;

ALTER TABLE reschedule_request_opt_out_detail
ADD CONSTRAINT unique_id UNIQUE (id);