package io.muzoo.ssc.webapp.service;

import io.muzoo.ssc.webapp.model.Todo;
import io.muzoo.ssc.webapp.repository.TodoRepository;

import java.util.List;
import java.util.Optional;

public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> listForUser(long userId) {
        return todoRepository.findByUserId(userId);
    }

    public Optional<Todo> getForUser(long userId, long id) {
        return todoRepository.findByUserIdAndId(userId, id);
    }

    private String requireTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        return title.trim();
    }

    public void add(long userId, String title) {
        todoRepository.insert(userId, requireTitle(title));
    }

    public void updateTitle(long userId, long id, String title) {
        todoRepository.updateTitle(userId, id, requireTitle(title));
    }

    public void delete(long userId, long id) {
        todoRepository.delete(userId, id);
    }

    public void toggle(long userId, long id) {
        todoRepository.toggleCompleted(userId, id);
    }

}
