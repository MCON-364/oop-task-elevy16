package edu.touro.las.mcon364.taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TaskManager.
 * After refactoring to use pattern-matching switch, these tests should still pass.
 */
class TaskManagerTest {
    private TaskRegistry registry;
    private TaskManager manager;

    @BeforeEach
    void setUp() {
        registry = new TaskRegistry();
        manager = new TaskManager(registry);
    }

    @Test
    @DisplayName("TaskManager.run() should execute AddTaskCommand")
    void testRunAddTaskCommand() {
        Task task = new Task("Test task", Priority.MEDIUM);
        Command command = new AddTaskCommand(registry, task);

        manager.run(command);

        assertNotNull(registry.get("Test task"), "Task should be added");
    }

    @Test
    @DisplayName("TaskManager.run() should execute RemoveTaskCommand")
    void testRunRemoveTaskCommand() {
        registry.add(new Task("Remove me", Priority.HIGH));
        Command command = new RemoveTaskCommand(registry, "Remove me");

        manager.run(command);

        Optional<Task>  removeOptional = registry.get("Remove me");
        assertTrue(removeOptional.isEmpty(), "Task should be removed");
    }

    @Test
    @DisplayName("TaskManager.run() should execute UpdateTaskCommand")
    void testRunUpdateTaskCommand() {
        registry.add(new Task("Update me", Priority.LOW));
        Command command = new UpdateTaskCommand(registry, "Update me", Priority.HIGH);

        manager.run(command);

        // get the optional and unwrap it
        Optional<Task> taskOptional = registry.get("Update me");
        Task task = taskOptional.get();

        assertEquals(Priority.HIGH, task.priority(),
                "Task priority should be updated");
    }

    @Test
    @DisplayName("TaskManager.run() should handle multiple commands in sequence")
    void testRunMultipleCommands() {
        manager.run(new AddTaskCommand(registry, new Task("Task 1", Priority.HIGH)));
        manager.run(new AddTaskCommand(registry, new Task("Task 2", Priority.LOW)));
        manager.run(new UpdateTaskCommand(registry, "Task 2", Priority.MEDIUM));
        manager.run(new RemoveTaskCommand(registry, "Task 1"));

        // get the optional and unwrap it
        Optional<Task> taskOptional1 = registry.get("Task 1");
        Optional<Task> taskOptional2 = registry.get("Task 2");

        assertTrue(taskOptional1.isEmpty(), "Task 1 should be removed");
        assertTrue(taskOptional2.isPresent(), "Task 2 should still exist");

        Task task2 = taskOptional2.get(); // unwrap optional
        assertEquals(Priority.MEDIUM, task2.priority(),
                "Task 2 priority should be updated");
    }

    @Test
    @DisplayName("TaskManager should work with same registry instance")
    void testSharedRegistry() {
        // Verify that manager uses the same registry instance
        Task task = new Task("Shared task", Priority.HIGH);
        manager.run(new AddTaskCommand(registry, task));

        // Should be retrievable from the registry we passed to manager
        assertNotNull(registry.get("Shared task"),
                "Task should be in the shared registry instance");
    }
}

