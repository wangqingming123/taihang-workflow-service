-- auto-generated definition
create table if not exists process_handle_record
(
    id                    bigserial                                                 not null
        constraint process_handle_record_pk
            primary key,
    process_apply_id      varchar(64)                 default ''::character varying not null,
    process_type          varchar(32)                 default ''::character varying not null,
    process_instance_id   varchar(64)                 default ''::character varying not null,
    process_instance_name varchar(255)                default ''::character varying not null,
    operator_id           bigint                      default 0                     not null,
    operator_name         varchar(64)                 default ''::character varying not null,
    begin_time            timestamp(6) with time zone default CURRENT_TIMESTAMP     not null,
    end_time              timestamp(6) with time zone,
    task_id               varchar(64)                 default ''::character varying not null,
    task_name             varchar(255)                default ''::character varying not null,
    status                varchar(32)                 default ''::character varying not null,
    comment               varchar(200)                default ''::character varying not null,
    apply_user_id         bigint                      default 0                     not null,
    apply_user            varchar(64)                 default ''::character varying not null,
    tenant_id             bigint                      default 0                     not null
);

comment on table process_handle_record is '流程处理记录表';

comment on column process_handle_record.id is '主键';

comment on column process_handle_record.process_apply_id is '流程业务Id';

comment on column process_handle_record.process_type is '流程类型';

comment on column process_handle_record.process_instance_id is '流程实例Id';

comment on column process_handle_record.process_instance_name is '流程实例名称';

comment on column process_handle_record.operator_id is '操作人Id';

comment on column process_handle_record.operator_name is '操作人名称';

comment on column process_handle_record.begin_time is '开始时间';

comment on column process_handle_record.end_time is '结束时间';

comment on column process_handle_record.task_id is '当前任务Id';

comment on column process_handle_record.task_name is '当前任务名称';

comment on column process_handle_record.status is '流程状态,draft:草稿 submitted:提交成功 in_process:审批中 approved:审批通过 reject:驳回 refuse:拒绝 end:结束(归档) cancel:取消';

comment on column process_handle_record.comment is '审批人意见';

comment on column process_handle_record.apply_user_id is '申请人Id';

comment on column process_handle_record.apply_user is '申请人';