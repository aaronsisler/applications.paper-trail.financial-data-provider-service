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
* A financial institution
*/
CREATE TABLE IF NOT EXISTS institution (
  institution_id integer NOT NULL,
  name varchar(45) NOT NULL,
  PRIMARY KEY (institution_id)
);

/*
* A user’s association with an institution
*/
CREATE TABLE IF NOT EXISTS member (
  member_id integer NOT NULL,
  PRIMARY KEY (member_id),
  user_id integer REFERENCES app_user (user_id),
  institution_id integer REFERENCES institution (institution_id)
);

/*
* An account held at an institution which is tied to a member
*/
CREATE TABLE IF NOT EXISTS account (
  account_id integer NOT NULL,
  name varchar(45) NOT NULL,
  PRIMARY KEY (account_id),
  member_id integer REFERENCES member (member_id),
  institution_id integer REFERENCES institution (institution_id)
);

/*
* An instance of money that moves in or out of an account
*/
CREATE TABLE IF NOT EXISTS act_transaction (
  act_transaction_id integer NOT NULL,
  amount numeric(12,2) NOT NULL,
  description varchar(150) NOT NULL,
  PRIMARY KEY (act_transaction_id),
  account_id integer REFERENCES account (account_id)
);