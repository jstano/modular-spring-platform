package com.stano.domain_jpa_test;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

class EmbeddedPostgresTest {

    @Test
    void shouldBeAbleToStartAnEmbeddedPostgresDatabase() throws SQLException, IOException {
        var embeddedPostgres = EmbeddedPostgres.builder().start();

        try {
            var database = embeddedPostgres.getDatabase("postgres", "test");

            try (var connection = embeddedPostgres.getPostgresDatabase().getConnection();
                 var statement = connection.createStatement()) {
                statement.execute("create database test");
            }

            try (var connection = database.getConnection();
                 var statement = connection.createStatement()) {
                statement.execute("create table test (id int,name text)");
                statement.execute("insert into test (id,name) values (1,'One')");
                statement.execute("insert into test (id,name) values (2,'Two')");
            }

            assertThat(true).isTrue();
        } finally {
            embeddedPostgres.close();
        }
    }
}
