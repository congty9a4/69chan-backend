
DO $$
    DECLARE
        i INT := 0;
        numberOfUsers INT := 10000;  -- Specify the number of users to insert
    BEGIN
        WHILE i < numberOfUsers LOOP
                DELETE FROM userchans
                where username = 'user_' || i;
                i := i + 1;
            END LOOP;
    END $$;
