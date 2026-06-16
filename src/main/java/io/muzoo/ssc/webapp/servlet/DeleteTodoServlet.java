package io.muzoo.ssc.webapp.servlet;

import io.muzoo.ssc.webapp.Routable;
import io.muzoo.ssc.webapp.service.SecurityService;
import io.muzoo.ssc.webapp.service.TodoService;
import io.muzoo.ssc.webapp.service.TodoServiceAware;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DeleteTodoServlet extends HttpServlet implements Routable, TodoServiceAware {

    private SecurityService securityService;
    private TodoService todoService;

    @Override
    public String getMapping() {
        return "/delete";
    }

    @Override
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public void setTodoService(TodoService todoService) {
        this.todoService = todoService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long userId = (Long) req.getSession().getAttribute("userId");
        long id;
        try {
            id = Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/todos");
            return;
        }

        var todo = todoService.getForUser(userId, id);
        if (todo.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/todos");
            return;
        }
        req.setAttribute("todo", todo.get());
        req.getRequestDispatcher("/WEB-INF/delete.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long userId = (Long) req.getSession().getAttribute("userId");
        long id = Long.parseLong(req.getParameter("id"));

        todoService.delete(userId, id);
        resp.sendRedirect(req.getContextPath() + "/todos");
    }

}
