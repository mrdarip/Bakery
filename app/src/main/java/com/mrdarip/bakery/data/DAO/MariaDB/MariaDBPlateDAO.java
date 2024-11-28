package com.mrdarip.bakery.data.DAO.MariaDB;

import com.mrdarip.bakery.data.DAO.PlateDao;
import com.mrdarip.bakery.data.database.MariaDBConnector;
import com.mrdarip.bakery.data.entity.Plate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MariaDBPlateDAO extends PlateDao {

    Connection connection;

    public MariaDBPlateDAO() {
        super();

        connection = MariaDBConnector.getConnectionInstance();
    }

    @Override
    public Plate getPlate(int id) {
        ResultSet rs;
        if (connection != null && id != 0) {
            String query = "SELECT * FROM Plate WHERE idPlate = ?";
            try {
                Plate plate = null;
                PreparedStatement preparedStatement = this.connection.prepareStatement(query);
                preparedStatement.setInt(1, id);

                rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    plate = plateFromResultSet(rs);
                }
                return plate;
            } catch (SQLException e) {
                System.out.println("SQL exception when trying to getPlate " + id + ": " + e.getMessage());
            }

        }
        return null;
    }

    @Override
    public List<Plate> getPlatesPage(int page, int orderBy) {
        ResultSet rs;
        List<Plate> output = new ArrayList<>();
        int pageSize = 25;
        if (connection != null) {
            String query = "SELECT * FROM Plate LIMIT ? OFFSET ?";
            try {
                PreparedStatement preparedStatement = this.connection.prepareStatement(query);
                preparedStatement.setInt(1, pageSize);
                preparedStatement.setInt(2, pageSize * page);

                rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    output.add(plateFromResultSet(rs));
                }
            } catch (SQLException e) {
                System.out.println("SQL exception when trying to getPlatesPage: " + e.getMessage());
            }
            return output;
        }
        return null; //could be replaced to return output, so it will return empty list or list with plates
    }

    @Override
    public Plate upsert(Plate plate) {
        ResultSet rs;
        if (connection != null) {
            String query = """
                    INSERT INTO Plate (idPlate, plateName, valoration, idRequiredPlate, uri_preview)
                    VALUES (?, ?, ?, ?, ?)
                    ON DUPLICATE KEY UPDATE
                        plateName = VALUES(plateName),
                        valoration = VALUES(valoration),
                        idRequiredPlate = VALUES(idRequiredPlate),
                        uri_preview = VALUES(uri_preview);
                    """;
            try {
                PreparedStatement preparedStatement = this.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                if (plate.getId() == -1) {
                    preparedStatement.setNull(1, java.sql.Types.INTEGER);
                } else {
                    preparedStatement.setInt(1, plate.getId());
                }
                preparedStatement.setString(2, plate.getName());
                preparedStatement.setInt(3, plate.getValoration());
                if (plate.getRequiredPlate() != null) {
                    preparedStatement.setInt(4, plate.getRequiredPlate().getId());
                } else {
                    preparedStatement.setNull(4, java.sql.Types.INTEGER);
                }
                preparedStatement.setString(5, plate.getPreviewURI());


                rs = preparedStatement.executeQuery();


                ResultSet a = preparedStatement.getGeneratedKeys();

                while (a.next()) {
                    plate.setId(a.getInt(1));
                }

                System.out.println("upserted plate: " + plate);
            } catch (SQLException e) {
                System.out.println("SQL exception when trying to upsert plate: " + e.getMessage());
            }
        }
        return plate;
    }

    @Override
    public void delete(Plate plate) {
        if (connection != null) {
            String query = "DELETE FROM Plate WHERE idPlate = ?";
            try {
                PreparedStatement preparedStatement = this.connection.prepareStatement(query);
                preparedStatement.setInt(1, plate.getId());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("SQL exception when trying to upsert plate: " + e.getMessage());
            }
        }

        plate.setId(-1);
    }

    private Plate plateFromResultSet(ResultSet rs) throws SQLException {
        return new Plate(rs.getInt("idPlate"), rs.getString("plateName"), rs.getInt("valoration"), this.getPlate(rs.getInt("idRequiredPlate")), rs.getString("uri_preview"));
    }

}
