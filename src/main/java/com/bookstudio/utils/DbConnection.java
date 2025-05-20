package com.bookstudio.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    public static Connection getConexion() {
        Connection con = null;
        try {
            Class.forName(DRIVER);

            String host = System.getenv("MYSQL_HOST");
            String port = System.getenv("MYSQL_PORT");
            String database = System.getenv("MYSQL_DATABASE");
            String user = System.getenv("MYSQL_USER");
            String password = System.getenv("MYSQL_PASSWORD");

            String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&useTimezone=true&serverTimezone=UTC";

            con = DriverManager.getConnection(url, user, password);

        } catch (ClassNotFoundException e) {
            System.out.println("Error: Driver not installed! " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error: Database connection failed! " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: General exception: " + e.getMessage());
        }

        return con;
    }
}
