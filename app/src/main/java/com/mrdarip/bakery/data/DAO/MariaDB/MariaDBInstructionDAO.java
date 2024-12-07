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
    public void delete(Instruction instruction) {
        deleteById(instruction.getId());
    }

    @Override
    public void deleteById(int id) {
        System.out.println("deleting instruction with id: " + id);
        String query = "DELETE FROM Instruction WHERE idInstruction = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL exception when trying to delete instruction: " + e.getMessage());
        }
    }

    @Override
    public Instruction insert(Instruction instruction) {
        ResultSet rs;
        if (connection != null) {
            String query = "INSERT INTO Instruction (difficulty, duration, instructionText) VALUES (?, ?, ?)";
            try {
                PreparedStatement preparedStatement = this.connection.prepareStatement(query);
                preparedStatement.setInt(1, instruction.getDifficulty());
                preparedStatement.setInt(2, instruction.getDuration());
                preparedStatement.setString(3, instruction.getInstructionText());


                preparedStatement.executeUpdate();

                rs = preparedStatement.getGeneratedKeys();

                if (rs.next()) {
                    instruction = instructionFromResultSet(rs);
                }
            } catch (SQLException e) {
                System.out.println("SQL exception when trying to insert instruction: " + e.getMessage());
            }
        }
        return instruction;
    }

    @Override
    public Instruction update(Instruction instruction) {
        System.out.println("updating " + instruction.toString());
        String query = "UPDATE Instruction SET difficulty = ?, duration = ?, instructionText = ? WHERE idInstruction = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);

            preparedStatement.setInt(1, instruction.getDifficulty());
            preparedStatement.setInt(2, instruction.getDuration());
            preparedStatement.setString(3, instruction.getInstructionText());
            preparedStatement.setInt(4, instruction.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL exception when trying to update instruction: " + e.getMessage());
        }
        return instruction;
    }

    @Override
    public Instruction upsert(Instruction instruction) {
        System.out.println("upserting instruction: " + instruction);
        ResultSet rs;
        if (connection != null) {
            String query = """
                    INSERT INTO Instruction (idInstruction, difficulty, duration, instructionText)
                    VALUES (?, ?, ?, ?)
                    ON DUPLICATE KEY UPDATE
                        difficulty = VALUES(difficulty),
                        duration = VALUES(duration),
                        instructionText = VALUES(instructionText);
                    """;
            try {
                PreparedStatement preparedStatement = this.connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
                if (instruction.getId() < 0) {
                    preparedStatement.setNull(1, java.sql.Types.INTEGER);
                } else {
                    preparedStatement.setInt(1, instruction.getId());
                }
                preparedStatement.setInt(2, instruction.getDifficulty());
                preparedStatement.setInt(3, instruction.getDuration());
                preparedStatement.setString(4, instruction.getInstructionText());

                rs = preparedStatement.executeQuery();


                ResultSet a = preparedStatement.getGeneratedKeys();

                while (a.next()) {
                    instruction.setIdInstruction(a.getInt(1));
                }
            } catch (SQLException e) {
                System.out.println("SQL exception when trying to upsert instruction: " + e.getMessage());
            }
        }
        return instruction;
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

    @Override
    public List<Instruction> getAll() {
        List<Instruction> output = new ArrayList<>();
        ResultSet rs;
        if (connection != null) {
            String query = "SELECT * FROM Instruction";
            try {
                Instruction instruction;
                PreparedStatement preparedStatement = this.connection.prepareStatement(query);

                rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    instruction = instructionFromResultSet(rs);
                    output.add(instruction);
                }
            } catch (SQLException e) {
                System.out.println("SQL exception when trying to getAll instructions: " + e.getMessage());
            }
        }
        return output;
    }

    private Instruction instructionFromResultSet(ResultSet rs) throws SQLException {
        return new Instruction(rs.getInt("idInstruction"), rs.getInt("difficulty"), rs.getInt("duration"), rs.getString("instructionText"));
    }
}