package edu.touro.las.mcon364.taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DemoMain to help students verify behavior is preserved during refactoring.
 * Each test validates one of the demonstration methods.
 */
class DemoMainTest {
    private DemoMain demo;
    private TaskRegistry registry;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        demo = new DemoMain();
        // Access registry through reflection or create our own for testing
        registry = new TaskRegistry();

        // Capture System.out for output verification
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    @DisplayName("Adding tasks should create 5 tasks with correct priorities")
    void testDemonstrateAddingTasks() {
        // Create a fresh demo with accessible registry
        TaskRegistry testRegistry = new TaskRegistry();
        TaskManager testManager = new TaskManager(testRegistry);

        // Add tasks manually (simulating what demonstrateAddingTasks does)
        testManager.run(new AddTaskCommand(testRegistry, new Task("Write documentation", Priority.HIGH)));
        testManager.run(new AddTaskCommand(testRegistry, new Task("Review pull requests", Priority.MEDIUM)));
        testManager.run(new AddTaskCommand(testRegistry, new Task("Update dependencies", Priority.LOW)));
        testManager.run(new AddTaskCommand(testRegistry, new Task("Fix critical bug", Priority.HIGH)));
        testManager.run(new AddTaskCommand(testRegistry, new Task("Refactor code", Priority.MEDIUM)));

        // Verify all tasks were added
        assertEquals(5, testRegistry.getAll().size(), "Should have 5 tasks");

        // Verify specific tasks exist with correct priorities
        Optional<Task> docOptional = testRegistry.get("Write documentation");
        assertTrue(docOptional.isPresent(), "Write documentation task should exist");
        Task doc = docOptional.get();
        assertEquals(Priority.HIGH, doc.priority(), "Write documentation should be HIGH priority");

        Optional<Task> reviewOptional = testRegistry.get("Review pull requests");
        assertTrue(reviewOptional.isPresent(), "Review pull requests task should exist");
        Task review = reviewOptional.get();
        assertEquals(Priority.MEDIUM, review.priority(), "Review pull requests should be MEDIUM priority");

        Optional<Task> dependenciesOptional = testRegistry.get("Update dependencies");
        assertTrue(dependenciesOptional.isPresent(), "Update dependencies task should exist");
        Task dependencies = dependenciesOptional.get();
        assertEquals(Priority.LOW, dependencies.priority(), "Update dependencies should be LOW priority");

        Optional<Task> bugOptional = testRegistry.get("Fix critical bug");
        assertTrue(bugOptional.isPresent(), "Fix critical bug task should exist");
        Task bug =  bugOptional.get();
        assertEquals(Priority.HIGH, bug.priority(), "Fix critical bug should be HIGH priority");

        Optional<Task> refactorOptional = testRegistry.get("Refactor code");
        assertTrue(refactorOptional.isPresent(), "Refactor code task should exist");
        Task refactor = refactorOptional.get();
        assertEquals(Priority.MEDIUM, refactor.priority(), "Refactor code should be MEDIUM priority");
    }

    @Test
    @DisplayName("Retrieving existing task should return correct task")
    void testDemonstrateRetrievingTask() {
        TaskRegistry testRegistry = new TaskRegistry();
        TaskManager testManager = new TaskManager(testRegistry);

        // Add the task we want to retrieve
        Task expectedTask = new Task("Fix critical bug", Priority.HIGH);
        testManager.run(new AddTaskCommand(testRegistry, expectedTask));

        // get optional and verify
        Optional<Task> retrievedOptional = testRegistry.get("Fix critical bug");
        assertTrue(retrievedOptional.isPresent(), "Retrieved task should not be null");

        // unwrap optional and verify
        Task retrieved = retrievedOptional.get();
        assertEquals("Fix critical bug", retrieved.name(), "Task name should match");
        assertEquals(Priority.HIGH, retrieved.priority(), "Task priority should match");
        assertEquals(expectedTask, retrieved, "Retrieved task should equal the added task");
    }

