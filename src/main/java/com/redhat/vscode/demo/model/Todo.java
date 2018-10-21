package com.redhat.vscode.demo.model;

import java.util.Map;

/**
 * Todo class
 */
public class Todo {

    private String id;
    private String title;
    private boolean completed;

    public Todo(String string, boolean b) {
        this.title = string;
        this.completed = b;
	}


    public Todo() {

    }
    public Todo(String title) {
        this(title, false);
    }
	/**
     * @return the completed
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @param completed the completed to set
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }



}
