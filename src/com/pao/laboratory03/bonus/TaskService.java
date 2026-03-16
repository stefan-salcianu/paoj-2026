package com.pao.laboratory03.bonus;

import java.util.*;

public class TaskService {
    private static TaskService instance;
    private int idCounter = 1;

    private final Map<String, Task> tasksById = new HashMap<>();
    private final Map<Priority, List<Task>> tasksByPriority = new HashMap<>();
    private final List<String> auditLog = new ArrayList<>();

    private TaskService() {
        for (Priority p : Priority.values()) {
            tasksByPriority.put(p, new ArrayList<>());
        }
    }

    public static TaskService getInstance() {
        if (instance == null) {
            instance = new TaskService();
        }
        return instance;
    }

    public Task addTask(String title, Priority priority) {
        String id = String.format("T%03d", idCounter++);

        if (tasksById.containsKey(id)) { // Teoretic nu se întâmplă la auto-generare, dar o tratăm
            throw new DuplicateTaskException("Task-ul cu ID " + id + " există deja!");
        }

        Task newTask = new Task(id, title, priority);
        tasksById.put(id, newTask);
        tasksByPriority.get(priority).add(newTask);

        String logEntry = String.format("[ADD] %s: '%s' (%s)", id, title, priority.name());
        auditLog.add(logEntry);

        return newTask;
    }

    public void assignTask(String taskId, String assignee) {
        Task task = getTaskOrThrow(taskId);
        task.setAssignee(assignee);
        auditLog.add(String.format("[ASSIGN] %s → %s", taskId, assignee));
    }

    public void changeStatus(String taskId, Status newStatus) {
        Task task = getTaskOrThrow(taskId);
        Status oldStatus = task.getStatus();

        if (!oldStatus.canTransitionTo(newStatus)) {
            throw new InvalidTransitionException(oldStatus, newStatus);
        }

        task.setStatus(newStatus);
        auditLog.add(String.format("[STATUS] %s: %s → %s", taskId, oldStatus.name(), newStatus.name()));
    }

    public List<Task> getTasksByPriority(Priority priority) {
        return tasksByPriority.getOrDefault(priority, new ArrayList<>());
    }

    public Map<Status, Long> getStatusSummary() {
        Map<Status, Long> summary = new LinkedHashMap<>();
        for (Status s : Status.values()) {
            summary.put(s, 0L);
        }

        for (Task t : tasksById.values()) {
            summary.put(t.getStatus(), summary.get(t.getStatus()) + 1);
        }
        return summary;
    }

    public List<Task> getUnassignedTasks() {
        List<Task> unassigned = new ArrayList<>();
        for (Task t : tasksById.values()) {
            if (t.getAssignee() == null) {
                unassigned.add(t);
            }
        }
        return unassigned;
    }

    public double getTotalUrgencyScore(int baseDays) {
        double total = 0;
        for (Task t : tasksById.values()) {
            if (t.getStatus() != Status.DONE && t.getStatus() != Status.CANCELLED) {
                total += t.getPriority().calculateScore(baseDays);
            }
        }
        return total;
    }

    public void printAuditLog() {
        for (String log : auditLog) {
            System.out.println(log);
        }
    }

    private Task getTaskOrThrow(String taskId) {
        if (!tasksById.containsKey(taskId)) {
            throw new TaskNotFoundException("Task-ul '" + taskId + "' nu a fost găsit");
        }
        return tasksById.get(taskId);
    }
}