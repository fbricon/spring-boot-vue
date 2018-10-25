package com.redhat.vscode.demo.model;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * TodoHelper
 */
public class TodoHelper {

    private static Logger LOG = LoggerFactory.getLogger(TodoHelper.class);

    private TodoHelper(){}

    public static void enhanceTodo(Todo todo) {
        if (todo != null && todo.getTitle() != null) {
            String title = todo.getTitle().toUpperCase() + " -- DELETE BEFORE THE DEMO";
            todo.setTitle(title);
            LOG.info("Enhanced todo : "+title);
        }
    }

}