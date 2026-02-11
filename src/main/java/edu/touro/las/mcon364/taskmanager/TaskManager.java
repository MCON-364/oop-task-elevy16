package edu.touro.las.mcon364.taskmanager;

/** Pattern-matching switch improves readability and maintainability.
 * It reaches its full potential when used with sealed interfaces or classes because
 * the compiles knows all permitted subtypes, so the switch becomes exhaustive.
 * This means: no default branch is needed, if a new permitted subtype is added the compiler forces you to handle it,
 * missing logic becomes a compile-time error and not a runtime bug.
 * Shift from defensive programming to compiler-enforced correctness.
 */

public class TaskManager {

    private final TaskRegistry registry;

    public TaskManager(TaskRegistry registry) {
        this.registry = registry;
    }


    // implemented with pattern-matching switch
    public void run(Command command) {
        switch (command) {
            case AddTaskCommand add -> add.execute();
            case RemoveTaskCommand remove -> remove.execute();
            case UpdateTaskCommand update -> update.execute();
        }
    }
}
