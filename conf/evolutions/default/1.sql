# --- !Ups
CREATE TABLE contacts (
  id            BIGINT NOT NULL AUTO_INCREMENT,
  name          VARCHAR(255),
  age           INT,
  emailAddress  VARCHAR(255),
  PRIMARY KEY (id)
);

INSERT INTO contacts(name, age, emailAddress) VALUES('John Norton', 37, 'john@test.com');
INSERT INTO contacts(name, age, emailAddress) VALUES('Tom Jones', 28, 'tom@test.com');
INSERT INTO contacts(name, age, emailAddress) VALUES('Andrew Black', 48, 'andrew@test.com');
INSERT INTO contacts(name, age, emailAddress) VALUES('George White', 63, 'george@test.com');

# --- !Downs
DROP TABLE IF EXISTS contacts;

