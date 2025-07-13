
package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.RoleEntity;
import service.RoleServices;

@WebServlet(urlPatterns = {"/role-table", "/role-edit", "/role-delete", "/role-add"})
public class RoleController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private RoleServices roleServices = new RoleServices();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        switch (path) {
            case "/role-table":
                getRoles(request, response);
                break;
            case "/role-edit":
                showEditForm(request, response);
                break;
            case "/role-delete":
                deleteRole(request, response);
                break;
            case "/role-add":
                showAddForm(request, response);
                break;
            default:
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        switch (path) {
            case "/role-edit":
                updateRole(request, response);
                break;
            case "/role-add":
                addRole(request, response);
                break;
            default:
                break;
        }
    }
    
    // lấy danh ds Role
    private void getRoles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<RoleEntity> listRoles = roleServices.getAllRoles();
        request.setAttribute("listRoles", listRoles);
        request.getRequestDispatcher("role-table.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int roleId = Integer.parseInt(request.getParameter("id"));
        RoleEntity role = roleServices.getRoleById(roleId);
        request.setAttribute("role", role);
        request.getRequestDispatcher("role-edit.jsp").forward(request, response);
    }

    private void updateRole(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int roleId = Integer.parseInt(request.getParameter("id"));
        String roleName = request.getParameter("name");
        String roleDescription = request.getParameter("description");

        RoleEntity role = new RoleEntity();
        role.setId(roleId);
        role.setName(roleName);
        role.setDescription(roleDescription);

        roleServices.updateRole(role);
        response.sendRedirect("role-table");
    }

    private void deleteRole(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            try {
                int roleId = Integer.parseInt(idParam);
                roleServices.deleteRole(roleId);
                response.sendRedirect("role-table");
            } catch (NumberFormatException e) {
                System.err.println("Invalid role ID: " + idParam);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid role ID");
            }
        } else {
            System.err.println("Role ID is missing");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Role ID is missing");
        }
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("role-add.jsp").forward(request, response);
    }

    private void addRole(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String roleName = request.getParameter("name");
        String roleDescription = request.getParameter("description");

        RoleEntity role = new RoleEntity();
        role.setName(roleName);
        role.setDescription(roleDescription);

        roleServices.addRole(role);
        response.sendRedirect("role-table");
    }
}
