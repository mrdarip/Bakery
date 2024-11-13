package com.mrdarip.bakery.data.DAO;

import com.mrdarip.bakery.data.entity.Plate;
import javafx.scene.control.Alert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static java.lang.System.exit;

public class MySqlPlateDAO extends PlateDao {

    Connection conexion;
    Statement st;
    ResultSet rs;

    public MySqlPlateDAO() {
        super();

        try {
            conexion = this.getConnection();
            if (conexion != null) {
                this.st = conexion.createStatement();
            }
        } catch (IOException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public Plate getPlate(int id) {
        if (conexion != null && id != 0) {
            String query = "SELECT * FROM Plate WHERE idPlate = ?";
            try {
                Plate plate = null;
                PreparedStatement preparedStatement = this.conexion.prepareStatement(query);
                preparedStatement.setInt(1, id);

                rs = preparedStatement.executeQuery();

                plate = new Plate(rs.getInt("idPlate"), rs.getString("plateName"), rs.getInt("valoration"), this.getPlate(rs.getInt("idRequiredPlate")), rs.getString("uri_preview"));

                return plate;
            } catch (SQLException e) {
                System.out.println("SQL exception when trying to getPlate: " + e.getMessage());
            }

        }
        return null;
    }

    @Override
    public List<Plate> getPlatesPage(int page, int orderBy) {
        List<Plate> output = new ArrayList();
        int pageSize = 4;
        if (conexion != null) {
            String query = "SELECT * FROM Plate LIMIT ? OFFSET ?";
            try {
                PreparedStatement preparedStatement = this.conexion.prepareStatement(query);
                preparedStatement.setInt(1, pageSize);
                preparedStatement.setInt(2, pageSize * page);

                rs = preparedStatement.executeQuery();
                Plate plate;
                while (rs.next()) {
                    plate = new Plate(rs.getInt("idPlate"), rs.getString("plateName"), rs.getInt("valoration"), this.getPlate(rs.getInt("idRequiredPlate")), rs.getString("uri_preview"));
                    output.add(plate);
                }
            } catch (SQLException e) {
                System.out.println("SQL exception when trying to getPlatesPage: " + e.getMessage());
            }
            return output;
        }
        return null;
    }

    @Override
    public Plate upsert(Plate plate) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Connection getConnection() throws IOException {
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

        InputStream input = getClass().getClassLoader().getResourceAsStream("bbdd.properties");
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
