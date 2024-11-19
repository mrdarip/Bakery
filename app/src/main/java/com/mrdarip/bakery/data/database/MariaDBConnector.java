package com.mrdarip.bakery.data.database;

import javafx.scene.control.Alert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import static java.lang.System.exit;

public class MariaDBConnector {
    private static Connection connection;

    public static Connection getConnectionInstance(){
        if (connection == null) {
            try {
                connection = connect();
                if (connection != null) {
                    Statement st = connection.createStatement();
                }
            } catch (IOException | SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        return connection;
    }

    private static Connection connect() throws IOException {
        Properties properties = new Properties();
        String IP, PORT, BBDD, USER, PWD;

        try {
            InputStream input_ip = new FileInputStream("ip.properties");
            properties.load(input_ip);
            IP = (String) properties.get("IP");
        } catch (FileNotFoundException e) {
            System.out.println("ip.properties not found, using localhost as IP");
            IP = "localhost";
        }

        InputStream input = MariaDBConnector.class.getClassLoader().getResourceAsStream("bbdd.properties");
        if (input == null) {
            System.out.println("bbdd.properties not found");
            return null;
        } else {
            properties.load(input);

            PORT = (String) properties.get("PORT");
            BBDD = (String) properties.get("BBDD");
            USER = (String) properties.get("USER");
            PWD = (String) properties.get("PWD");

            Connection conn;
            try {
                conn = DriverManager.getConnection("jdbc:mariadb://" + IP + ":" + PORT + "/" + BBDD, USER, PWD);
                return conn;
            } catch (SQLException e) {
                System.out.println("Error SQL: " + e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("A connection error has occurred");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                exit(0);
                return null;
            }
        }
    }
}

