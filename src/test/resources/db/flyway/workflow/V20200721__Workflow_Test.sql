--以下是引擎表
create table if not exists act_ge_property
(
    name_  varchar(64) not null
        constraint act_ge_property_pkey
            primary key,
    value_ varchar(300),
    rev_   integer
);


create table if not exists flw_event_deployment
(
    id_                   varchar(255) not null
        constraint flw_event_deployment_pkey
            primary key,
    name_                 varchar(255),
    category_             varchar(255),
    deploy_time_          timestamp(3),
    tenant_id_            varchar(255),
    parent_deployment_id_ varchar(255)
);


create table if not exists flw_event_resource
(
    id_             varchar(255) not null
        constraint flw_event_resource_pkey
            primary key,
    name_           varchar(255),
    deployment_id_  varchar(255),
    resource_bytes_ bytea
);


create table if not exists flw_event_definition
(
    id_            varchar(255) not null
        constraint flw_event_definition_pkey
            primary key,
    name_          varchar(255),
    version_       integer,
    key_           varchar(255),
    category_      varchar(255),
    deployment_id_ varchar(255),
    tenant_id_     varchar(255),
    resource_name_ varchar(255),
    description_   varchar(255)
);


create unique index act_idx_event_def_uniq
    on flw_event_definition (key_, version_, tenant_id_);

create table if not exists flw_channel_definition
(
    id_            varchar(255) not null
        constraint flw_channel_definition_pkey
            primary key,
    name_          varchar(255),
    version_       integer,
    key_           varchar(255),
    category_      varchar(255),
    deployment_id_ varchar(255),
    create_time_   timestamp(3),
    tenant_id_     varchar(255),
    resource_name_ varchar(255),
    description_   varchar(255)
);


create
unique index act_idx_channel_def_uniq
    on flw_channel_definition (key_, version_, tenant_id_);

create table if not exists act_id_property
(
    name_  varchar(64) not null
        constraint act_id_property_pkey
            primary key,
    value_ varchar(300),
    rev_   integer
);


create table if not exists act_id_bytearray
(
    id_    varchar(64) not null
        constraint act_id_bytearray_pkey
            primary key,
    rev_   integer,
    name_  varchar(255),
    bytes_ bytea
);


create table if not exists act_id_group
(
    id_   varchar(64) not null
        constraint act_id_group_pkey
            primary key,
    rev_  integer,
    name_ varchar(255),
    type_ varchar(255)
);


create table if not exists act_id_user
(
    id_           varchar(64) not null
        constraint act_id_user_pkey
            primary key,
    rev_          integer,
    first_        varchar(255),
    last_         varchar(255),
    display_name_ varchar(255),
    email_        varchar(255),
    pwd_          varchar(255),
    picture_id_   varchar(64),
    tenant_id_    varchar(255) default ''::character varying
);


create table if not exists act_id_membership
(
    user_id_  varchar(64) not null
        constraint act_fk_memb_user
            references act_id_user,
    group_id_ varchar(64) not null
        constraint act_fk_memb_group
            references act_id_group,
    constraint act_id_membership_pkey
        primary key (user_id_, group_id_)
);


create
index act_idx_memb_group
    on act_id_membership (group_id_);

create
index act_idx_memb_user
    on act_id_membership (user_id_);

create table if not exists act_id_info
(
    id_        varchar(64) not null
        constraint act_id_info_pkey
            primary key,
    rev_       integer,
    user_id_   varchar(64),
    type_      varchar(64),
    key_       varchar(255),
    value_     varchar(255),
    password_  bytea,
    parent_id_ varchar(255)
);


create table if not exists act_id_token
(
    id_          varchar(64) not null
        constraint act_id_token_pkey
            primary key,
    rev_         integer,
    token_value_ varchar(255),
    token_date_  timestamp,
    ip_address_  varchar(255),
    user_agent_  varchar(255),
    user_id_     varchar(255),
    token_data_  varchar(2000)
);


create table if not exists act_id_priv
(
    id_   varchar(64)  not null
        constraint act_id_priv_pkey
            primary key,
    name_ varchar(255) not null
        constraint act_uniq_priv_name
            unique
);


create table if not exists act_id_priv_mapping
(
    id_       varchar(64) not null
        constraint act_id_priv_mapping_pkey
            primary key,
    priv_id_  varchar(64) not null
        constraint act_fk_priv_mapping
            references act_id_priv,
    user_id_  varchar(255),
    group_id_ varchar(255)
);


create
index act_idx_priv_mapping
    on act_id_priv_mapping (priv_id_);

create
index act_idx_priv_user
    on act_id_priv_mapping (user_id_);

create
index act_idx_priv_group
    on act_id_priv_mapping (group_id_);

