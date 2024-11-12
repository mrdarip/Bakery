package com.mrdarip.bakery.data.DAO;

import com.mrdarip.bakery.data.entity.Plate;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.System.exit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javafx.scene.control.Alert;

public class MySqlPlateDAO extends PlateDao {
    Connection conexion;
    Statement st;
    ResultSet rs;

    public MySqlPlateDAO(){
        super();
        
        try {
            conexion = this.getConnection();
            if (conexion != null) {
                this.st = conexion.createStatement();
            }
        } catch (IOException | SQLException e) {
        }
    }

    @Override
    public Plate getPlate(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Plate> getPlatesPage(int page, int orderBy) {
        List<Plate> output = new ArrayList();
        int pageSize = 6;
        if (conexion != null) {
            String query = "SELECT * FROM Plate LIMIT ? OFFSET ?";
            try {
                PreparedStatement preparedStatement = this.conexion.prepareStatement(query);
                preparedStatement.setInt(1,pageSize);
                preparedStatement.setInt(2, pageSize * page);
                
                rs = preparedStatement.executeQuery();
                Plate plate;
                while (rs.next()) { //Se usan los identificadores propios en la BBDD (no es case sensitive). Revisar en phpmyadmin
                    plate = new Plate(rs.getInt("idPlate"), rs.getString("plateName"), rs.getInt("valoration"), (Plate) null,rs.getString("uri_preview"));
                    output.add(plate);
                }
            } catch (SQLException e) {
                System.out.println("Excepción SQL: " + e.getMessage());
            }
            return output;
        }
        return null;
    }

    @Override
    public Plate upsert(Plate plate) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public Connection getConnection() throws IOException {
        Properties properties = new Properties();
        String IP, PORT, BBDD, USER, PWD;
        
        try {
            InputStream input_ip = new FileInputStream("ip.properties");//archivo debe estar junto al jar
            properties.load(input_ip);
            IP = (String) properties.get("IP");
        } catch (FileNotFoundException e) {
            System.out.println("No se pudo encontrar el archivo de propiedades para IP, se establece localhost por defecto");
            IP = "localhost";
        }

        InputStream input = getClass().getClassLoader().getResourceAsStream("bbdd.properties");
        if (input == null) {
            System.out.println("No se pudo encontrar el archivo de propiedades");
            return null;
        } else {
            // Cargar las propiedades desde el archivo
            properties.load(input);
            // String IP = (String) properties.get("IP"); //Tiene sentido leerlo desde fuera del Jar por si cambiamos la IP, el resto no debería de cambiar
            //ni debería ser público
            PORT = (String) properties.get("PORT");//En vez de crear con new, lo crea por asignación + casting
            BBDD = (String) properties.get("BBDD");
            USER = (String) properties.get("USER");//USER de MARIADB en LAMP 
            PWD = (String) properties.get("PWD");//PWD de MARIADB en LAMP 

            Connection conn;
            try {               
                conn = DriverManager.getConnection("jdbc:mariadb://" + IP + ":" + PORT + "/" + BBDD, USER, PWD);
                return conn;
            } catch (SQLException e) {
                System.out.println("Error SQL: " + e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Ha ocurrido un error de conexión");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                exit(0);
                return null;
            }
        }
    }
}
