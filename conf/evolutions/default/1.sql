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

create table inbound_issue_project (
  id                        bigint not null,
  source_organisation       varchar(255),
  project_key               varchar(255),
  constraint pk_inbound_issue_project primary key (id))
;

create table inbound_project (
  id                        bigint not null,
  source_organisation       varchar(255),
  project_key               varchar(255),
  repository_slug           varchar(255),
  constraint pk_inbound_project primary key (id))
;

create table outbound_issue_project (
  id                        bigint not null,
  display_name              varchar(255),
  project_key               varchar(255),
  constraint uq_outbound_issue_project_projec unique (project_key),
  constraint pk_outbound_issue_project primary key (id))
;

create table outbound_project (
  id                        bigint not null,
  display_name              varchar(255),
  project_key               varchar(255),
  repository_slug           varchar(255),
  constraint pk_outbound_project primary key (id))
;

create table repository (
  id                        bigint not null,
  repo_type                 varchar(5),
  identifier                varchar(255),
  constraint ck_repository_repo_type check (repo_type in ('SCM','ISSUE')),
  constraint pk_repository primary key (id))
;


create table destination_outbound_project (
  destination_id                 bigint not null,
  outbound_project_id            bigint not null,
  constraint pk_destination_outbound_project primary key (destination_id, outbound_project_id))
;

create table destination_outbound_issue_proje (
  destination_id                 bigint not null,
  outbound_issue_project_id      bigint not null,
  constraint pk_destination_outbound_issue_proje primary key (destination_id, outbound_issue_project_id))
;

create table destination_repository (
  destination_id                 bigint not null,
  repository_id                  bigint not null,
  constraint pk_destination_repository primary key (destination_id, repository_id))
;
create sequence destination_seq;

create sequence inbound_issue_project_seq;

create sequence inbound_project_seq;

create sequence outbound_issue_project_seq;

create sequence outbound_project_seq;

create sequence repository_seq;




alter table destination_outbound_project add constraint fk_destination_outbound_proje_01 foreign key (destination_id) references destination (id);

alter table destination_outbound_project add constraint fk_destination_outbound_proje_02 foreign key (outbound_project_id) references outbound_project (id);

alter table destination_outbound_issue_proje add constraint fk_destination_outbound_issue_01 foreign key (destination_id) references destination (id);

alter table destination_outbound_issue_proje add constraint fk_destination_outbound_issue_02 foreign key (outbound_issue_project_id) references outbound_issue_project (id);

alter table destination_repository add constraint fk_destination_repository_des_01 foreign key (destination_id) references destination (id);

alter table destination_repository add constraint fk_destination_repository_rep_02 foreign key (repository_id) references repository (id);

# --- !Downs

drop table if exists destination cascade;

drop table if exists destination_outbound_project cascade;

drop table if exists destination_outbound_issue_proje cascade;

drop table if exists destination_repository cascade;

drop table if exists inbound_issue_project cascade;

drop table if exists inbound_project cascade;

drop table if exists outbound_issue_project cascade;

drop table if exists outbound_project cascade;

drop table if exists repository cascade;

drop sequence if exists destination_seq;

drop sequence if exists inbound_issue_project_seq;

drop sequence if exists inbound_project_seq;

drop sequence if exists outbound_issue_project_seq;

drop sequence if exists outbound_project_seq;

drop sequence if exists repository_seq;