create table if not exists act_hi_identitylink
(
    id_                  varchar(64) not null
        constraint act_hi_identitylink_pkey
            primary key,
    group_id_            varchar(255),
    type_                varchar(255),
    user_id_             varchar(255),
    task_id_             varchar(64),
    create_time_         timestamp,
    proc_inst_id_        varchar(64),
    scope_id_            varchar(255),
    sub_scope_id_        varchar(255),
    scope_type_          varchar(255),
    scope_definition_id_ varchar(255)
);


create
index act_idx_hi_ident_lnk_user
    on act_hi_identitylink (user_id_);

create
index act_idx_hi_ident_lnk_scope
    on act_hi_identitylink (scope_id_, scope_type_);

create
index act_idx_hi_ident_lnk_sub_scope
    on act_hi_identitylink (sub_scope_id_, scope_type_);

create
index act_idx_hi_ident_lnk_scope_def
    on act_hi_identitylink (scope_definition_id_, scope_type_);

create
index act_idx_hi_ident_lnk_task
    on act_hi_identitylink (task_id_);

create
index act_idx_hi_ident_lnk_procinst
    on act_hi_identitylink (proc_inst_id_);

create table if not exists act_ru_entitylink
(
    id_                      varchar(64) not null
        constraint act_ru_entitylink_pkey
            primary key,
    rev_                     integer,
    create_time_             timestamp,
    link_type_               varchar(255),
    scope_id_                varchar(255),
    scope_type_              varchar(255),
    scope_definition_id_     varchar(255),
    ref_scope_id_            varchar(255),
    ref_scope_type_          varchar(255),
    ref_scope_definition_id_ varchar(255),
    hierarchy_type_          varchar(255)
);


create
index act_idx_ent_lnk_scope
    on act_ru_entitylink (scope_id_, scope_type_, link_type_);

create
index act_idx_ent_lnk_scope_def
    on act_ru_entitylink (scope_definition_id_, scope_type_, link_type_);

create table if not exists act_hi_entitylink
(
    id_                      varchar(64) not null
        constraint act_hi_entitylink_pkey
            primary key,
    link_type_               varchar(255),
    create_time_             timestamp,
    scope_id_                varchar(255),
    scope_type_              varchar(255),
    scope_definition_id_     varchar(255),
    ref_scope_id_            varchar(255),
    ref_scope_type_          varchar(255),
    ref_scope_definition_id_ varchar(255),
    hierarchy_type_          varchar(255)
);

create
index act_idx_hi_ent_lnk_scope
    on act_hi_entitylink (scope_id_, scope_type_, link_type_);

create
index act_idx_hi_ent_lnk_scope_def
    on act_hi_entitylink (scope_definition_id_, scope_type_, link_type_);

create table if not exists act_hi_taskinst
(
    id_                       varchar(64) not null
        constraint act_hi_taskinst_pkey
            primary key,
    rev_                      integer      default 1,
    proc_def_id_              varchar(64),
    task_def_id_              varchar(64),
    task_def_key_             varchar(255),
    proc_inst_id_             varchar(64),
    execution_id_             varchar(64),
    scope_id_                 varchar(255),
    sub_scope_id_             varchar(255),
    scope_type_               varchar(255),
    scope_definition_id_      varchar(255),
    propagated_stage_inst_id_ varchar(255),
    name_                     varchar(255),
    parent_task_id_           varchar(64),
    description_              varchar(4000),
    owner_                    varchar(255),
    assignee_                 varchar(255),
    start_time_               timestamp   not null,
    claim_time_               timestamp,
    end_time_                 timestamp,
    duration_                 bigint,
    delete_reason_            varchar(4000),
    priority_                 integer,
    due_date_                 timestamp,
    form_key_                 varchar(255),
    category_                 varchar(255),
    tenant_id_                varchar(255) default ''::character varying,
    last_updated_time_        timestamp
);


create
index act_idx_hi_task_scope
    on act_hi_taskinst (scope_id_, scope_type_);

create
index act_idx_hi_task_sub_scope
    on act_hi_taskinst (sub_scope_id_, scope_type_);

create
index act_idx_hi_task_scope_def
    on act_hi_taskinst (scope_definition_id_, scope_type_);

create
index act_idx_hi_task_inst_procinst
    on act_hi_taskinst (proc_inst_id_);

create table if not exists act_hi_tsk_log
(
    id_                  serial      not null
        constraint act_hi_tsk_log_pkey
            primary key,
    type_                varchar(64),
    task_id_             varchar(64) not null,
    time_stamp_          timestamp   not null,
    user_id_             varchar(255),
    data_                varchar(4000),
    execution_id_        varchar(64),
    proc_inst_id_        varchar(64),
    proc_def_id_         varchar(64),
    scope_id_            varchar(255),
    scope_definition_id_ varchar(255),
    sub_scope_id_        varchar(255),
    scope_type_          varchar(255),
    tenant_id_           varchar(255) default ''::character varying
);


