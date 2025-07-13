package controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.TaskEntity;
import service.TaskServices;
import service.ProjectServices;
import service.UserServices;
import service.StatusServices;

import entity.ProjectEntity;
import entity.UserEntity;
import entity.StatusEntity;

@WebServlet(name = "taskController", urlPatterns = { "/task", "/task-edit", "/task-delete", "/task-add", "/task-detail" })
public class TaskController extends HttpServlet {

    private TaskServices taskService = new TaskServices();
    private ProjectServices jobService = new ProjectServices();
    private UserServices userService = new UserServices();
    private StatusServices statusService = new StatusServices();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        switch (path) {
            case "/task":
                getTaskList(req, resp);
                break;
            case "/task-edit":
                getTaskEdit(req, resp);
                break;
            case "/task-delete":
                deleteTask(req, resp);
                break;
            case "/task-add":
                getTaskAdd(req, resp);
                break;
            case "/task-detail":
                getTaskDetail(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        switch (path) {
            case "/task-edit":
                updateTask(req, resp);
                break;
            case "/task-add":
                addTask(req, resp);
                break;
        }
    }

    private void getTaskList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<TaskEntity> taskList = taskService.getAllTasks();
        req.setAttribute("taskList", taskList);
        req.getRequestDispatcher("task.jsp").forward(req, resp);
    }
    //Sửa full code của TaskController
    private void getTaskDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        TaskEntity task = taskService.getTaskById(id);

        req.setAttribute("task", task);
        req.getRequestDispatcher("task-detail.jsp").forward(req, resp);
    }

    private void getTaskEdit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        TaskEntity task = taskService.getTaskById(id);

        // Lấy danh sách project, user và status
        List<ProjectEntity> jobList = jobService.getAllProjects();
        List<UserEntity> userList = userService.getAllUsers();
        List<StatusEntity> statusList = statusService.getAllStatuses();

        // Set các danh sách vào request attribute
        req.setAttribute("jobList", jobList);
        req.setAttribute("userList", userList);
        req.setAttribute("statusList", statusList);
        req.setAttribute("task", task);

        req.getRequestDispatcher("task-edit.jsp").forward(req, resp);
    }

    private void updateTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        int userId = Integer.parseInt(req.getParameter("userId"));
        int jobId = Integer.parseInt(req.getParameter("jobId"));
        int statusId = Integer.parseInt(req.getParameter("statusId"));

        taskService.updateTask(id, name, startDate, endDate, userId, jobId, statusId);

        resp.sendRedirect(req.getContextPath() + "/task");
    }

    private void deleteTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        taskService.deleteTask(id);
        resp.sendRedirect(req.getContextPath() + "/task");
    }

    private void getTaskAdd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ProjectEntity> jobList = jobService.getAllProjects();
        List<UserEntity> userList = userService.getAllUsers();
        List<StatusEntity> statusList = statusService.getAllStatuses();

        req.setAttribute("jobList", jobList);
        req.setAttribute("userList", userList);
        req.setAttribute("statusList", statusList);
        req.getRequestDispatcher("task-add.jsp").forward(req, resp);
    }

    private void addTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String startDateString = req.getParameter("startDate");
        String endDateString = req.getParameter("endDate");

        int userId = Integer.parseInt(req.getParameter("userId"));
        int jobId = Integer.parseInt(req.getParameter("jobId"));
        int statusId = Integer.parseInt(req.getParameter("statusId"));

        // Validate data (important!)
        if (name == null || name.isEmpty() || startDateString == null || startDateString.isEmpty() || endDateString == null || endDateString.isEmpty()) {
            req.setAttribute("errorMessage", "Vui lòng điền đầy đủ thông tin.");
            getTaskAdd(req, resp); // Quay lại trang thêm với thông báo lỗi
            return;
        }

        // Parse date strings to java.sql.Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // Matching the input format
        Date startDate = null;
        Date endDate = null;

        try {
            startDate = dateFormat.parse(startDateString);
            endDate = dateFormat.parse(endDateString);
        } catch (ParseException e) {
            req.setAttribute("errorMessage", "Định dạng ngày không hợp lệ (dd/MM/yyyy).");
            getTaskAdd(req, resp); // Quay lại trang thêm với thông báo lỗi
            return;
        }

        // Convert java.util.Date to java.sql.Date
        java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
        java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());


        TaskEntity task = new TaskEntity();
        task.setName(name);
        task.setStartDate(sqlStartDate.toString()); // Store as String in yyyy-MM-dd format
        task.setEndDate(sqlEndDate.toString());   // Store as String in yyyy-MM-dd format
        task.setUserId(userId);
        task.setJobId(jobId);
        task.setStatusId(statusId);

        taskService.addTask(task); // Gọi TaskService để thêm task vào database

        resp.sendRedirect(req.getContextPath() + "/task");
    }

}