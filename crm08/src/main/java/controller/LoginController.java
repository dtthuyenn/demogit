package controller;

import config.MysqlConfig;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "LoginController", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy email từ Cookie (nếu có)
        Cookie[] cookies = req.getCookies();
        String email = "";
        
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("email")) {
                    email = cookie.getValue();
                }
            }
        }

        req.setAttribute("email", email);
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String remember = req.getParameter("remember-me");

        // Truy vấn kiểm tra email & mật khẩu
        String query = "SELECT email FROM users WHERE email = ? AND password = ?";
        Connection connection = MysqlConfig.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet result = statement.executeQuery();

            if (result.next()) { // Nếu có kết quả, đăng nhập thành công
                // Lưu Session
                HttpSession session = req.getSession();
                session.setAttribute("user", email);

                // Lưu Cookie nếu chọn "Remember Me"
                if (remember != null) {
                    Cookie ckEmail = new Cookie("email", email);
                    ckEmail.setMaxAge(60 * 60 * 24 * 30); // Lưu trong 30 ngày
                    resp.addCookie(ckEmail);
                }

                resp.sendRedirect(req.getContextPath() + "/index.html"); // Chuyển hướng
            } else {
                req.setAttribute("message", "Email hoặc mật khẩu không đúng!");
                req.getRequestDispatcher("login.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("message", "Đăng nhập thất bại.");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}