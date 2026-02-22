package edu.touro.las.mcon364.taskmanager;

/** A sealed interface improves safety and clarity by allowing you to explicitly control
 * which classes are allowed to extend or implement them.
 * Sealed hierarchies mean that the compiler knows all valid subtypes, which improves safety and
 * enables better support for modern switch expressions.
 */

public sealed interface Command
        permits AddTaskCommand,
        RemoveTaskCommand,
        UpdateTaskCommand,
        ClearAllTasksCommand {
    void execute();
}