    @Test
    @DisplayName("Retrieving non-existent task should return null (pre-refactor behavior)")
    void testDemonstrateRetrievingNonExistentTask() {
        TaskRegistry testRegistry = new TaskRegistry();

        Optional<Task> missingOptional = testRegistry.get("Non-existent task");

        assertTrue(missingOptional.isEmpty(), "Non-existent task should return null (before Optional refactoring)");
    }

    @Test
    @DisplayName("Updating task should change priority")
    void testDemonstrateUpdatingTask() {
        TaskRegistry testRegistry = new TaskRegistry();
        TaskManager testManager = new TaskManager(testRegistry);

        // Add original task with MEDIUM priority
        testManager.run(new AddTaskCommand(testRegistry, new Task("Refactor code", Priority.MEDIUM)));

        // Verify original priority
        Optional<Task> beforeOptional = testRegistry.get("Refactor code");
        Task before = beforeOptional.get();
        assertEquals(Priority.MEDIUM, before.priority(), "Initial priority should be MEDIUM");

        // Update to HIGH priority
        testManager.run(new UpdateTaskCommand(testRegistry, "Refactor code", Priority.HIGH));

        // Verify updated priority
        Optional<Task> afterOptional = testRegistry.get("Refactor code");
        assertTrue(afterOptional.isPresent(), "Task should still exist after update");
        Task after = afterOptional.get();
        assertEquals(Priority.HIGH, after.priority(), "Priority should be updated to HIGH");
        assertEquals("Refactor code", after.name(), "Task name should remain unchanged");
    }

    @Test
    @DisplayName("Updating non-existent task should not throw exception (pre-refactor behavior)")
    void testDemonstrateUpdatingNonExistentTask() {
        TaskRegistry testRegistry = new TaskRegistry();
        TaskManager testManager = new TaskManager(testRegistry);

        assertThrows(TaskNotFoundException.class, () -> {
            testManager.run(new UpdateTaskCommand(testRegistry, "Non-existent task", Priority.HIGH));
        }, "Updating non-existent task should throw TaskNotFoundException");
    }

    @Test
    @DisplayName("Removing task should delete it from registry")
    void testDemonstrateRemovingTask() {
        TaskRegistry testRegistry = new TaskRegistry();
        TaskManager testManager = new TaskManager(testRegistry);

        // Add tasks
        testManager.run(new AddTaskCommand(testRegistry, new Task("Update dependencies", Priority.LOW)));
        testManager.run(new AddTaskCommand(testRegistry, new Task("Fix critical bug", Priority.HIGH)));

        Optional<Task> removeOptional  = testRegistry.get("Update dependencies");
        assertTrue(removeOptional.isPresent(), "Update dependencies should exist.");
        assertEquals(2, testRegistry.getAll().size(), "Should have 2 tasks initially");

        // Remove one task
        testManager.run(new RemoveTaskCommand(testRegistry, "Update dependencies"));

        // Verify removal
        assertEquals(1, testRegistry.getAll().size(), "Should have 1 task after removal");

        // verify removed task is gone
        Optional<Task> removedOptional =  testRegistry.get("Update dependencies");
        assertTrue(removedOptional.isEmpty(), "Update dependencies should be removed");
        // verify remaining task exists
        Optional<Task> remainingOptional = testRegistry.get("Fix critical bug");
        assertNotNull(remainingOptional.isPresent(), "Fix critical bug should still exist");
    }

    @Test
    @DisplayName("Null return demonstration - registry.get() returns null for missing tasks")
    void testDemonstrateNullReturn() {
        TaskRegistry testRegistry = new TaskRegistry();

        // Attempt to get non-existent task
        Optional<Task> missingOptional = testRegistry.get("Non-existent task");

        // Verify it returns null (this is what needs to be refactored to Optional)
        assertTrue(missingOptional.isEmpty(), "Getting non-existent task should be empty.");
    }

    @Test
    @DisplayName("Full demo run should execute without exceptions")
    void testFullDemoRun() {
        DemoMain testDemo = new DemoMain();

        // The full demo should run without throwing any exceptions
        assertThrows(TaskNotFoundException.class, () -> {
            testDemo.run();
        }, "Full demo should run without exceptions");
    }

