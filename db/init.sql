CREATE DATABASE IF NOT EXISTS phonebook;

CREATE TABLE IF NOT EXISTS phonebook.User (
  user_id INTEGER(10) NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(40) NOT NULL,
  last_name VARCHAR(40) NOT NULL,
  password VARCHAR (128) NOT NULL,
  PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS phonebook.Contact (
  contact_id INTEGER (10) NOT NULL AUTO_INCREMENT,
  user_id INTEGER(10) NOT NULL,
  first_name VARCHAR(40) NOT NULL,
  last_name VARCHAR(40) NOT NULL,
  PRIMARY KEY (contact_id)
);

CREATE TABLE IF NOT EXISTS phonebook.Phone (
  phone_id INTEGER (10) NOT NULL AUTO_INCREMENT,
  contact_id INTEGER(10) NOT NULL,
  phone VARCHAR(40) NOT NULL,
  PRIMARY KEY (phone_id)
);