create table if not exists act_hi_varinst
(
    id_                varchar(64)  not null
        constraint act_hi_varinst_pkey
            primary key,
    rev_               integer default 1,
    proc_inst_id_      varchar(64),
    execution_id_      varchar(64),
    task_id_           varchar(64),
    name_              varchar(255) not null,
    var_type_          varchar(100),
    scope_id_          varchar(255),
    sub_scope_id_      varchar(255),
    scope_type_        varchar(255),
    bytearray_id_      varchar(64),
    double_            double precision,
    long_              bigint,
    text_              varchar(4000),
    text2_             varchar(4000),
    create_time_       timestamp,
    last_updated_time_ timestamp
);


create
index act_idx_hi_procvar_name_type
    on act_hi_varinst (name_, var_type_);

create
index act_idx_hi_var_scope_id_type
    on act_hi_varinst (scope_id_, scope_type_);

create
index act_idx_hi_var_sub_id_type
    on act_hi_varinst (sub_scope_id_, scope_type_);

create
index act_idx_hi_procvar_proc_inst
    on act_hi_varinst (proc_inst_id_);

create
index act_idx_hi_procvar_task_id
    on act_hi_varinst (task_id_);

create
index act_idx_hi_procvar_exe
    on act_hi_varinst (execution_id_);

create table if not exists act_ru_history_job
(
    id_                 varchar(64) not null
        constraint act_ru_history_job_pkey
            primary key,
    rev_                integer,
    lock_exp_time_      timestamp,
    lock_owner_         varchar(255),
    retries_            integer,
    exception_stack_id_ varchar(64),
    exception_msg_      varchar(4000),
    handler_type_       varchar(255),
    handler_cfg_        varchar(4000),
    custom_values_id_   varchar(64),
    adv_handler_cfg_id_ varchar(64),
    create_time_        timestamp,
    scope_type_         varchar(255),
    tenant_id_          varchar(255) default ''::character varying
);


create table if not exists flw_ru_batch
(
    id_            varchar(64) not null
        constraint flw_ru_batch_pkey
            primary key,
    rev_           integer,
    type_          varchar(64) not null,
    search_key_    varchar(255),
    search_key2_   varchar(255),
    create_time_   timestamp   not null,
    complete_time_ timestamp,
    status_        varchar(255),
    batch_doc_id_  varchar(64),
    tenant_id_     varchar(255) default ''::character varying
);

create table if not exists flw_ru_batch_part
(
    id_            varchar(64) not null
        constraint flw_ru_batch_part_pkey
            primary key,
    rev_           integer,
    batch_id_      varchar(64)
        constraint flw_fk_batch_part_parent
            references flw_ru_batch,
    type_          varchar(64) not null,
    scope_id_      varchar(64),
    sub_scope_id_  varchar(64),
    scope_type_    varchar(64),
    search_key_    varchar(255),
    search_key2_   varchar(255),
    create_time_   timestamp   not null,
    complete_time_ timestamp,
    status_        varchar(255),
    result_doc_id_ varchar(64),
    tenant_id_     varchar(255) default ''::character varying
);


create
index flw_idx_batch_part
    on flw_ru_batch_part (batch_id_);

create table if not exists act_re_deployment
(
    id_                   varchar(64) not null
        constraint act_re_deployment_pkey
            primary key,
    name_                 varchar(255),
    category_             varchar(255),
    key_                  varchar(255),
    tenant_id_            varchar(255) default ''::character varying,
    deploy_time_          timestamp,
    derived_from_         varchar(64),
    derived_from_root_    varchar(64),
    parent_deployment_id_ varchar(255),
    engine_version_       varchar(255)
);


create table if not exists act_ge_bytearray
(
    id_            varchar(64) not null
        constraint act_ge_bytearray_pkey
            primary key,
    rev_           integer,
    name_          varchar(255),
    deployment_id_ varchar(64)
        constraint act_fk_bytearr_depl
            references act_re_deployment,
    bytes_         bytea,
    generated_     boolean
);


create
index act_idx_bytear_depl
    on act_ge_bytearray (deployment_id_);

create table if not exists act_re_model
(
    id_                           varchar(64) not null
        constraint act_re_model_pkey
            primary key,
    rev_                          integer,
    name_                         varchar(255),
    key_                          varchar(255),
    category_                     varchar(255),
    create_time_                  timestamp,
    last_update_time_             timestamp,
    version_                      integer,
    meta_info_                    varchar(4000),
    deployment_id_                varchar(64)
        constraint act_fk_model_deployment
            references act_re_deployment,
    editor_source_value_id_       varchar(64)
        constraint act_fk_model_source
            references act_ge_bytearray,
    editor_source_extra_value_id_ varchar(64)
        constraint act_fk_model_source_extra
            references act_ge_bytearray,
    tenant_id_                    varchar(255) default ''::character varying
);


