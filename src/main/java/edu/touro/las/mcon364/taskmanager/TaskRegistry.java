package edu.touro.las.mcon364.taskmanager;

import java.util.*;

public class TaskRegistry {
    private final Map<String, Task> tasks = new HashMap<>();

    public void add(Task task) {
        tasks.put(task.name(), task);
    }

    public Optional<Task> get(String name) {
        return Optional.ofNullable(tasks.get(name));
    }

    public void remove(String name) {
        tasks.remove(name);
    }

    public Map<String, Task> getAll() {
        return tasks;
    }

    public void clear() {
        tasks.clear();
    }

    public Map<Priority, List<Task>> getTasksByPriority() {
        Map<Priority, List<Task>> tasksByPriority = new HashMap<>();

        for (Task task : tasks.values()) {
            Priority priority = task.priority();


            // get the list or create a new one if it doesn't exist
            tasksByPriority
                    .computeIfAbsent(priority, k -> new ArrayList<>())
                    .add(task);
        }
        return tasksByPriority;
    }
}
