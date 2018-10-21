package com.redhat.vscode.demo.repositories;

import java.util.List;

import com.redhat.vscode.demo.model.Todo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Todo Repository
 */
@Repository
public interface TodoRepository extends MongoRepository<Todo, String> {

    default List<Todo> resetData() {
        deleteAll();
        save(new Todo("Introduction", true));
        save(new Todo("Something on the LSP"));
        save(new Todo("jdt.ls and the ecosystem"));
        return findAll();
    }

}