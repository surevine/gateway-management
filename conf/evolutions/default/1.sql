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

create table outbound_project (
  id                        bigint not null,
  display_name              varchar(255),
  project_key               varchar(255),
  repository_slug           varchar(255),
  constraint pk_outbound_project primary key (id))
;


create table destination_outbound_project (
  destination_id                 bigint not null,
  outbound_project_id            bigint not null,
  constraint pk_destination_outbound_project primary key (destination_id, outbound_project_id))
;
create sequence destination_seq;

create sequence inbound_project_seq;

create sequence outbound_project_seq;




alter table destination_outbound_project add constraint fk_destination_outbound_proje_01 foreign key (destination_id) references destination (id) on delete restrict on update restrict;

alter table destination_outbound_project add constraint fk_destination_outbound_proje_02 foreign key (outbound_project_id) references outbound_project (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists destination;

drop table if exists destination_outbound_project;

drop table if exists inbound_project;

drop table if exists outbound_project;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists destination_seq;

drop sequence if exists inbound_project_seq;

drop sequence if exists outbound_project_seq;

