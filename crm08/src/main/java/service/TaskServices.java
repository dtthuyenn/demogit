package service;
import java.util.List;
import entity.TaskEntity;
import repository.TaskRepository;

public class TaskServices {

    private TaskRepository taskRepository = new TaskRepository();

    public List<TaskEntity> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<TaskEntity> getTasksByUserId(int userId) {
        return taskRepository.findTasksByUserId(userId); //Thêm function getTasksByUserId
    }

    public TaskEntity getTaskById(int id) {
        return taskRepository.findById(id);
    }

    public void updateTask(int id, String name, String startDate, String endDate, int userId, int jobId, int statusId) {
        taskRepository.updateTask(id, name, startDate, endDate, userId, jobId, statusId);
    }

    public void deleteTask(int id) {
        taskRepository.deleteTask(id);
    }

    public void addTask(TaskEntity task) {
        taskRepository.addTask(task);
    }
}