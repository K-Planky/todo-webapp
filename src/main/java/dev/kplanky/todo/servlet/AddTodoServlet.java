package dev.kplanky.todo.servlet;

import dev.kplanky.todo.Routable;
import dev.kplanky.todo.service.SessionKeys;
import dev.kplanky.todo.service.TodoService;
import dev.kplanky.todo.service.TodoServiceAware;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AddTodoServlet extends HttpServlet implements Routable, TodoServiceAware {

    private TodoService todoService;

    @Override
    public String getMapping() {
        return "/add";
    }

    @Override
    public void setTodoService(TodoService todoService) {
        this.todoService = todoService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/add.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long userId = (Long) req.getSession().getAttribute(SessionKeys.USER_ID);
        String title = req.getParameter("title");

        try {
            todoService.add(userId, title);
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/add.jsp").forward(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/todos");
    }

}
