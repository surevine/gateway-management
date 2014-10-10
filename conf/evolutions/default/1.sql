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

create table project (
  id                        bigint not null,
  name                      varchar(255),
  url                       varchar(255),
  constraint uq_project_url unique (url),
  constraint pk_project primary key (id))
;


create table destination_project (
  destination_id                 bigint not null,
  project_id                     bigint not null,
  constraint pk_destination_project primary key (destination_id, project_id))
;
create sequence destination_seq;

create sequence project_seq;




alter table destination_project add constraint fk_destination_project_destin_01 foreign key (destination_id) references destination (id) on delete restrict on update restrict;

alter table destination_project add constraint fk_destination_project_projec_02 foreign key (project_id) references project (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists destination;

drop table if exists destination_project;

drop table if exists project;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists destination_seq;

drop sequence if exists project_seq;

