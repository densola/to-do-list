package com.example;

import java.util.ArrayList;

public class Tasks {
    private final ArrayList<Task> tasklist = new ArrayList<>();

    /**
     * addTask adds a Task to tasklist based on the
     * provided name and info.
     * @param name
     * @param info
     */
    public void addTask(String name, String info) {
        Task taskToAdd = new Task();

        taskToAdd.name = name;
        taskToAdd.info = info;

        tasklist.add(taskToAdd);
    }

    /**
     * toJSON generates a json string based on each
     * Task stored in tasklist.
     * @return
     */
    public String toJSON() {
        String json = "{\"tasks\": [";

        for (int i = 0; i < this.tasklist.size(); i++) {
            if (i == this.tasklist.size() - 1) {
                Task t = this.tasklist.get(i);
                json += String.format("{\"name\": \"%s\",\"info\": \"%s\"}", t.name, t.info);
            } else {
                Task t = this.tasklist.get(i);
                json += String.format("{\"name\": \"%s\",\"info\": \"%s\"},", t.name, t.info);
            }
        }

        json += "]}";

        return json;
    }

    public class Task {
        private String name;
        private String info;
    }
}
