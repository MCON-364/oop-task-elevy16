package edu.touro.las.mcon364.taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Command implementations.
 * After sealing the Command interface and refactoring, these tests should still pass.
 */
class CommandTest {
    private TaskRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new TaskRegistry();
    }

    @Test
    @DisplayName("AddTaskCommand should add task to registry")
    void testAddTaskCommand() {
        Task task = new Task("New task", Priority.MEDIUM);
        Command command = new AddTaskCommand(registry, task);

        command.execute();

        Optional<Task> newTaskOptional = registry.get("New task");
        assertTrue(newTaskOptional.isPresent(), "Task should be in registry after AddTaskCommand");
        Task newTask = newTaskOptional.get();
        assertEquals(task, newTask, "Added task should match");
    }

    @Test
    @DisplayName("AddTaskCommand should replace existing task with same name")
    void testAddTaskCommandReplacement() {
        Task originalTask = new Task("Task", Priority.LOW);
        Task duplicateTask = new Task("Task", Priority.HIGH);

        new AddTaskCommand(registry, originalTask).execute();

        // adding duplicate task should throw
        assertThrows(TaskAlreadyExistsException.class, () -> {
            new AddTaskCommand(registry, duplicateTask).execute();
        }, "Adding duplicate task should throw exception");

    }

    @Test
    @DisplayName("RemoveTaskCommand should remove task from registry")
    void testRemoveTaskCommand() {
        registry.add(new Task("To be removed", Priority.HIGH));

        Command command = new RemoveTaskCommand(registry, "To be removed");
        command.execute();

        Optional<Task> removedOptional = registry.get("To be removed");
        assertTrue(removedOptional.isEmpty(), "Task should be removed from registry");
    }

    @Test
    @DisplayName("RemoveTaskCommand on non-existent task should throw TaskNotFoundException")
    void testRemoveTaskCommandNonExistent() {
        Command command = new RemoveTaskCommand(registry, "Non-existent");

        assertThrows(TaskNotFoundException.class, () -> {
            command.execute();
        }, "Removing non-existent task should throw TaskNotFoundException");
    }

    @Test
    @DisplayName("UpdateTaskCommand should update existing task priority")
    void testUpdateTaskCommand() {
        registry.add(new Task("Update me", Priority.LOW));

        Command command = new UpdateTaskCommand(registry, "Update me", Priority.HIGH);
        command.execute();

        Optional<Task> updatedOptional = registry.get("Update me");
        assertTrue(updatedOptional.isPresent(), "Task should still exist after update");

        // unwrap optional
        Task updated = updatedOptional.get();
        assertEquals(Priority.HIGH, updated.priority(), "Priority should be updated to HIGH");
    }

    @Test
    @DisplayName("UpdateTaskCommand should preserve task name")
    void testUpdateTaskCommandPreservesName() {
        registry.add(new Task("Important task", Priority.MEDIUM));

        Command command = new UpdateTaskCommand(registry, "Important task", Priority.LOW);
        command.execute();

        // get and unwrap optional
        Optional<Task> updatedOptional = registry.get("Important task");
        Task updated = updatedOptional.get();

        assertEquals("Important task", updated.name(), "Task name should be preserved");
    }

    @Test
    @DisplayName("UpdateTaskCommand on non-existent task should not throw (pre-refactor)")
    void testUpdateTaskCommandNonExistent() {
        Command command = new UpdateTaskCommand(registry, "Non-existent", Priority.HIGH);

        // Pre-refactor: this should not throw, just print a warning
        assertThrows(TaskNotFoundException.class, command::execute,
                "Updating non-existent task should not throw (before custom exception refactoring)");

        // Task should not be created
        // get optional
        Optional<Task> updateOptional = registry.get("Non-existent");
        assertTrue(updateOptional.isEmpty(),
                "Non-existent task should not be created by update");
    }

    @Test
    @DisplayName("UpdateTaskCommand should allow changing priority from HIGH to LOW")
    void testUpdateTaskCommandPriorityDecrease() {
        registry.add(new Task("Flexible", Priority.HIGH));

        new UpdateTaskCommand(registry, "Flexible", Priority.LOW).execute();

        // get and unwrap optional
        Optional<Task> updatedOptional = registry.get("Flexible");
        Task  updated = updatedOptional.get();

        assertEquals(Priority.LOW, updated.priority(),
                "Should allow decreasing priority");
    }

    @Test
    @DisplayName("UpdateTaskCommand should allow changing priority from LOW to HIGH")
    void testUpdateTaskCommandPriorityIncrease() {
        registry.add(new Task("Urgent", Priority.LOW));

        new UpdateTaskCommand(registry, "Urgent", Priority.HIGH).execute();

        // get and unwrap optional
        Optional<Task> updatedOptional = registry.get("Urgent");
        Task  updated = updatedOptional.get();

        assertEquals(Priority.HIGH, updated.priority(),
                "Should allow increasing priority");
    }
}

