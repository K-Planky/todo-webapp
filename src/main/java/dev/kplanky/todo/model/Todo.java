package dev.kplanky.todo.model;

import java.time.OffsetDateTime;

@SuppressWarnings("ClassCanBeRecord")
public class Todo {

    private final long id;
    private final long userId;
    private final String title;
    private final boolean completed;
    private final OffsetDateTime createdAt;

    public Todo(long id, long userId, String title, boolean completed, OffsetDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.completed = completed;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

}


