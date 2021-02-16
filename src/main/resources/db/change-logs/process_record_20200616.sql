--修改processKey长度为varchar(64)
alter table "public".process_definition alter column process_key type varchar(64) using process_key::varchar(64);

alter table  "public".process_definition alter column process_key set not null;

alter table  "public".process_definition alter column process_key set default '';
