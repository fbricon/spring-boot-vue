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
        save(new Todo("Overview of VSCode and the LSP"));
        save(new Todo("Talk about Eclipse jdt.ls"));
        save(new Todo("Talk about vscode-java"));
        save(new Todo("Demo time"));
        return findAll();
    }

}