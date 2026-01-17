-- PAGINATION

DO $$
DECLARE
    i INT := 0;
    numberOfUsers INT := 10000;  -- Specify the number of users to insert
BEGIN
    WHILE i < numberOfUsers LOOP
        INSERT INTO userchans (id, username, email, password, is_active)
        VALUES (
            gen_random_uuid(),
            'user_' || i,
            'user_' || i || '@example.com',
            'password' || i,
            TRUE
        );
        i := i + 1;
    END LOOP;
END $$;



