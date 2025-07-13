package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entity.UserEntity;
import entity.TaskEntity;
import service.UserServices;

@WebServlet(name = "profileController", urlPatterns = {"/profile"})
public class ProfileController extends HttpServlet {

    private UserServices userServices = new UserServices();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy thông tin người dùng từ session
        HttpSession session = req.getSession();
        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            // Nếu không có thông tin người dùng trong session, chuyển hướng đến trang đăng nhập
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Lấy ID người dùng
        int userId = loggedInUser.getId();

        // Lấy danh sách công việc của người dùng
        List<TaskEntity> taskList = userServices.getTasksByUserId(userId);

        // Tính toán thống kê công việc
        int notStartedCount = userServices.getTaskCountByStatusAndUserId(userId, 1); // Giả sử status ID 1 là "Chưa bắt đầu"
        int inProgressCount = userServices.getTaskCountByStatusAndUserId(userId, 2); // Giả sử status ID 2 là "Đang thực hiện"
        int completedCount = userServices.getTaskCountByStatusAndUserId(userId, 3); // Giả sử status ID 3 là "Hoàn thành"
        int totalTaskCount = userServices.getTotalTaskCountByUserId(userId);

        double notStartedPercentage = (totalTaskCount == 0) ? 0 : (double) notStartedCount / totalTaskCount * 100;
        double inProgressPercentage = (totalTaskCount == 0) ? 0 : (double) inProgressCount / totalTaskCount * 100;
        double completedPercentage = (totalTaskCount == 0) ? 0 : (double) completedCount / totalTaskCount * 100;

        // Truyền dữ liệu đến trang JSP
        req.setAttribute("user", loggedInUser);
        req.setAttribute("taskList", taskList);
        req.setAttribute("notStartedPercentage", notStartedPercentage);
        req.setAttribute("inProgressPercentage", inProgressPercentage);
        req.setAttribute("completedPercentage", completedPercentage);

        // Chuyển tiếp đến trang profile.jsp
        req.getRequestDispatcher("profile.jsp").forward(req, resp);
    }
}