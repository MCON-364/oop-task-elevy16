package edu.touro.las.mcon364.taskmanager;

public final class UpdateTaskCommand implements Command {
    private final TaskRegistry registry;
    private final String taskName;
    private final Priority newPriority;

    public UpdateTaskCommand(TaskRegistry registry, String taskName, Priority newPriority) {
        this.registry = registry;
        this.taskName = taskName;
        this.newPriority = newPriority;
    }

    public void execute() {
        // use Optional and custom exceptions
        Task existing = registry.get(taskName)
                .orElseThrow(() -> new TaskNotFoundException("Warning: Task '" + taskName + "' not found"));

        // Create a new task with updated priority (tasks are immutable)
        Task updated = new Task(existing.name(), newPriority);
        registry.add(updated);  // This replaces the old task
    }
}
