package dev.kplanky.todo.service;

import dev.kplanky.todo.model.Todo;
import dev.kplanky.todo.repository.TodoRepository;

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

    private static final int MAX_TITLE_LENGTH = 255;

    private String requireTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        String trimmed = title.trim();
        if (trimmed.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException("Title must be " + MAX_TITLE_LENGTH + " characters or fewer");
        }
        return trimmed;
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
