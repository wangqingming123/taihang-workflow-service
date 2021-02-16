create table if not exists process_definition
(
    id                 bigserial                                                 not null
        constraint process_record_pk
            primary key,
    tenant_id          bigint                      default 0                     not null,
    process_key        varchar(64)                 default ''::character varying not null,
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

insert into process_definition(tenant_id, process_type, name, reviewer_ids, reviewer_names, created_date) values (123, 'test_approve0', 'test_approve0', '1,2,3,5', '', CURRENT_TIMESTAMP);
insert into process_definition(tenant_id, process_type, name, reviewer_ids, reviewer_names, created_date) values (123, 'test_approve2', 'test_approve2', '1,2,3,5', '', CURRENT_TIMESTAMP);
insert into process_definition(tenant_id, process_type, name, reviewer_ids, reviewer_names, created_date) values (123, 'test_approve1', 'test_approve1', '1,2,3,5', '', CURRENT_TIMESTAMP);
insert into process_definition(tenant_id, process_type, name, reviewer_ids, reviewer_names, created_date) values (123, 'test_approve2', 'test_approve2', '1,2,3,6', '', CURRENT_TIMESTAMP);
insert into process_definition(tenant_id, process_type, name, reviewer_ids, reviewer_names, created_date) values (123, 'test_approve3', 'test_approve3', '1,2,3,5', '', CURRENT_TIMESTAMP);
insert into process_definition(tenant_id, process_type, name, reviewer_ids, reviewer_names, created_date) values (123, 'test_approve4', 'test_approve4', '1,2,3,5', '', CURRENT_TIMESTAMP);
insert into process_definition(tenant_id, process_type, name, reviewer_ids, reviewer_names, created_date) values (123, 'test_approve5', 'test_approve5', '1,2,3,5', '', CURRENT_TIMESTAMP);
insert into process_definition(tenant_id, process_type, name, reviewer_ids, reviewer_names, created_date) values (123, 'test_approve6', 'test_approve6', '1,2,3,5', '', CURRENT_TIMESTAMP);

create table process_task_history
(
    id                    bigserial                                                 not null
        constraint process_task_history_pk
            primary key,
    business_id           bigint                      default 0                     not null,
    business_code         varchar(64)                 default ''::character varying not null,
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

insert into process_task_history (id, business_code, process_type, process_instance_id, process_instance_name, operator_id, operator_name, begin_time, end_time, task_id, task_name, status, comment, apply_user_id, apply_user, tenant_id) values (302, '123456', 'abc12', '9fd0403a-9ef6-11ea-99d0-080027d76b76', '', 225, 'test2', '2020-05-26 02:15:53.827788', '2020-05-26 02:15:53.940000', 'a91b8ddd-9ef6-11ea-99d0-080027d76b76', 'test2', 'REJECT', 'n0可以', 987, 'sundp', 1);
insert into process_task_history (id, business_code, process_type, process_instance_id, process_instance_name, operator_id, operator_name, begin_time, end_time, task_id, task_name, status, comment, apply_user_id, apply_user, tenant_id) values (303, '123456', 'abc12', '9fd0403a-9ef6-11ea-99d0-080027d76b76', '', 1, 'System', '2020-05-26 02:15:53.827788', '2020-05-26 02:15:54.168000', '', '', 'END', '流程结束', 987, 'sundp', 1);
insert into process_task_history (id, business_code, process_type, process_instance_id, process_instance_name, operator_id, operator_name, begin_time, end_time, task_id, task_name, status, comment, apply_user_id, apply_user, tenant_id) values (299, '123456', 'abc12', '9fd0403a-9ef6-11ea-99d0-080027d76b76', '', 226, '', '2020-05-26 02:14:26.795821', null, '9fd06754-9ef6-11ea-99d0-080027d76b76', 'test11', 'IN_PROCESS', '', 987, 'sundp', 1);
insert into process_task_history (id, business_code, process_type, process_instance_id, process_instance_name, operator_id, operator_name, begin_time, end_time, task_id, task_name, status, comment, apply_user_id, apply_user, tenant_id) values (300, '123456', 'abc12', '9fd0403a-9ef6-11ea-99d0-080027d76b76', '', 226, '', '2020-05-26 02:14:42.245791', '2020-05-26 02:14:42.348000', '9fd06754-9ef6-11ea-99d0-080027d76b76', 'test11', 'APPROVED', '可以', 987, 'sundp', 1);
insert into process_task_history (id, business_code, process_type, process_instance_id, process_instance_name, operator_id, operator_name, begin_time, end_time, task_id, task_name, status, comment, apply_user_id, apply_user, tenant_id) values (301, '123456', 'abc12', '9fd0403a-9ef6-11ea-99d0-080027d76b76', '', 225, '', '2020-05-26 02:14:42.245791', null, 'a91b8ddd-9ef6-11ea-99d0-080027d76b76', 'test2', 'IN_PROCESS', '', 987, 'sundp', 1);

