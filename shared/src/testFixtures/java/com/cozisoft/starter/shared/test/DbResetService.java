package com.cozisoft.starter.shared.test;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
public class DbResetService {
    private final JdbcTemplate jdbcTemplate;

    public void resetAll() {
        jdbcTemplate.execute("SET session_replication_role = 'replica'");
        jdbcTemplate.execute("""
                DO $$ DECLARE
                    r RECORD;
                BEGIN
                    FOR r IN (
                        SELECT schemaname, tablename
                        FROM pg_tables
                        WHERE schemaname LIKE '%_service'
                    ) LOOP
                        EXECUTE 'TRUNCATE TABLE '
                            || quote_ident(r.schemaname) || '.' || quote_ident(r.tablename)
                            || ' CASCADE';
                    END LOOP;
                END $$;
                """);
        jdbcTemplate.execute("SET session_replication_role = 'origin'");
    }
}
