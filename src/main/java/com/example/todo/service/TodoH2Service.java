package com.example.todo.service;

import com.example.todo.model.*;
import com.example.todo.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.*;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class TodoH2Service implements TodoRepository {
    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Todo> getTodos() {
        List<Todo> todoList = db.query("SELECT * FROM TODOLIST", new TodoRowMapper());
        ArrayList<Todo> todos = new ArrayList<>(todoList);
        return todos;
    }

    @Override
    public Todo addTodo(Todo newTodo) {
        db.update("INSERT INTO TODOLIST(todo,status,priority) VALUES(?,?,?)", newTodo.getTodo(), newTodo.getStatus(),
                newTodo.getPriority());
        Todo savedTodo = db.queryForObject(
                "SELECT * FROM TODOLIST WHERE todo LIKE ? AND status LIKE ? and priority LIKE ?", new TodoRowMapper(),
                newTodo.getTodo(), newTodo.getStatus(), newTodo.getPriority());
        return savedTodo;
    }

    @Override
    public Todo getTodoById(int id) {
        try {
            Todo todo = db.queryForObject("SELECT * FROM TODOLIST WHERE id = ?", new TodoRowMapper(), id);
            return todo;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Todo updateTodo(int id, Todo newTodo) {
        if (newTodo.getTodo() != null) {
            db.update("UPDATE TODOLIST SET todo = ? WHERE id = ?", newTodo.getTodo(), id);
        }
        if (newTodo.getStatus() != null && ((newTodo.getStatus().equals("TO DO"))
                || (newTodo.getStatus().equals("IN PROGRESS")) || (newTodo.getStatus().equals("DONE")))) {
            db.update("UPDATE TODOLIST SET status = ? WHERE id = ?", newTodo.getStatus(), id);
        }
        if (newTodo.getPriority() != null && ((newTodo.getPriority().equals("HIGH"))
                || (newTodo.getPriority().equals("MEDIUM")) || (newTodo.getPriority().equals("LOW")))) {
            db.update("UPDATE TODOLIST SET priority = ? WHERE id = ?", newTodo.getPriority(), id);
        }
        return getTodoById(id);
    }

    @Override
    public void deleteTodo(int id) {
        db.update("DELETE FROM TODOLIST WHERE id = ?", id);
    }

}