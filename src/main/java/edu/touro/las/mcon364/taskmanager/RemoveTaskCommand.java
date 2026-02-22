package edu.touro.las.mcon364.taskmanager;

public final class RemoveTaskCommand implements Command {
    private final TaskRegistry registry;
    private final String name;

    public RemoveTaskCommand(TaskRegistry registry, String name) {
        this.registry = registry;
        this.name = name;
    }

    public void execute() {
        // check if task exists
        Task existing = registry.get(name)
                        .orElseThrow(() -> new TaskNotFoundException("Warning: cannot remove because task not found."));

        // remove task
        registry.remove(name);
    }
}
