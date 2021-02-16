alter table "public".process_task_history add business_id bigint default 0 not null;

alter table "public".process_task_history rename column process_apply_id to business_code;

comment on column "public".process_task_history.business_id is '业务主键';
comment on column "public".process_task_history.business_code is '业务编码';