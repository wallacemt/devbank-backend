DO
$$
    BEGIN
        IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'devbank') THEN
            CREATE DATABASE devbank;
        END IF;
    END
$$;
