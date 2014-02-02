package org.denevell.droidnatch.uitests.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created by user on 31/01/14.
 */
public class TestUtils {

    public static void deleteDb() throws Exception {

        final String DBDriver = "org.postgresql.Driver";
        Class.forName(DBDriver);

        Properties connectionProps = new Properties();
        connectionProps.put("user", "denevell");
        connectionProps.put("password", "user");

        Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://" +
                        "10.0.2.2" +
                        ":5432" + "/testnatch",
                connectionProps);

        Statement statement = conn.createStatement();
        statement.execute("delete from thread_posts");
        statement.execute("delete from post_tags");
        statement.execute("delete from ThreadEntity");
        statement.execute("delete from PostEntity");
    }

}
