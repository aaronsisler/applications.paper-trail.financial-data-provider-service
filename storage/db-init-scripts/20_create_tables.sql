/*
* A user of the application
*/
CREATE TABLE IF NOT EXISTS app_user (
  id integer primary key generated always as identity,
  username varchar(45) NOT NULL,
  first_name varchar(45) NOT NULL,
  last_name varchar(45) NOT NULL
);

/*
* An entity that represents a group of users
*/
CREATE TABLE IF NOT EXISTS household (
  id integer primary key generated always as identity,
  name varchar(45) NOT NULL
);

/*
* An association between a household and a user
*/
CREATE TABLE IF NOT EXISTS household_member (
  id integer primary key generated always as identity,
  household_id integer REFERENCES household (id),
  user_id integer REFERENCES app_user (id)
);

/*
* A financial institution
*/
CREATE TABLE IF NOT EXISTS institution (
  id integer primary key generated always as identity,
  name varchar(45) NOT NULL
);

/*
* An account held at an institution which is tied to a household member
*/
CREATE TABLE IF NOT EXISTS account (
  id integer primary key generated always as identity,
  name varchar(45) NOT NULL,
  institution_id integer REFERENCES institution (id),
  household_member_id integer REFERENCES household_member (id)
);

/*
* An instance of money that moves in or out of an account
*/
CREATE TABLE IF NOT EXISTS account_transaction (
  id integer primary key generated always as identity,
  amount numeric(12,2) NOT NULL,
  description varchar(150) NOT NULL,
  account_id integer REFERENCES account (id)
);