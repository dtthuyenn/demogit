package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.MysqlConfig;
import entity.TaskEntity;

public class TaskRepository {

    public List<TaskEntity> findAll() {
        List<TaskEntity> taskList = new ArrayList<>();
        String query = "SELECT t.id, t.name, t.start_date, t.end_date, t.user_id, t.job_id, t.status_id, s.name AS status_name, " +
                "p.name AS project_name, u.fullname AS user_name " +
                "FROM tasks t " +
                "JOIN status s ON t.status_id = s.id " +
                "JOIN jobs p ON t.job_id = p.id " +
                "JOIN users u ON t.user_id = u.id " +
                "ORDER BY t.name";

        try (Connection connection = MysqlConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    TaskEntity task = new TaskEntity();
                    task.setId(resultSet.getInt("id"));
                    task.setName(resultSet.getString("name"));
                    task.setStartDate(resultSet.getString("start_date"));
                    task.setEndDate(resultSet.getString("end_date"));
                    task.setUserId(resultSet.getInt("user_id"));
                    task.setJobId(resultSet.getInt("job_id"));
                    task.setStatusId(resultSet.getInt("status_id"));
                    task.setStatusName(resultSet.getString("status_name"));
                    task.setProjectName(resultSet.getString("project_name"));
                    task.setUserName(resultSet.getString("user_name"));
                    taskList.add(task);
                }
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy danh sách task: " + e.getMessage());
            e.printStackTrace();
        }

        return taskList;
    }

    public List<TaskEntity> findTasksByUserId(int userId) { //Thêm function findTasksByUserId
        List<TaskEntity> taskList = new ArrayList<>();
        String query = "SELECT t.id, t.name, t.start_date, t.end_date, t.user_id, t.job_id, t.status_id, s.name AS status_name, " +
                "p.name AS project_name, u.fullname AS user_name " +
                "FROM tasks t " +
                "JOIN status s ON t.status_id = s.id " +
                "JOIN jobs p ON t.job_id = p.id " +
                "JOIN users u ON t.user_id = u.id "+
                " WHERE t.user_id = ?"; //thêm where để lấy nhưng công việc theo user id

        try (Connection connection = MysqlConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    TaskEntity task = new TaskEntity();
                    task.setId(resultSet.getInt("id"));
                    task.setName(resultSet.getString("name"));
                    task.setStartDate(resultSet.getString("start_date"));
                    task.setEndDate(resultSet.getString("end_date"));
                    task.setUserId(resultSet.getInt("user_id"));
                    task.setJobId(resultSet.getInt("job_id"));
                    task.setStatusId(resultSet.getInt("status_id"));
                    task.setStatusName(resultSet.getString("status_name"));
                    task.setProjectName(resultSet.getString("project_name"));
                    task.setUserName(resultSet.getString("user_name"));
                    taskList.add(task);
                }
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy danh sách task: " + e.getMessage());
            e.printStackTrace();
        }

        return taskList;
    }

    public TaskEntity findById(int id) {
        TaskEntity task = null;
        String query = "SELECT t.id, t.name, t.start_date, t.end_date, t.user_id, t.job_id, t.status_id, s.name AS status_name,  p.name AS project_name, u.fullname AS user_name " +
                "FROM tasks t " +
                "JOIN status s ON t.status_id = s.id " +
                "JOIN jobs p ON t.job_id = p.id " +
                "JOIN users u ON t.user_id = u.id "+
                "WHERE t.id = ?";

        try (Connection connection = MysqlConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    task = new TaskEntity();
                    task.setId(resultSet.getInt("id"));
                    task.setName(resultSet.getString("name"));
                    task.setStartDate(resultSet.getString("start_date"));
                    task.setEndDate(resultSet.getString("end_date"));
                    task.setUserId(resultSet.getInt("user_id"));
                    task.setJobId(resultSet.getInt("job_id"));
                    task.setStatusId(resultSet.getInt("status_id"));
                    task.setStatusName(resultSet.getString("status_name")); // Lấy statusName
                    task.setProjectName(resultSet.getString("project_name")); // Lấy project name
                    task.setUserName(resultSet.getString("user_name")); // Lấy user name
                }
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy task theo id: " + e.getMessage());
            e.printStackTrace();
        }

        return task;
    }

    public int updateTask(int id, String name, String startDate, String endDate, int userId, int jobId, int statusId) {
        int count = 0;
        String query = "UPDATE tasks SET name = ?, start_date = ?, end_date = ?, user_id = ?, job_id = ?, status_id = ? WHERE id = ?";

        try (Connection connection = MysqlConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, name);
            statement.setString(2, startDate);
            statement.setString(3, endDate);
            statement.setInt(4, userId);
            statement.setInt(5, jobId);
            statement.setInt(6, statusId);
            statement.setInt(7, id);

            count = statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật task: " + e.getMessage());
            e.printStackTrace();
        }

        return count;
    }

    public int deleteTask(int id) {
        int count = 0;
        String query = "DELETE FROM tasks WHERE id = ?";

        try (Connection connection = MysqlConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            count = statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa task: " + e.getMessage());
            e.printStackTrace();
        }

        return count;
    }

    public int addTask(TaskEntity task) {
        String query = "INSERT INTO tasks (name, start_date, end_date, user_id, job_id, status_id) VALUES (?, ?, ?, ?, ?, ?)";
        int count = 0;

        try (Connection connection = MysqlConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, task.getName());
            statement.setString(2, task.getStartDate());
            statement.setString(3, task.getEndDate());
            statement.setInt(4, task.getUserId());
            statement.setInt(5, task.getJobId());
            statement.setInt(6, task.getStatusId());

            count = statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm task: " + e.getMessage());
            e.printStackTrace();
        }
        return count;
    }
}