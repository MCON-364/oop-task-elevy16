package edu.touro.las.mcon364.taskmanager;

public final class AddTaskCommand implements Command {
    private final TaskRegistry registry;
    private final Task task;

    public AddTaskCommand(TaskRegistry registry, Task task) {
        this.registry = registry;
        this.task = task;
    }

    public void execute() {
        registry.add(task);
    }
}
