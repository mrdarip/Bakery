package com.mrdarip.bakery.data.DAO;

import com.mrdarip.bakery.data.Database.MariaDBConnector;
import com.mrdarip.bakery.data.entity.Plate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                    plate = new Plate(rs.getInt("idPlate"), rs.getString("plateName"), rs.getInt("valoration"), this.getPlate(rs.getInt("idRequiredPlate")), rs.getString("uri_preview"));
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
        List<Plate> output = new ArrayList();
        int pageSize = 4;
        if (connection != null) {
            String query = "SELECT * FROM Plate LIMIT ? OFFSET ?";
            try {
                PreparedStatement preparedStatement = this.connection.prepareStatement(query);
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


}
