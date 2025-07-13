package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.RoleEntity;
import entity.UserEntity;
import entity.TaskEntity;
import service.UserServices;

@WebServlet(name = "usercController", urlPatterns = { "/user", "/user-add", "/user-edit", "/user-delete", "/user-detail" })
public class UserController extends HttpServlet {

    private UserServices userServices = new UserServices();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String path = req.getServletPath();

        switch (path) {
            case "/user":
                getUser(req, resp);
                break;

            case "/user-add":
                userAdd(req, resp);
                break;
            case "/user-edit":
                getUserEdit(req, resp);
                break;
            case "/user-delete":
                deleteUser(req, resp);
                break;
            case "/user-detail":
                showUserDetail(req, resp);
                break;

        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String path = req.getServletPath();
        switch (path) {
            case "/user":

                break;

            case "/user-add":

                String fullname = req.getParameter("fullname");
                String password = req.getParameter("password");
                String email = req.getParameter("email");
                String phone = req.getParameter("phone");
                int idRole = Integer.parseInt(req.getParameter("role"));

                userServices.insertUser(fullname, email, password, phone, idRole);
                userAdd(req, resp);
                break;
            case "/user-edit":
                updateUser(req, resp);
                break;

        }
    }

    private void userAdd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<RoleEntity> roles = userServices.getRole();
        req.setAttribute("roles", roles);
        req.getRequestDispatcher("user-add.jsp").forward(req, resp);
    }

    private void getUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<UserEntity> listUser = userServices.getAllUsers();
        req.setAttribute("listUser", listUser);
        req.getRequestDispatcher("user-table.jsp").forward(req, resp);
    }

    private void getUserEdit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        UserEntity user = userServices.getUserById(id);
        List<RoleEntity> roles = userServices.getRole();

        req.setAttribute("user", user);
        req.setAttribute("roles", roles);

        req.getRequestDispatcher("user-edit.jsp").forward(req, resp);
    }

    private void updateUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String fullname = req.getParameter("fullname");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        int roleId = Integer.parseInt(req.getParameter("role"));

        userServices.updateUser(id, fullname, email, phone, roleId);
        resp.sendRedirect(req.getContextPath() + "/user");
    }


    private void deleteUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        userServices.deleteUser(id);
        resp.sendRedirect(req.getContextPath() + "/user");
    }

    private void showUserDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        UserEntity user = userServices.getUserById(id);
        List<TaskEntity> tasks = userServices.getTasksByUserId(id);

        int totalTasks = userServices.getTotalTaskCountByUserId(id);
        int notStartedTasks = userServices.getTaskCountByStatusAndUserId(id, 1); // Giả sử status ID của "Chưa bắt đầu" là 1
        int inProgressTasks = userServices.getTaskCountByStatusAndUserId(id, 2); // Giả sử status ID của "Đang thực hiện" là 2
        int completedTasks = userServices.getTaskCountByStatusAndUserId(id, 3); // Giả sử status ID của "Hoàn thành" là 3

        double notStartedPercentage = (totalTasks > 0) ? ((double) notStartedTasks / totalTasks) * 100 : 0;
        double inProgressPercentage = (totalTasks > 0) ? ((double) inProgressTasks / totalTasks) * 100 : 0;
        double completedPercentage = (totalTasks > 0) ? ((double) completedTasks / totalTasks) * 100 : 0;

        req.setAttribute("user", user);
        req.setAttribute("tasks", tasks);
        req.setAttribute("totalTasks", totalTasks);
        req.setAttribute("notStartedPercentage", String.format("%.0f", notStartedPercentage)); // Định dạng số về 0 chữ số thập phân
        req.setAttribute("inProgressPercentage",String.format("%.0f", inProgressPercentage));
        req.setAttribute("completedPercentage", String.format("%.0f", completedPercentage));
        req.getRequestDispatcher("/user-detail.jsp").forward(req, resp);
    }

}