create
index act_idx_model_source
    on act_re_model (editor_source_value_id_);

create
index act_idx_model_source_extra
    on act_re_model (editor_source_extra_value_id_);

create
index act_idx_model_deployment
    on act_re_model (deployment_id_);

create table if not exists act_re_procdef
(
    id_                     varchar(64)            not null
        constraint act_re_procdef_pkey
            primary key,
    rev_                    integer,
    category_               varchar(255),
    name_                   varchar(255),
    key_                    varchar(255)           not null,
    version_                integer                not null,
    deployment_id_          varchar(64),
    resource_name_          varchar(4000),
    dgrm_resource_name_     varchar(4000),
    description_            varchar(4000),
    has_start_form_key_     boolean,
    has_graphical_notation_ boolean,
    suspension_state_       integer,
    tenant_id_              varchar(255) default ''::character varying,
    derived_from_           varchar(64),
    derived_from_root_      varchar(64),
    derived_version_        integer      default 0 not null,
    engine_version_         varchar(255),
    constraint act_uniq_procdef
        unique (key_, version_, derived_version_, tenant_id_)
);


create table if not exists act_ru_execution
(
    id_                       varchar(64) not null
        constraint act_ru_execution_pkey
            primary key,
    rev_                      integer,
    proc_inst_id_             varchar(64)
        constraint act_fk_exe_procinst
            references act_ru_execution,
    business_key_             varchar(255),
    parent_id_                varchar(64)
        constraint act_fk_exe_parent
            references act_ru_execution,
    proc_def_id_              varchar(64)
        constraint act_fk_exe_procdef
            references act_re_procdef,
    super_exec_               varchar(64)
        constraint act_fk_exe_super
            references act_ru_execution,
    root_proc_inst_id_        varchar(64),
    act_id_                   varchar(255),
    is_active_                boolean,
    is_concurrent_            boolean,
    is_scope_                 boolean,
    is_event_scope_           boolean,
    is_mi_root_               boolean,
    suspension_state_         integer,
    cached_ent_state_         integer,
    tenant_id_                varchar(255) default ''::character varying,
    name_                     varchar(255),
    start_act_id_             varchar(255),
    start_time_               timestamp,
    start_user_id_            varchar(255),
    lock_time_                timestamp,
    is_count_enabled_         boolean,
    evt_subscr_count_         integer,
    task_count_               integer,
    job_count_                integer,
    timer_job_count_          integer,
    susp_job_count_           integer,
    deadletter_job_count_     integer,
    var_count_                integer,
    id_link_count_            integer,
    callback_id_              varchar(255),
    callback_type_            varchar(255),
    reference_id_             varchar(255),
    reference_type_           varchar(255),
    propagated_stage_inst_id_ varchar(255)
);


create table if not exists act_ru_event_subscr
(
    id_                  varchar(64)  not null
        constraint act_ru_event_subscr_pkey
            primary key,
    rev_                 integer,
    event_type_          varchar(255) not null,
    event_name_          varchar(255),
    execution_id_        varchar(64)
        constraint act_fk_event_exec
            references act_ru_execution,
    proc_inst_id_        varchar(64),
    activity_id_         varchar(64),
    configuration_       varchar(255),
    created_             timestamp    not null,
    proc_def_id_         varchar(64),
    sub_scope_id_        varchar(64),
    scope_id_            varchar(64),
    scope_definition_id_ varchar(64),
    scope_type_          varchar(64),
    tenant_id_           varchar(255) default ''::character varying
);


create
index act_idx_event_subscr_config_
    on act_ru_event_subscr (configuration_);

create
index act_idx_event_subscr
    on act_ru_event_subscr (execution_id_);

create table if not exists act_ru_task
(
    id_                       varchar(64) not null
        constraint act_ru_task_pkey
            primary key,
    rev_                      integer,
    execution_id_             varchar(64)
        constraint act_fk_task_exe
            references act_ru_execution,
    proc_inst_id_             varchar(64)
        constraint act_fk_task_procinst
            references act_ru_execution,
    proc_def_id_              varchar(64)
        constraint act_fk_task_procdef
            references act_re_procdef,
    task_def_id_              varchar(64),
    scope_id_                 varchar(255),
    sub_scope_id_             varchar(255),
    scope_type_               varchar(255),
    scope_definition_id_      varchar(255),
    propagated_stage_inst_id_ varchar(255),
    name_                     varchar(255),
    parent_task_id_           varchar(64),
    description_              varchar(4000),
    task_def_key_             varchar(255),
    owner_                    varchar(255),
    assignee_                 varchar(255),
    delegation_               varchar(64),
    priority_                 integer,
    create_time_              timestamp,
    due_date_                 timestamp,
    category_                 varchar(255),
    suspension_state_         integer,
    tenant_id_                varchar(255) default ''::character varying,
    form_key_                 varchar(255),
    claim_time_               timestamp,
    is_count_enabled_         boolean,
    var_count_                integer,
    id_link_count_            integer,
    sub_task_count_           integer
);


