INSERT INTO app_user (username, first_name, last_name)
VALUES
('main_user', 'Main', 'User'),
('update_user', 'Update', 'User'),
('delete_user', 'Delete', 'User'),
('household_member_create_user_1', 'Household Member Create 1', 'User'),
('household_member_create_user_2', 'Household Member Create 2', 'User'),
('account_create_user', 'Account Create', 'User');

INSERT INTO household (name)
VALUES
('main_household'),
('update_household'),
('delete_household'),
('household_member_create_household'),
('account_create_household');

INSERT INTO household_member (household_id, user_id)
VALUES
(5, 6),
(1, 2);

INSERT INTO institution (name)
VALUES
('my_bank_institution'),
('update_institution'),
('delete_institution'),
('account_create_institution');
