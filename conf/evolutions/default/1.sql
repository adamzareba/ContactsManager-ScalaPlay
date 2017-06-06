# --- !Ups
create table contacts (
  id            bigint not null auto_increment,
  name          varchar(255),
  emailAddress  varchar(255),
  primary key (id)
);

insert into contacts(name, emailAddress) values('John Norton', 'john@test.com');
insert into contacts(name, emailAddress) values('Tom Jones', 'tom@test.com');
insert into contacts(name, emailAddress) values('Andrew Black', 'andrew@test.com');
insert into contacts(name, emailAddress) values('George White', 'george@test.com');

# --- !Downs
drop table if exists contacts;