create table if not exists act_ru_identitylink
(
    id_                  varchar(64) not null
        constraint act_ru_identitylink_pkey
            primary key,
    rev_                 integer,
    group_id_            varchar(255),
    type_                varchar(255),
    user_id_             varchar(255),
    task_id_             varchar(64)
        constraint act_fk_tskass_task
            references act_ru_task,
    proc_inst_id_        varchar(64)
        constraint act_fk_idl_procinst
            references act_ru_execution,
    proc_def_id_         varchar(64)
        constraint act_fk_athrz_procedef
            references act_re_procdef,
    scope_id_            varchar(255),
    sub_scope_id_        varchar(255),
    scope_type_          varchar(255),
    scope_definition_id_ varchar(255)
);


create
index act_idx_ident_lnk_user
    on act_ru_identitylink (user_id_);

create
index act_idx_ident_lnk_group
    on act_ru_identitylink (group_id_);

create
index act_idx_ident_lnk_scope
    on act_ru_identitylink (scope_id_, scope_type_);

create
index act_idx_ident_lnk_sub_scope
    on act_ru_identitylink (sub_scope_id_, scope_type_);

create
index act_idx_ident_lnk_scope_def
    on act_ru_identitylink (scope_definition_id_, scope_type_);

create
index act_idx_tskass_task
    on act_ru_identitylink (task_id_);

create
index act_idx_athrz_procedef
    on act_ru_identitylink (proc_def_id_);

create
index act_idx_idl_procinst
    on act_ru_identitylink (proc_inst_id_);

create
index act_idx_task_create
    on act_ru_task (create_time_);

create
index act_idx_task_scope
    on act_ru_task (scope_id_, scope_type_);

create
index act_idx_task_sub_scope
    on act_ru_task (sub_scope_id_, scope_type_);

create
index act_idx_task_scope_def
    on act_ru_task (scope_definition_id_, scope_type_);

create
index act_idx_task_exec
    on act_ru_task (execution_id_);

create
index act_idx_task_procinst
    on act_ru_task (proc_inst_id_);

create
index act_idx_task_procdef
    on act_ru_task (proc_def_id_);

create table if not exists act_ru_variable
(
    id_           varchar(64)  not null
        constraint act_ru_variable_pkey
            primary key,
    rev_          integer,
    type_         varchar(255) not null,
    name_         varchar(255) not null,
    execution_id_ varchar(64)
        constraint act_fk_var_exe
            references act_ru_execution,
    proc_inst_id_ varchar(64)
        constraint act_fk_var_procinst
            references act_ru_execution,
    task_id_      varchar(64),
    scope_id_     varchar(255),
    sub_scope_id_ varchar(255),
    scope_type_   varchar(255),
    bytearray_id_ varchar(64)
        constraint act_fk_var_bytearray
            references act_ge_bytearray,
    double_       double precision,
    long_         bigint,
    text_         varchar(4000),
    text2_        varchar(4000)
);


create
index act_idx_ru_var_scope_id_type
    on act_ru_variable (scope_id_, scope_type_);

create
index act_idx_ru_var_sub_id_type
    on act_ru_variable (sub_scope_id_, scope_type_);

create
index act_idx_var_bytearray
    on act_ru_variable (bytearray_id_);

create
index act_idx_variable_task_id
    on act_ru_variable (task_id_);

create
index act_idx_var_exe
    on act_ru_variable (execution_id_);

create
index act_idx_var_procinst
    on act_ru_variable (proc_inst_id_);

create table if not exists act_ru_job
(
    id_                  varchar(64)  not null
        constraint act_ru_job_pkey
            primary key,
    rev_                 integer,
    type_                varchar(255) not null,
    lock_exp_time_       timestamp,
    lock_owner_          varchar(255),
    exclusive_           boolean,
    execution_id_        varchar(64)
        constraint act_fk_job_execution
            references act_ru_execution,
    process_instance_id_ varchar(64)
        constraint act_fk_job_process_instance
            references act_ru_execution,
    proc_def_id_         varchar(64)
        constraint act_fk_job_proc_def
            references act_re_procdef,
    element_id_          varchar(255),
    element_name_        varchar(255),
    scope_id_            varchar(255),
    sub_scope_id_        varchar(255),
    scope_type_          varchar(255),
    scope_definition_id_ varchar(255),
    retries_             integer,
    exception_stack_id_  varchar(64)
        constraint act_fk_job_exception
            references act_ge_bytearray,
    exception_msg_       varchar(4000),
    duedate_             timestamp,
    repeat_              varchar(255),
    handler_type_        varchar(255),
    handler_cfg_         varchar(4000),
    custom_values_id_    varchar(64)
        constraint act_fk_job_custom_values
            references act_ge_bytearray,
    create_time_         timestamp,
    tenant_id_           varchar(255) default ''::character varying
);


