CREATE OR REPLACE FUNCTION read_users(refcursor) RETURNS refcursor AS $$
BEGIN
   OPEN $1 FOR SELECT user_id, username, first_name, last_name FROM app_user;
   RETURN $1;
END;
$$ LANGUAGE plpgsql;

-- BEGIN;
-- SELECT read_users('my_cursor');
-- FETCH ALL IN my_cursor;
-- COMMIT;

-- select * from app_user au;

