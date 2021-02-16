
CREATE TABLE IF NOT EXISTS "process_definition"
(
    id                 bigserial                                                 not null
        constraint process_record_pk
            primary key,
    tenant_id          bigint                      default 0                     not null,
    process_key        varchar(32)                 default ''::character varying not null,
    contents           text                        default ''::text              not null,
    created_by         bigint                      default 0                     not null,
    created_date       timestamp(6) with time zone default CURRENT_TIMESTAMP     not null,
    last_modified_by   bigint                      default 0                     not null,
    last_modified_date timestamp(6) with time zone default CURRENT_TIMESTAMP     not null,
    process_type       varchar(32)                 default ''::character varying not null,
    reviewer_ids       text                        default ''::text              not null,
    reviewer_names     text                        default ''::text              not null,
    name               varchar(32)                 default ''                    not null
);

comment on table "process_definition" is '流程记录表';

comment on column "process_definition".tenant_id is '租户Id';

comment on column "process_definition".process_key is '流程key与流程定义中的id相';

comment on column "process_definition".contents is '流程定义内容';

comment on column "process_definition".process_type is '流程类型编码，如采购申请编码、调拨申请编码等';

comment on column "process_definition".reviewer_ids is '发起人、审批人列表';

CREATE
UNIQUE INDEX IF NOT EXISTS process_record_tenant_id_process_type
	on "process_definition" (tenant_id, process_type, reviewer_ids);