create
index act_idx_job_exception_stack_id
    on act_ru_job (exception_stack_id_);

create
index act_idx_job_custom_values_id
    on act_ru_job (custom_values_id_);

create
index act_idx_job_scope
    on act_ru_job (scope_id_, scope_type_);

create
index act_idx_job_sub_scope
    on act_ru_job (sub_scope_id_, scope_type_);

create
index act_idx_job_scope_def
    on act_ru_job (scope_definition_id_, scope_type_);

create
index act_idx_job_execution_id
    on act_ru_job (execution_id_);

create
index act_idx_job_process_instance_id
    on act_ru_job (process_instance_id_);

create
index act_idx_job_proc_def_id
    on act_ru_job (proc_def_id_);

create table if not exists act_ru_timer_job
(
    id_                  varchar(64)  not null
        constraint act_ru_timer_job_pkey
            primary key,
    rev_                 integer,
    type_                varchar(255) not null,
    lock_exp_time_       timestamp,
    lock_owner_          varchar(255),
    exclusive_           boolean,
    execution_id_        varchar(64)
        constraint act_fk_timer_job_execution
            references act_ru_execution,
    process_instance_id_ varchar(64)
        constraint act_fk_timer_job_process_instance
            references act_ru_execution,
    proc_def_id_         varchar(64)
        constraint act_fk_timer_job_proc_def
            references act_re_procdef,
    element_id_          varchar(255),
    element_name_        varchar(255),
    scope_id_            varchar(255),
    sub_scope_id_        varchar(255),
    scope_type_          varchar(255),
    scope_definition_id_ varchar(255),
    retries_             integer,
    exception_stack_id_  varchar(64)
        constraint act_fk_timer_job_exception
            references act_ge_bytearray,
    exception_msg_       varchar(4000),
    duedate_             timestamp,
    repeat_              varchar(255),
    handler_type_        varchar(255),
    handler_cfg_         varchar(4000),
    custom_values_id_    varchar(64)
        constraint act_fk_timer_job_custom_values
            references act_ge_bytearray,
    create_time_         timestamp,
    tenant_id_           varchar(255) default ''::character varying
);


create
index act_idx_timer_job_exception_stack_id
    on act_ru_timer_job (exception_stack_id_);

create
index act_idx_timer_job_custom_values_id
    on act_ru_timer_job (custom_values_id_);

create
index act_idx_tjob_scope
    on act_ru_timer_job (scope_id_, scope_type_);

create
index act_idx_tjob_sub_scope
    on act_ru_timer_job (sub_scope_id_, scope_type_);

create
index act_idx_tjob_scope_def
    on act_ru_timer_job (scope_definition_id_, scope_type_);

create
index act_idx_timer_job_execution_id
    on act_ru_timer_job (execution_id_);

create
index act_idx_timer_job_process_instance_id
    on act_ru_timer_job (process_instance_id_);

create
index act_idx_timer_job_proc_def_id
    on act_ru_timer_job (proc_def_id_);

create table if not exists act_ru_suspended_job
(
    id_                  varchar(64)  not null
        constraint act_ru_suspended_job_pkey
            primary key,
    rev_                 integer,
    type_                varchar(255) not null,
    exclusive_           boolean,
    execution_id_        varchar(64)
        constraint act_fk_suspended_job_execution
            references act_ru_execution,
    process_instance_id_ varchar(64)
        constraint act_fk_suspended_job_process_instance
            references act_ru_execution,
    proc_def_id_         varchar(64)
        constraint act_fk_suspended_job_proc_def
            references act_re_procdef,
    element_id_          varchar(255),
    element_name_        varchar(255),
    scope_id_            varchar(255),
    sub_scope_id_        varchar(255),
    scope_type_          varchar(255),
    scope_definition_id_ varchar(255),
    retries_             integer,
    exception_stack_id_  varchar(64)
        constraint act_fk_suspended_job_exception
            references act_ge_bytearray,
    exception_msg_       varchar(4000),
    duedate_             timestamp,
    repeat_              varchar(255),
    handler_type_        varchar(255),
    handler_cfg_         varchar(4000),
    custom_values_id_    varchar(64)
        constraint act_fk_suspended_job_custom_values
            references act_ge_bytearray,
    create_time_         timestamp,
    tenant_id_           varchar(255) default ''::character varying
);


create
index act_idx_suspended_job_exception_stack_id
    on act_ru_suspended_job (exception_stack_id_);

create
index act_idx_suspended_job_custom_values_id
    on act_ru_suspended_job (custom_values_id_);

create
index act_idx_sjob_scope
    on act_ru_suspended_job (scope_id_, scope_type_);

create
index act_idx_sjob_sub_scope
    on act_ru_suspended_job (sub_scope_id_, scope_type_);