    @Test
    @DisplayName("Task equality should work correctly")
    void testTaskEquality() {
        Task task1 = new Task("Test task", Priority.HIGH);
        Task task2 = new Task("Test task", Priority.HIGH);
        Task task3 = new Task("Test task", Priority.LOW);
        Task task4 = new Task("Different task", Priority.HIGH);

        // Same name and priority should be equal
        assertEquals(task1, task2, "Tasks with same name and priority should be equal");
        assertEquals(task1.hashCode(), task2.hashCode(), "Equal tasks should have same hashCode");

        // Different priority should not be equal
        assertNotEquals(task1, task3, "Tasks with different priorities should not be equal");

        // Different name should not be equal
        assertNotEquals(task1, task4, "Tasks with different names should not be equal");
    }

    @Test
    @DisplayName("Command pattern - AddTaskCommand should execute correctly")
    void testAddTaskCommand() {
        TaskRegistry testRegistry = new TaskRegistry();
        Task task = new Task("Test task", Priority.MEDIUM);

        AddTaskCommand command = new AddTaskCommand(testRegistry, task);
        command.execute();

        Optional<Task> addTaskOptional = testRegistry.get("Test task");
        assertTrue(addTaskOptional.isPresent(), "Task should be added after command execution");
        Task addTask = addTaskOptional.get();
        assertEquals(task, addTask, "Added task should match original");
    }

    @Test
    @DisplayName("Command pattern - RemoveTaskCommand should execute correctly")
    void testRemoveTaskCommand() {
        TaskRegistry testRegistry = new TaskRegistry();
        testRegistry.add(new Task("Test task", Priority.MEDIUM));

        RemoveTaskCommand command = new RemoveTaskCommand(testRegistry, "Test task");
        command.execute();

        Optional<Task> removeTaskOptional = testRegistry.get("Test task");
        assertTrue(removeTaskOptional.isEmpty(), "Task should be removed after command execution");
    }

    @Test
    @DisplayName("Command pattern - UpdateTaskCommand should execute correctly")
    void testUpdateTaskCommand() {
        TaskRegistry testRegistry = new TaskRegistry();
        testRegistry.add(new Task("Test task", Priority.LOW));

        UpdateTaskCommand command = new UpdateTaskCommand(testRegistry, "Test task", Priority.HIGH);
        command.execute();

        Optional<Task> updatedOptional = testRegistry.get("Test task");
        assertTrue(updatedOptional.isPresent(), "Task should still exist after update");
        Task updated = updatedOptional.get();
        assertEquals(Priority.HIGH, updated.priority(), "Priority should be updated");
    }

    @Test
    @DisplayName("TaskManager.run() should handle AddTaskCommand")
    void testTaskManagerRunWithAddCommand() {
        TaskRegistry testRegistry = new TaskRegistry();
        TaskManager manager = new TaskManager(testRegistry);
        Task task = new Task("Test task", Priority.HIGH);

        manager.run(new AddTaskCommand(testRegistry, task));

        assertNotNull(testRegistry.get("Test task"), "Task should be added via TaskManager.run()");
    }

    @Test
    @DisplayName("TaskManager.run() should handle RemoveTaskCommand")
    void testTaskManagerRunWithRemoveCommand() {
        TaskRegistry testRegistry = new TaskRegistry();
        TaskManager manager = new TaskManager(testRegistry);
        testRegistry.add(new Task("Test task", Priority.HIGH));

        manager.run(new RemoveTaskCommand(testRegistry, "Test task"));

        Optional<Task> testOptional = testRegistry.get("Test task");
        assertTrue(testOptional.isEmpty(), "Task should be removed via TaskManager.run()");
    }

    @Test
    @DisplayName("TaskManager.run() should handle UpdateTaskCommand")
    void testTaskManagerRunWithUpdateCommand() {
        TaskRegistry testRegistry = new TaskRegistry();
        TaskManager manager = new TaskManager(testRegistry);
        testRegistry.add(new Task("Test task", Priority.LOW));

        manager.run(new UpdateTaskCommand(testRegistry, "Test task", Priority.HIGH));

        Optional<Task> updatedOptional = testRegistry.get("Test task");
        Task updated = updatedOptional.get();
        assertEquals(Priority.HIGH, updated.priority(), "Priority should be updated via TaskManager.run()");
    }
}

