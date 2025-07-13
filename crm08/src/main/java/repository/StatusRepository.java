package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.MysqlConfig; // Assuming you have this
import entity.StatusEntity;

public class StatusRepository {

    public List<StatusEntity> findAll() {
        List<StatusEntity> statusList = new ArrayList<>();
        String query = "SELECT id, name FROM status"; // Adjust table and column names

        try (Connection connection = MysqlConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                StatusEntity status = new StatusEntity();
                status.setId(resultSet.getInt("id"));
                status.setName(resultSet.getString("name"));
                statusList.add(status);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving status: " + e.getMessage());
            // TODO: Implement more robust exception handling (logging, throwing exceptions)
        }

        return statusList;
    }

    public StatusEntity findById(int id) {
        StatusEntity status = null;
        String query = "SELECT id, name FROM status WHERE id = ?";

        try (Connection connection = MysqlConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                status = new StatusEntity();
                status.setId(resultSet.getInt("id"));
                status.setName(resultSet.getString("name"));
                status = new StatusEntity(resultSet.getInt("id"), resultSet.getString("name"));
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving status by ID: " + e.getMessage());
            // TODO: Implement more robust exception handling
        }

        return status;
    }

    // You might need other methods, e.g., to create, update, or delete statuses

}