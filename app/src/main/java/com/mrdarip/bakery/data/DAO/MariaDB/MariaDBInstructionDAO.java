package com.mrdarip.bakery.data.DAO.MariaDB;

import com.mrdarip.bakery.data.DAO.InstructionDao;
import com.mrdarip.bakery.data.database.MariaDBConnector;
import com.mrdarip.bakery.data.entity.Instruction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MariaDBInstructionDAO extends InstructionDao {

    Connection connection;

    public MariaDBInstructionDAO() {
        super();

        connection = MariaDBConnector.getConnectionInstance();
    }


    @Override
    public Instruction getInstructionById(int id) {
        ResultSet rs;
        if (connection != null && id != 0) {
            String query = "SELECT * FROM Instruction WHERE idInstruction = ?";
            try {
                Instruction instruction = null;
                PreparedStatement preparedStatement = this.connection.prepareStatement(query);
                preparedStatement.setInt(1, id);

                rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    instruction = instructionFromResultSet(rs);
                }
                return instruction;
            } catch (SQLException e) {
                System.out.println("SQL exception when trying to getPlate " + id + ": " + e.getMessage());
            }

        }
        return null;
    }

    @Override
    public Instruction upsert(Instruction instruction) {
        return null;

        //not implemented
    }

    @Override
    public List<Instruction> getInstructionsByPlateId(int plateId) {
        List<Instruction> output = new ArrayList<>();
        ResultSet rs;
        if (connection != null && plateId != 0) {
            String query = "SELECT i.* FROM Instruction as i INNER JOIN PlateInstructionCR as cr ON i.idInstruction = cr.idInstruction WHERE cr.idPlate = ?";
            try {
                Instruction instruction;
                PreparedStatement preparedStatement = this.connection.prepareStatement(query);
                preparedStatement.setInt(1, plateId);

                rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    instruction = instructionFromResultSet(rs);
                    output.add(instruction);
                }
                return output;
            } catch (SQLException e) {
                System.out.println("SQL exception when trying to getInstructionsByPlateId " + plateId + ": " + e.getMessage());
            }
        }
        return null;
    }

    private Instruction instructionFromResultSet(ResultSet rs) throws SQLException {
        return new Instruction(rs.getInt("idInstruction"), getInstructionById(rs.getInt("idSharperInstruction")), rs.getInt("difficulty"), rs.getInt("duration"), rs.getString("instructionText"));
    }
}