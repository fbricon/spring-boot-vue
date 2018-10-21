package com.redhat.vscode.demo.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.redhat.vscode.demo.model.Todo;
import com.redhat.vscode.demo.model.TodoHelper;
import com.redhat.vscode.demo.repositories.TodoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Todo Controller
 */
@RestController
@RequestMapping("/api")
public class TodoController {

    @Autowired
    private TodoRepository repository;

    @GetMapping("/todos")
    public List<Todo> getTodos() {
        return repository.findAll();
    }

    @GetMapping(value = "/todos/{id}")
    public ResponseEntity<Todo> getTodo(@PathVariable("id") String id) {
        return repository.findById(id).map(t -> ResponseEntity.ok(t)).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/todos/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable("id") String id, @Valid @RequestBody Todo todo) {
        Optional<Todo> existing = repository.findById(id);
        if (!existing.isPresent() ) {
            return ResponseEntity.notFound().build();
        }
        todo.setId(id);
        repository.save(todo);
        return ResponseEntity.noContent().build();
        //return ResponseEntity.ok(todo);
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
        List<Todo> completed = repository.findAll(Example.of( new Todo(null, true)));
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
    public List<Todo> resetTodos() {
        repository.resetData();
        return getTodos();
    }

    @PostMapping("/todos")
    public Todo createTodo(@Valid @RequestBody Todo todo) {
        todo.setCompleted(false);
        TodoHelper.enhanceTodo(todo);
        return repository.save(todo);
    }
}
