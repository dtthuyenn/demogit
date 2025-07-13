package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(filterName = "authenFilter", urlPatterns = { "/user-add", "/user", "/role-add" })

public class AuthenticationFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		/*
		 * Ép kiểu thằng ServletRequest và ServletResponse về thằng con HTTP Servlet Tại
		 * vì thằng ServletRequest ít hàm quá
		 */
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		String roleName = "";
		Cookie[] cookies = req.getCookies();
		for (Cookie data : cookies) {
			if (data.getName().equals("role")) {
				roleName = data.getValue();
			}
		}

		String context = req.getContextPath();
		// Kiểm tra đường dẫn đang gọi là đường dẫn nào
		String path = req.getServletPath();
		String rolename = null;
		switch (path) {
		case "/user":
			chain.doFilter(request, response);
			break;

		case "/user-add":
			if (roleName.equals("ROLE_ADMIN")) {
				chain.doFilter(request, response);
			} else {
				resp.sendRedirect(context + "/user");
			}
			break;

		case "/role-add":
			if (roleName.equals("ROLE_ADMIN")) {
				chain.doFilter(request, response);
			} else {
				resp.sendRedirect(context + "/role-table");
			}
			break;
		case "/task-add":
			if (roleName.equals("ROLE_ADMIN")) {
				chain.doFilter(request, response);
				resp.sendRedirect(context + "/task");
			}
			break;
		case "/groupwork-add":
			if (roleName.equals("ROLE_ADMIN")) {
				chain.doFilter(request, response);
			} else {
				resp.sendRedirect(context + "/groupwork");
			}
			break;
		default:
			break;
		}

	}

}