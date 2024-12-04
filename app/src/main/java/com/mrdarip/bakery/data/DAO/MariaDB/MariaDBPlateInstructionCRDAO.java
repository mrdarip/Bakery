package com.mrdarip.bakery.data.DAO.MariaDB;

import com.mrdarip.bakery.data.DAO.PlateInstructionCRDAO;
import com.mrdarip.bakery.data.database.MariaDBConnector;
import com.mrdarip.bakery.data.entity.Instruction;
import com.mrdarip.bakery.data.entity.Plate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MariaDBPlateInstructionCRDAO extends PlateInstructionCRDAO {

    Connection connection;

    public MariaDBPlateInstructionCRDAO() {
        super();

        connection = MariaDBConnector.getConnectionInstance();
    }

    @Override
    public void bind(Plate plate, Instruction instruction, int position) {
        this.shift(plate, 1, position);

        String query = "INSERT INTO PlateInstructionCR (IdPlate, idInstruction, position) VALUES (?, ?, ?)";
        ResultSet rs;

        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1, plate.getId());
            preparedStatement.setInt(2, instruction.getId());
            preparedStatement.setInt(3, position);

            preparedStatement.executeUpdate();

            System.out.println("PlateInstructionCR binded: " + plate.getId() + " " + instruction.getId() + " " + position);
        } catch (SQLException e) {
            System.out.println("SQL exception when trying to bind CR: " + e.getMessage());
        }
    }

    @Override
    public void remove(Plate plate, int position) {
        String query = "DELETE FROM PlateInstructionCR WHERE IdPlate = ? AND position = ?";

        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1, plate.getId());
            preparedStatement.setInt(2, position);

            preparedStatement.executeUpdate();

            System.out.println("PlateInstructionCR removed: " + plate.getId() + " " + position);
        } catch (SQLException e) {
            System.out.println("SQL exception when trying to remove CR: " + e.getMessage());
        }
        this.shift(plate, -1, position);
    }

    @Override
    public void swap(Plate plate, int position1, int position2) {
        System.out.println("Not implemented yet swapping plate instructions");
    }

    @Override
    public void shift(Plate plate, int positions, int from) {
        negate(plate);
        String query = "UPDATE PlateInstructionCR SET position = position - ? WHERE IdPlate = ? AND position <= ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1, positions);
            preparedStatement.setInt(2, plate.getId());
            preparedStatement.setInt(3, from);

            preparedStatement.executeUpdate();

            System.out.println("PlateInstructionCR shifted: " + plate.getId() + " " + positions);
        } catch (SQLException e) {
            System.out.println("SQL exception when trying to shift CR: " + e.getMessage());
        }
        negate(plate);
    }

    @Override
    public void negate(Plate plate) {
        String query = "UPDATE PlateInstructionCR SET position = -position WHERE IdPlate = ?";

        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1, plate.getId());

            preparedStatement.executeUpdate();

            System.out.println("PlateInstructionCR negated: " + plate.getId());
        } catch (SQLException e) {
            System.out.println("SQL exception when trying to negate CR: " + e.getMessage());
        }
    }

    @Override
    public void bind(Plate plate, List<Instruction> instructions, boolean reverse) {
        removeAll(plate);

        if (reverse) {
            instructions = instructions.reversed();
        }

        for (int i = 0; i < instructions.size(); i++) {
            bind(plate, instructions.get(i), 1);
        }
    }

    @Override
    public void overwrite(Plate plate, Instruction instruction, int position) {
        String query = "UPDATE PlateInstructionCR SET idInstruction = ? WHERE IdPlate = ? AND position = ?";

        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1, instruction.getId());
            preparedStatement.setInt(2, plate.getId());
            preparedStatement.setInt(3, position);

            preparedStatement.executeUpdate();

            System.out.println("PlateInstructionCR overwritten: " + plate.getId() + " " + instruction.getId() + " " + position);
        } catch (SQLException e) {
            System.out.println("SQL exception when trying to overwrite CR: " + e.getMessage());
        }
    }

    @Override
    public void removeAll(Plate plate) {
        String query = "DELETE FROM PlateInstructionCR WHERE IdPlate = ?";

        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1, plate.getId());

            preparedStatement.executeUpdate();

            System.out.println("PlateInstructionCR removed all: " + plate.getId());
        } catch (SQLException e) {
            System.out.println("SQL exception when trying to remove all CR: " + e.getMessage());
        }
    }
}
