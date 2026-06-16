package io.muzoo.ssc.webapp.service;

import io.muzoo.ssc.webapp.model.Todo;
import io.muzoo.ssc.webapp.repository.TodoRepository;

import java.util.List;

public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> listForUser(long userId) {
        return todoRepository.findByUserId(userId);
    }

    public void add(long userId, String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        todoRepository.insert(userId, title.trim());
    }

}
