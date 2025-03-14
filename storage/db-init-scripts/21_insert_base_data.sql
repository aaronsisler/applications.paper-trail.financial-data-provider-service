INSERT INTO app_user (username, first_name, last_name)
VALUES
('aaron_sisler', 'Aaron', 'Sisler'),
('bridget_sisler', 'Bridget', 'Sisler');

INSERT INTO household (name)
VALUES
('sisler_household');

INSERT INTO household_member (household_id, user_id)
VALUES
(1,1),
(1,2);
