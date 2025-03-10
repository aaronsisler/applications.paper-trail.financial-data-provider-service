/*
* A user of the application
*/
CREATE TABLE IF NOT EXISTS app_user (
  user_id integer primary key generated always as identity,
  username varchar(45) NOT NULL,
  first_name varchar(45) NOT NULL,
  last_name varchar(45) NOT NULL
);

/*
* An entity that represents a group of users
*/
CREATE TABLE IF NOT EXISTS household (
  household_id integer primary key generated always as identity,
  name varchar(45) NOT NULL
);

/*
* An association between a household and a user
*/
CREATE TABLE IF NOT EXISTS household_member (
  household_member_id integer primary key generated always as identity,
  household_id integer REFERENCES household (household_id),
  user_id integer REFERENCES app_user (user_id)
);

/*
* A financial institution
*/
CREATE TABLE IF NOT EXISTS institution (
  institution_id integer primary key generated always as identity,
  name varchar(45) NOT NULL
);

/*
* An account held at an institution which is tied to a household member
*/
CREATE TABLE IF NOT EXISTS account (
  account_id integer primary key generated always as identity,
  name varchar(45) NOT NULL,
  institution_id integer REFERENCES institution (institution_id),
  household_member_id integer REFERENCES household_member (household_member_id)
);

/*
* An instance of money that moves in or out of an account
*/
CREATE TABLE IF NOT EXISTS account_transaction (
  account_transaction_id integer primary key generated always as identity,
  amount numeric(12,2) NOT NULL,
  description varchar(150) NOT NULL,
  account_id integer REFERENCES account (account_id)
);