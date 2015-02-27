# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table federation_configuration (
  id                        bigint not null,
  partner_id                bigint,
  repository_id             bigint,
  inbound_enabled           boolean,
  outbound_enabled          boolean,
  constraint pk_federation_configuration primary key (id))
;

create table partner (
  id                        bigint not null,
  name                      varchar(255),
  url                       varchar(255),
  source_key                varchar(255),
  constraint uq_partner_url unique (url),
  constraint uq_partner_source_key unique (source_key),
  constraint pk_partner primary key (id))
;

create table repository (
  id                        bigint not null,
  repo_type                 varchar(5),
  identifier                varchar(255),
  constraint ck_repository_repo_type check (repo_type in ('SCM','ISSUE')),
  constraint pk_repository primary key (id))
;

create sequence federation_configuration_seq;

create sequence partner_seq;

create sequence repository_seq;

alter table federation_configuration add constraint fk_federation_configuration_pa_1 foreign key (partner_id) references partner (id);
create index ix_federation_configuration_pa_1 on federation_configuration (partner_id);
alter table federation_configuration add constraint fk_federation_configuration_re_2 foreign key (repository_id) references repository (id);
create index ix_federation_configuration_re_2 on federation_configuration (repository_id);



# --- !Downs

drop table if exists federation_configuration cascade;

drop table if exists partner cascade;

drop table if exists repository cascade;

drop sequence if exists federation_configuration_seq;

drop sequence if exists partner_seq;

drop sequence if exists repository_seq;

