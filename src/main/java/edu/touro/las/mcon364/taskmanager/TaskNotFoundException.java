package edu.touro.las.mcon364.taskmanager;

public class TaskNotFoundException extends RuntimeException{
    public TaskNotFoundException(String taskName){

        super("Task not found: " + taskName);
    }
}
