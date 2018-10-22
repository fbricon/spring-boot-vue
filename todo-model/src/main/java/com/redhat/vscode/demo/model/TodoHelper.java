package com.redhat.vscode.demo.model;

/**
 * TodoHelper
 */
public class TodoHelper {

    private TodoHelper(){}

    public static void enhanceTodo(Todo todo) {
        if (todo != null) {
            String title = todo.getTitle() + " - DELETE THIS BEFORE THE DEMO";
            todo.setTitle(title);
        }
    }
}