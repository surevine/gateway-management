# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table destination (
  id                        bigint not null,
  name                      varchar(255),
  url                       varchar(255),
  constraint pk_destination primary key (id))
;

create sequence destination_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists destination;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists destination_seq;

