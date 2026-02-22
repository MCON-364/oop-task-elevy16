package edu.touro.las.mcon364.taskmanager;

public final class AddTaskCommand implements Command {
    private final TaskRegistry registry;
    private final Task task;

    public AddTaskCommand(TaskRegistry registry, Task task) {
        this.registry = registry;
        this.task = task;
    }

    public void execute() {
        // Check if task already exists
        if (registry.get(task.name()).isPresent()) {
            throw new TaskAlreadyExistsException("Warning: Task already exists.");
        }

        // add task
        registry.add(task);
    }
}
