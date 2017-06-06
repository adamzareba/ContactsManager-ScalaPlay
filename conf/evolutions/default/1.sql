# --- !Ups
CREATE TABLE contacts (
  id            bigint not null auto_increment,
  name          varchar(255),
  emailAddress  varchar(255),
  primary key (id)
);

INSERT INTO contacts(name, emailAddress) VALUES('John Norton', 'john@test.com');
INSERT INTO contacts(name, emailAddress) VALUES('Tom Jones', 'tom@test.com');
INSERT INTO contacts(name, emailAddress) VALUES('Andrew Black', 'andrew@test.com');
INSERT INTO contacts(name, emailAddress) VALUES('George White', 'george@test.com');

# --- !Downs
DROP TABLE IF EXISTS contacts;

