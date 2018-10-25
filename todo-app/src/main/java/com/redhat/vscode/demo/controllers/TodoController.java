package com.redhat.vscode.demo.controllers;

import static com.redhat.vscode.demo.model.TodoHelper.enhanceTodo;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.redhat.vscode.demo.model.Todo;
import com.redhat.vscode.demo.repositories.TodoRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Todo Controller
 */
@RestController
@RequestMapping("/api")
public class TodoController {

    private static Logger LOG = LoggerFactory.getLogger(TodoController.class);

    @Autowired
    private TodoRepository repository;

    @GetMapping("/todos")
    public List<Todo> getTodos() {
        List<Todo> todos = repository.findAll();
        for (Todo t : todos) {
            t.toString();
        }
        return todos;
    }

    @GetMapping(value = "/todos/{id}")
    public ResponseEntity<Todo> getTodo(@PathVariable("id") String id) {
        Optional<Todo> res = repository.findById(id);
        return res.map(t -> ResponseEntity.ok(t))
                  .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/todos/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable("id") String id, @Valid @RequestBody Todo todo) {
        Optional<Todo> existing = repository.findById(id);
        if (!existing.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        todo.setId(id);
        repository.save(todo);
        return ResponseEntity.noContent().build();
        // return ResponseEntity.ok(todo);
    }

    @DeleteMapping(value = "/todos/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable("id") String id) {
        return repository.findById(id).map(todo -> {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/todos/purge")
    public ResponseEntity<Object> deleteCompleted() {
        List<Todo> completed = repository.findAll(Example.of(new Todo(null, true)));
        if (!completed.isEmpty()) {
            repository.deleteAll(completed);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/clean")
    public ResponseEntity<Object> deleteAll() {
        repository.deleteAll();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reset")
    public void resetTodos(HttpServletResponse response) throws IOException {
        repository.resetData();
        response.sendRedirect("/");
    }

    @PostMapping("/todos")
    public Todo createTodo(@Valid @RequestBody Todo todo) {
        todo.setCompleted(false);
        enhanceTodo(todo);
        return repository.save(todo);
    }
}
