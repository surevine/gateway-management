# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table destination (
  id                        bigint not null,
  name                      varchar(255),
  url                       varchar(255),
  constraint uq_destination_url unique (url),
  constraint pk_destination primary key (id))
;

create table inbound_project (
  id                        bigint not null,
  source_organisation       varchar(255),
  project_key               varchar(255),
  repository_slug           varchar(255),
  constraint pk_inbound_project primary key (id))
;

create table project (
  id                        bigint not null,
  display_name              varchar(255),
  project_key               varchar(255),
  repository_slug           varchar(255),
  constraint pk_project primary key (id))
;


create table destination_project (
  destination_id                 bigint not null,
  project_id                     bigint not null,
  constraint pk_destination_project primary key (destination_id, project_id))
;
create sequence destination_seq;

create sequence inbound_project_seq;

create sequence project_seq;




alter table destination_project add constraint fk_destination_project_destin_01 foreign key (destination_id) references destination (id);

alter table destination_project add constraint fk_destination_project_projec_02 foreign key (project_id) references project (id);

# --- !Downs

drop table if exists destination cascade;

drop table if exists destination_project cascade;

drop table if exists inbound_project cascade;

drop table if exists project cascade;

drop sequence if exists destination_seq;

drop sequence if exists inbound_project_seq;

drop sequence if exists project_seq;

