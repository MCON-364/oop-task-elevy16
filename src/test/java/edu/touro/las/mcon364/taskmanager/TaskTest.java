package edu.touro.las.mcon364.taskmanager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Task class.
 * After refactoring to a record, these tests should still pass.
 */
class TaskTest {

    @Test
    @DisplayName("Task should be created with name and priority")
    void testTaskCreation() {
        Task task = new Task("Test task", Priority.HIGH);

        assertEquals("Test task", task.name(), "Task name should match");
        assertEquals(Priority.HIGH, task.priority(), "Task priority should match");
    }

    @Test
    @DisplayName("Tasks with same name and priority should be equal")
    void testEquality() {
        Task task1 = new Task("Same task", Priority.MEDIUM);
        Task task2 = new Task("Same task", Priority.MEDIUM);

        assertEquals(task1, task2, "Tasks with same name and priority should be equal");
        assertEquals(task1.hashCode(), task2.hashCode(), "Equal tasks should have same hashCode");
    }

    @Test
    @DisplayName("Tasks with different names should not be equal")
    void testInequalityDifferentNames() {
        Task task1 = new Task("Task A", Priority.HIGH);
        Task task2 = new Task("Task B", Priority.HIGH);

        assertNotEquals(task1, task2, "Tasks with different names should not be equal");
    }

    @Test
    @DisplayName("Tasks with different priorities should not be equal")
    void testInequalityDifferentPriorities() {
        Task task1 = new Task("Same name", Priority.HIGH);
        Task task2 = new Task("Same name", Priority.LOW);

        assertNotEquals(task1, task2, "Tasks with different priorities should not be equal");
    }

    @Test
    @DisplayName("Task should not equal null")
    void testNotEqualsNull() {
        Task task = new Task("Test task", Priority.MEDIUM);

        assertNotEquals(null, task, "Task should not equal null");
    }

    @Test
    @DisplayName("Task should equal itself")
    void testEqualsSelf() {
        Task task = new Task("Test task", Priority.LOW);

        assertEquals(task, task, "Task should equal itself");
    }

    @Test
    @DisplayName("All priority levels should work")
    void testAllPriorities() {
        Task lowTask = new Task("Low", Priority.LOW);
        Task mediumTask = new Task("Medium", Priority.MEDIUM);
        Task highTask = new Task("High", Priority.HIGH);

        assertEquals(Priority.LOW, lowTask.priority());
        assertEquals(Priority.MEDIUM, mediumTask.priority());
        assertEquals(Priority.HIGH, highTask.priority());
    }
}