create
index act_idx_sjob_scope_def
    on act_ru_suspended_job (scope_definition_id_, scope_type_);

create
index act_idx_suspended_job_execution_id
    on act_ru_suspended_job (execution_id_);

create
index act_idx_suspended_job_process_instance_id
    on act_ru_suspended_job (process_instance_id_);

create
index act_idx_suspended_job_proc_def_id
    on act_ru_suspended_job (proc_def_id_);

create table if not exists act_ru_deadletter_job
(
    id_                  varchar(64)  not null
        constraint act_ru_deadletter_job_pkey
            primary key,
    rev_                 integer,
    type_                varchar(255) not null,
    exclusive_           boolean,
    execution_id_        varchar(64)
        constraint act_fk_deadletter_job_execution
            references act_ru_execution,
    process_instance_id_ varchar(64)
        constraint act_fk_deadletter_job_process_instance
            references act_ru_execution,
    proc_def_id_         varchar(64)
        constraint act_fk_deadletter_job_proc_def
            references act_re_procdef,
    element_id_          varchar(255),
    element_name_        varchar(255),
    scope_id_            varchar(255),
    sub_scope_id_        varchar(255),
    scope_type_          varchar(255),
    scope_definition_id_ varchar(255),
    exception_stack_id_  varchar(64)
        constraint act_fk_deadletter_job_exception
            references act_ge_bytearray,
    exception_msg_       varchar(4000),
    duedate_             timestamp,
    repeat_              varchar(255),
    handler_type_        varchar(255),
    handler_cfg_         varchar(4000),
    custom_values_id_    varchar(64)
        constraint act_fk_deadletter_job_custom_values
            references act_ge_bytearray,
    create_time_         timestamp,
    tenant_id_           varchar(255) default ''::character varying
);


create
index act_idx_deadletter_job_exception_stack_id
    on act_ru_deadletter_job (exception_stack_id_);

create
index act_idx_deadletter_job_custom_values_id
    on act_ru_deadletter_job (custom_values_id_);

create
index act_idx_djob_scope
    on act_ru_deadletter_job (scope_id_, scope_type_);

create
index act_idx_djob_sub_scope
    on act_ru_deadletter_job (sub_scope_id_, scope_type_);

create
index act_idx_djob_scope_def
    on act_ru_deadletter_job (scope_definition_id_, scope_type_);

create
index act_idx_deadletter_job_execution_id
    on act_ru_deadletter_job (execution_id_);

create
index act_idx_deadletter_job_process_instance_id
    on act_ru_deadletter_job (process_instance_id_);

create
index act_idx_deadletter_job_proc_def_id
    on act_ru_deadletter_job (proc_def_id_);

create
index act_idx_exec_buskey
    on act_ru_execution (business_key_);

create
index act_idx_exe_root
    on act_ru_execution (root_proc_inst_id_);

create
index act_idx_exe_procinst
    on act_ru_execution (proc_inst_id_);

create
index act_idx_exe_parent
    on act_ru_execution (parent_id_);

create
index act_idx_exe_super
    on act_ru_execution (super_exec_);

create
index act_idx_exe_procdef
    on act_ru_execution (proc_def_id_);

create table if not exists act_evt_log
(
    log_nr_       serial    not null
        constraint act_evt_log_pkey
            primary key,
    type_         varchar(64),
    proc_def_id_  varchar(64),
    proc_inst_id_ varchar(64),
    execution_id_ varchar(64),
    task_id_      varchar(64),
    time_stamp_   timestamp not null,
    user_id_      varchar(255),
    data_         bytea,
    lock_owner_   varchar(255),
    lock_time_    timestamp,
    is_processed_ smallint default 0
);


create table if not exists act_procdef_info
(
    id_           varchar(64) not null
        constraint act_procdef_info_pkey
            primary key,
    proc_def_id_  varchar(64) not null
        constraint act_uniq_info_procdef
            unique
        constraint act_fk_info_procdef
            references act_re_procdef,
    rev_          integer,
    info_json_id_ varchar(64)
        constraint act_fk_info_json_ba
            references act_ge_bytearray
);


create
index act_idx_procdef_info_json
    on act_procdef_info (info_json_id_);

create
index act_idx_procdef_info_proc
    on act_procdef_info (proc_def_id_);

create table if not exists act_ru_actinst
(
    id_                varchar(64)  not null
        constraint act_ru_actinst_pkey
            primary key,
    rev_               integer      default 1,
    proc_def_id_       varchar(64)  not null,
    proc_inst_id_      varchar(64)  not null,
    execution_id_      varchar(64)  not null,
    act_id_            varchar(255) not null,
    task_id_           varchar(64),
    call_proc_inst_id_ varchar(64),
    act_name_          varchar(255),
    act_type_          varchar(255) not null,
    assignee_          varchar(255),
    start_time_        timestamp    not null,
    end_time_          timestamp,
    duration_          bigint,
    delete_reason_     varchar(4000),
    tenant_id_         varchar(255) default ''::character varying
);


