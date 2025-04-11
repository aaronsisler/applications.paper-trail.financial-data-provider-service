INSERT INTO app_user (username, first_name, last_name)
VALUES
('main_user', 'Main', 'User'), -- 1
('update_user', 'Update', 'User'), -- 2
('delete_user', 'Delete', 'User'), -- 3
('household_member_create_user_1', 'Household Member Create 1', 'User'), -- 4
('household_member_create_user_2', 'Household Member Create 2', 'User'), -- 5
('account_create_user_1', 'Account Create 1', 'User'), -- 6
('account_create_user_2', 'Account Create 2', 'User'), -- 7
('account_transaction_create_user', 'Account Transaction Create', 'User'), -- 8
('account_transaction_update_user', 'Account Transaction Update', 'User'), -- 9
('account_transaction_delete_user', 'Account Transaction Delete', 'User') -- 10
;

INSERT INTO household (name)
VALUES
('main_household'), -- 1
('update_household'), -- 2
('delete_household'), -- 3
('household_member_create_household'), -- 4
('account_create_household'), -- 5
('account_transaction_create_household'), -- 6
('account_transaction_update_household'), -- 7
('account_transaction_delete_household') -- 8
;

INSERT INTO household_member (household_id, user_id)
VALUES
(5, 6), -- 1 account_create with user 1
(5, 7), -- 2 account_create with user 2
(6, 8), -- 3 account_transaction_create with single user
(7, 9), -- 4 account_transaction_update with single user
(8, 10) -- 5 account_transaction_delete with single user
;

INSERT INTO institution (name)
VALUES
('my_bank_institution'), -- 1
('update_institution'), -- 2
('delete_institution'), -- 3
('account_create_institution'), -- 4
('account_transaction_create_institution'), -- 5
('account_transaction_update_institution'), -- 6
('account_transaction_delete_institution') -- 7
;

INSERT INTO account (institution_id, household_member_id, name, nickname)
VALUES
(5, 3, 'account_transaction_create_name_1', 'account_transaction_create_nickname_1'), --1
(5, 3, 'account_transaction_create_name_2', 'account_transaction_create_nickname_2'), --2
(5, 3, 'account_transaction_update_name', 'account_transaction_update_nickname'), --3
(5, 3, 'account_transaction_delete_name', 'account_transaction_delete_nickname') --4
;

INSERT INTO account_transaction (account_id, amount, description, transaction_date)
VALUES
(3, 123, 'account_transaction_update_description', '2025-04-13'), --1
(4, 456, 'account_transaction_delete_description', '2025-04-09') --2
;