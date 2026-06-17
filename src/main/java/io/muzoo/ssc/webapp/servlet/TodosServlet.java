package io.muzoo.ssc.webapp.servlet;

import io.muzoo.ssc.webapp.Routable;
import io.muzoo.ssc.webapp.model.Todo;
import io.muzoo.ssc.webapp.service.SessionKeys;
import io.muzoo.ssc.webapp.service.TodoService;
import io.muzoo.ssc.webapp.service.TodoServiceAware;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class TodosServlet extends HttpServlet implements Routable, TodoServiceAware {

    private TodoService todoService;

    @Override
    public String getMapping() {
        return "/todos";
    }

    @Override
    public void setTodoService(TodoService todoService) {
        this.todoService = todoService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long userId = (Long) req.getSession().getAttribute(SessionKeys.USER_ID);
        List<Todo> todos = todoService.listForUser(userId);

        req.setAttribute("todos", todos);
        req.getRequestDispatcher("/WEB-INF/todos.jsp").forward(req, resp);
    }

}
