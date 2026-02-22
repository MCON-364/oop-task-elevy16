package edu.touro.las.mcon364.taskmanager;

public final class ClearAllTasksCommand implements Command {
    private final TaskRegistry registry;

    public ClearAllTasksCommand(TaskRegistry registry) {
        this.registry = registry;
    }

    public void execute() {
        registry.clear();
    }
}
