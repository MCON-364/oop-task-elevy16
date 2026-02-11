package edu.touro.las.mcon364.taskmanager;

public final class RemoveTaskCommand implements Command {
    private final TaskRegistry registry;
    private final String name;

    public RemoveTaskCommand(TaskRegistry registry, String name) {
        this.registry = registry;
        this.name = name;
    }

    public void execute() {
        registry.remove(name);
    }
}
