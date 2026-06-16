package io.muzoo.ssc.webapp.servlet;

import io.muzoo.ssc.webapp.Routable;
import io.muzoo.ssc.webapp.model.Todo;
import io.muzoo.ssc.webapp.service.SecurityService;
import io.muzoo.ssc.webapp.service.TodoService;
import io.muzoo.ssc.webapp.service.TodoServiceAware;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class TodosServlet extends HttpServlet implements Routable, TodoServiceAware {

    private SecurityService securityService;
    private TodoService todoService;

    @Override
    public String getMapping() {
        return "/todos";
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
        Long userId = (Long) req.getSession().getAttribute("userId");
        List<Todo> todos = todoService.listForUser(userId);
        
        req.setAttribute("todos", todos);
        req.getRequestDispatcher("/WEB-INF/todos.jsp").forward(req, resp);
    }

}
