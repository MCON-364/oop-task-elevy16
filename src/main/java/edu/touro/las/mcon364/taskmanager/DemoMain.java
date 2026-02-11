package edu.touro.las.mcon364.taskmanager;

public class DemoMain {
    private final TaskRegistry registry;
    private final TaskManager manager;

    public DemoMain() {
        this.registry = new TaskRegistry();
        this.manager = new TaskManager(registry);
    }

    public static void main(String[] args) {
        DemoMain demo = new DemoMain();
        demo.run();
    }

    public void run() {
        System.out.println("=== Task Management System Demo ===\n");

        demonstrateAddingTasks();
        demonstrateRetrievingTask();
        demonstrateUpdatingTask();
        demonstrateUpdatingNonExistentTask();
        demonstrateRemovingTask();
        demonstrateNullReturn();
        displaySummary();
    }

    private void demonstrateAddingTasks() {
        System.out.println("1. Adding tasks...");
        manager.run(new AddTaskCommand(registry, new Task("Write documentation", Priority.HIGH)));
        manager.run(new AddTaskCommand(registry, new Task("Review pull requests", Priority.MEDIUM)));
        manager.run(new AddTaskCommand(registry, new Task("Update dependencies", Priority.LOW)));
        manager.run(new AddTaskCommand(registry, new Task("Fix critical bug", Priority.HIGH)));
        manager.run(new AddTaskCommand(registry, new Task("Refactor code", Priority.MEDIUM)));

        System.out.println("   Added 5 tasks to the registry");
        displayAllTasks();
    }

    private void demonstrateRetrievingTask() {
        System.out.println("\n2. Retrieving a specific task...");
        Task retrieved = registry.get("Fix critical bug");
        if (retrieved != null) {
            System.out.println("   Found: " + retrieved.name() + " (Priority: " + retrieved.priority() + ")");
        } else {
            System.out.println("   Task not found");
        }
    }

    private void demonstrateUpdatingTask() {
        System.out.println("\n3. Updating a task's priority...");
        System.out.println("   Changing 'Refactor code' from MEDIUM to HIGH");
        manager.run(new UpdateTaskCommand(registry, "Refactor code", Priority.HIGH));
        displayAllTasks();
    }

    private void demonstrateUpdatingNonExistentTask() {
        System.out.println("\n4. Attempting to update non-existent task...");
        manager.run(new UpdateTaskCommand(registry, "Non-existent task", Priority.HIGH));
        System.out.println("   ^ This should throw a custom exception, not just print a warning!");
    }

    private void demonstrateRemovingTask() {
        System.out.println("\n5. Removing a task...");
        manager.run(new RemoveTaskCommand(registry, "Update dependencies"));
        System.out.println("   Removed 'Update dependencies'");
        displayAllTasks();
    }

    private void demonstrateNullReturn() {
        System.out.println("\n6. Attempting to retrieve non-existent task...");
        Task missing = registry.get("Non-existent task");
        if (missing == null) {
            System.out.println("   Returned null - this should be refactored to use Optional!");
        }
    }

    private void displaySummary() {
        System.out.println("\n=== Demo Complete ===");
        System.out.println("\nNOTE: This code uses old-style Java patterns:");
        System.out.println("  - Task is a verbose class (should be a record)");
        System.out.println("  - Command interface is not sealed (allows unexpected implementations)");
        System.out.println("  - TaskManager uses instanceof chains (should use pattern matching)");
        System.out.println("  - Methods return null (should use Optional)");
        System.out.println("  - No custom exceptions for domain errors");
        System.out.println("  - UpdateTaskCommand silently fails instead of throwing exceptions");
    }

    private void displayAllTasks() {
        System.out.println("\n   Current tasks in registry:");
        registry.getAll().forEach((name, task) ->
            System.out.println("     - " + name + " (Priority: " + task.priority() + ")")
        );
    }
}
