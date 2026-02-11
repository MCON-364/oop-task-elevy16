package edu.touro.las.mcon364.taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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
        Task doc = testRegistry.get("Write documentation");
        assertNotNull(doc, "Write documentation task should exist");
        assertEquals(Priority.HIGH, doc.priority(), "Write documentation should be HIGH priority");

        Task review = testRegistry.get("Review pull requests");
        assertNotNull(review, "Review pull requests task should exist");
        assertEquals(Priority.MEDIUM, review.priority(), "Review pull requests should be MEDIUM priority");

        Task dependencies = testRegistry.get("Update dependencies");
        assertNotNull(dependencies, "Update dependencies task should exist");
        assertEquals(Priority.LOW, dependencies.priority(), "Update dependencies should be LOW priority");

        Task bug = testRegistry.get("Fix critical bug");
        assertNotNull(bug, "Fix critical bug task should exist");
        assertEquals(Priority.HIGH, bug.priority(), "Fix critical bug should be HIGH priority");

        Task refactor = testRegistry.get("Refactor code");
        assertNotNull(refactor, "Refactor code task should exist");
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

        // Retrieve it
        Task retrieved = testRegistry.get("Fix critical bug");

        // Verify
        assertNotNull(retrieved, "Retrieved task should not be null");
        assertEquals("Fix critical bug", retrieved.name(), "Task name should match");
        assertEquals(Priority.HIGH, retrieved.priority(), "Task priority should match");
        assertEquals(expectedTask, retrieved, "Retrieved task should equal the added task");
    }

    @Test
    @DisplayName("Retrieving non-existent task should return null (pre-refactor behavior)")
    void testDemonstrateRetrievingNonExistentTask() {
        TaskRegistry testRegistry = new TaskRegistry();

        Task missing = testRegistry.get("Non-existent task");

        assertNull(missing, "Non-existent task should return null (before Optional refactoring)");
    }

    @Test
    @DisplayName("Updating task should change priority")
    void testDemonstrateUpdatingTask() {
        TaskRegistry testRegistry = new TaskRegistry();
        TaskManager testManager = new TaskManager(testRegistry);

        // Add original task with MEDIUM priority
        testManager.run(new AddTaskCommand(testRegistry, new Task("Refactor code", Priority.MEDIUM)));

        // Verify original priority
        Task before = testRegistry.get("Refactor code");
        assertEquals(Priority.MEDIUM, before.priority(), "Initial priority should be MEDIUM");

        // Update to HIGH priority
        testManager.run(new UpdateTaskCommand(testRegistry, "Refactor code", Priority.HIGH));

        // Verify updated priority
        Task after = testRegistry.get("Refactor code");
        assertNotNull(after, "Task should still exist after update");
        assertEquals(Priority.HIGH, after.priority(), "Priority should be updated to HIGH");
        assertEquals("Refactor code", after.name(), "Task name should remain unchanged");
    }

    @Test
    @DisplayName("Updating non-existent task should not throw exception (pre-refactor behavior)")
    void testDemonstrateUpdatingNonExistentTask() {
        TaskRegistry testRegistry = new TaskRegistry();
        TaskManager testManager = new TaskManager(testRegistry);

        // This should NOT throw an exception in the pre-refactor version
        // It silently fails with a warning message
        assertDoesNotThrow(() -> {
            testManager.run(new UpdateTaskCommand(testRegistry, "Non-existent task", Priority.HIGH));
        }, "Updating non-existent task should not throw (before custom exception refactoring)");

        // Verify task was not created
        assertNull(testRegistry.get("Non-existent task"), "Non-existent task should not be created");
    }

    @Test
    @DisplayName("Removing task should delete it from registry")
    void testDemonstrateRemovingTask() {
        TaskRegistry testRegistry = new TaskRegistry();
        TaskManager testManager = new TaskManager(testRegistry);

        // Add tasks
        testManager.run(new AddTaskCommand(testRegistry, new Task("Update dependencies", Priority.LOW)));
        testManager.run(new AddTaskCommand(testRegistry, new Task("Fix critical bug", Priority.HIGH)));

        assertEquals(2, testRegistry.getAll().size(), "Should have 2 tasks initially");
        assertNotNull(testRegistry.get("Update dependencies"), "Update dependencies should exist");

        // Remove one task
        testManager.run(new RemoveTaskCommand(testRegistry, "Update dependencies"));

        // Verify removal
        assertEquals(1, testRegistry.getAll().size(), "Should have 1 task after removal");
        assertNull(testRegistry.get("Update dependencies"), "Update dependencies should be removed");
        assertNotNull(testRegistry.get("Fix critical bug"), "Fix critical bug should still exist");
    }

    @Test
    @DisplayName("Null return demonstration - registry.get() returns null for missing tasks")
    void testDemonstrateNullReturn() {
        TaskRegistry testRegistry = new TaskRegistry();

        // Attempt to get non-existent task
        Task missing = testRegistry.get("Non-existent task");

        // Verify it returns null (this is what needs to be refactored to Optional)
        assertNull(missing, "Getting non-existent task should return null (before Optional refactoring)");
    }

    @Test
    @DisplayName("Full demo run should execute without exceptions")
    void testFullDemoRun() {
        DemoMain testDemo = new DemoMain();

        // The full demo should run without throwing any exceptions
        assertDoesNotThrow(() -> {
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

        assertNotNull(testRegistry.get("Test task"), "Task should be added after command execution");
        assertEquals(task, testRegistry.get("Test task"), "Added task should match original");
    }

    @Test
    @DisplayName("Command pattern - RemoveTaskCommand should execute correctly")
    void testRemoveTaskCommand() {
        TaskRegistry testRegistry = new TaskRegistry();
        testRegistry.add(new Task("Test task", Priority.MEDIUM));

        RemoveTaskCommand command = new RemoveTaskCommand(testRegistry, "Test task");
        command.execute();

        assertNull(testRegistry.get("Test task"), "Task should be removed after command execution");
    }

    @Test
    @DisplayName("Command pattern - UpdateTaskCommand should execute correctly")
    void testUpdateTaskCommand() {
        TaskRegistry testRegistry = new TaskRegistry();
        testRegistry.add(new Task("Test task", Priority.LOW));

        UpdateTaskCommand command = new UpdateTaskCommand(testRegistry, "Test task", Priority.HIGH);
        command.execute();

        Task updated = testRegistry.get("Test task");
        assertNotNull(updated, "Task should still exist after update");
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

        assertNull(testRegistry.get("Test task"), "Task should be removed via TaskManager.run()");
    }

    @Test
    @DisplayName("TaskManager.run() should handle UpdateTaskCommand")
    void testTaskManagerRunWithUpdateCommand() {
        TaskRegistry testRegistry = new TaskRegistry();
        TaskManager manager = new TaskManager(testRegistry);
        testRegistry.add(new Task("Test task", Priority.LOW));

        manager.run(new UpdateTaskCommand(testRegistry, "Test task", Priority.HIGH));

        Task updated = testRegistry.get("Test task");
        assertEquals(Priority.HIGH, updated.priority(), "Priority should be updated via TaskManager.run()");
    }
}

