package com.example.todo.repository;

import com.example.todo.model.*;
import java.util.*;

public interface TodoRepository {
    ArrayList<Todo> getTodos();

    Todo addTodo(Todo newTodo);

    Todo getTodoById(int id);

    Todo updateTodo(int id, Todo newTodo);

    void deleteTodo(int id);
}