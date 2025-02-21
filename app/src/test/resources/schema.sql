/*
* A user of the application
*/
CREATE TABLE IF NOT EXISTS app_user (
  user_id integer NOT NULL,
  username varchar(45) NOT NULL,
  first_name varchar(45) NOT NULL,
  last_name varchar(45) NOT NULL,
  PRIMARY KEY (user_id)
);
