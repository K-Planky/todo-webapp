package io.muzoo.ssc.webapp.servlet;

import io.muzoo.ssc.webapp.Routable;
import io.muzoo.ssc.webapp.service.SessionKeys;
import io.muzoo.ssc.webapp.service.TodoService;
import io.muzoo.ssc.webapp.service.TodoServiceAware;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ToggleTodoServlet extends HttpServlet implements Routable, TodoServiceAware {

    private TodoService todoService;

    @Override
    public String getMapping() {
        return "/toggle";
    }

    @Override
    public void setTodoService(TodoService todoService) {
        this.todoService = todoService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long userId = (Long) req.getSession().getAttribute(SessionKeys.USER_ID);
        long id = Long.parseLong(req.getParameter("id"));
        todoService.toggle(userId, id);
        resp.sendRedirect(req.getContextPath() + "/todos");
    }

}