create
index act_idx_ru_acti_start
    on act_ru_actinst (start_time_);

create
index act_idx_ru_acti_end
    on act_ru_actinst (end_time_);

create
index act_idx_ru_acti_proc
    on act_ru_actinst (proc_inst_id_);

create
index act_idx_ru_acti_proc_act
    on act_ru_actinst (proc_inst_id_, act_id_);

create
index act_idx_ru_acti_exec
    on act_ru_actinst (execution_id_);

create
index act_idx_ru_acti_exec_act
    on act_ru_actinst (execution_id_, act_id_);

create table if not exists act_hi_procinst
(
    id_                        varchar(64) not null
        constraint act_hi_procinst_pkey
            primary key,
    rev_                       integer      default 1,
    proc_inst_id_              varchar(64) not null
        constraint act_hi_procinst_proc_inst_id__key
            unique,
    business_key_              varchar(255),
    proc_def_id_               varchar(64) not null,
    start_time_                timestamp   not null,
    end_time_                  timestamp,
    duration_                  bigint,
    start_user_id_             varchar(255),
    start_act_id_              varchar(255),
    end_act_id_                varchar(255),
    super_process_instance_id_ varchar(64),
    delete_reason_             varchar(4000),
    tenant_id_                 varchar(255) default ''::character varying,
    name_                      varchar(255),
    callback_id_               varchar(255),
    callback_type_             varchar(255),
    reference_id_              varchar(255),
    reference_type_            varchar(255)
);

create
index act_idx_hi_pro_inst_end
    on act_hi_procinst (end_time_);

create
index act_idx_hi_pro_i_buskey
    on act_hi_procinst (business_key_);

create table if not exists act_hi_actinst
(
    id_                varchar(64)  not null
        constraint act_hi_actinst_pkey
            primary key,
    rev_               integer      default 1,
    proc_def_id_       varchar(64)  not null,
    proc_inst_id_      varchar(64)  not null,
    execution_id_      varchar(64)  not null,
    act_id_            varchar(255) not null,
    task_id_           varchar(64),
    call_proc_inst_id_ varchar(64),
    act_name_          varchar(255),
    act_type_          varchar(255) not null,
    assignee_          varchar(255),
    start_time_        timestamp    not null,
    end_time_          timestamp,
    duration_          bigint,
    delete_reason_     varchar(4000),
    tenant_id_         varchar(255) default ''::character varying
);


create
index act_idx_hi_act_inst_start
    on act_hi_actinst (start_time_);

create
index act_idx_hi_act_inst_end
    on act_hi_actinst (end_time_);

create
index act_idx_hi_act_inst_procinst
    on act_hi_actinst (proc_inst_id_, act_id_);

create
index act_idx_hi_act_inst_exec
    on act_hi_actinst (execution_id_, act_id_);

create table if not exists act_hi_detail
(
    id_           varchar(64)  not null
        constraint act_hi_detail_pkey
            primary key,
    type_         varchar(255) not null,
    proc_inst_id_ varchar(64),
    execution_id_ varchar(64),
    task_id_      varchar(64),
    act_inst_id_  varchar(64),
    name_         varchar(255) not null,
    var_type_     varchar(64),
    rev_          integer,
    time_         timestamp    not null,
    bytearray_id_ varchar(64),
    double_       double precision,
    long_         bigint,
    text_         varchar(4000),
    text2_        varchar(4000)
);


create
index act_idx_hi_detail_proc_inst
    on act_hi_detail (proc_inst_id_);

create
index act_idx_hi_detail_act_inst
    on act_hi_detail (act_inst_id_);

create
index act_idx_hi_detail_time
    on act_hi_detail (time_);

create
index act_idx_hi_detail_name
    on act_hi_detail (name_);

create
index act_idx_hi_detail_task_id
    on act_hi_detail (task_id_);

create table if not exists act_hi_comment
(
    id_           varchar(64) not null
        constraint act_hi_comment_pkey
            primary key,
    type_         varchar(255),
    time_         timestamp   not null,
    user_id_      varchar(255),
    task_id_      varchar(64),
    proc_inst_id_ varchar(64),
    action_       varchar(255),
    message_      varchar(4000),
    full_msg_     bytea
);


create table if not exists act_hi_attachment
(
    id_           varchar(64) not null
        constraint act_hi_attachment_pkey
            primary key,
    rev_          integer,
    user_id_      varchar(255),
    name_         varchar(255),
    description_  varchar(4000),
    type_         varchar(255),
    task_id_      varchar(64),
    proc_inst_id_ varchar(64),
    url_          varchar(4000),
    content_id_   varchar(64),
    time_         timestamp
);