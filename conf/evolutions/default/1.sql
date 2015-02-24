# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table destination (
  id                        bigint not null,
  name                      varchar(255),
  url                       varchar(255),
  source_key                varchar(255),
  constraint uq_destination_url unique (url),
  constraint pk_destination primary key (id))
;

create table federation_configuration (
  id                        bigint not null,
  destination_id            bigint,
  repository_id             bigint,
  inbound_enabled           boolean,
  outbound_enabled          boolean,
  constraint pk_federation_configuration primary key (id))
;

create table repository (
  id                        bigint not null,
  repo_type                 varchar(5),
  identifier                varchar(255),
  constraint ck_repository_repo_type check (repo_type in ('SCM','ISSUE')),
  constraint pk_repository primary key (id))
;

create sequence destination_seq;

create sequence federation_configuration_seq;

create sequence repository_seq;

alter table federation_configuration add constraint fk_federation_configuration_de_1 foreign key (destination_id) references destination (id) on delete restrict on update restrict;
create index ix_federation_configuration_de_1 on federation_configuration (destination_id);
alter table federation_configuration add constraint fk_federation_configuration_re_2 foreign key (repository_id) references repository (id) on delete restrict on update restrict;
create index ix_federation_configuration_re_2 on federation_configuration (repository_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists destination;

drop table if exists federation_configuration;

drop table if exists repository;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists destination_seq;

drop sequence if exists federation_configuration_seq;

drop sequence if exists repository_seq;

