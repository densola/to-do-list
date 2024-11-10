package com.example;

import java.io.IOException;
import java.io.InputStream;

public class Task {
    private String name;
    private String info;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * parseRequest parses the request body returning
     * a Task.
     * 
     * @param inStream
     * @return Task with name and info from submission data.
     */
    public static Task parseRequest(InputStream inStream) throws RuntimeException {
        StringBuilder builder = new StringBuilder();
        Task task = new Task();

        try {
            int i;
            while ((i = inStream.read()) != -1) {
                if (isValidStreamVal(i)) {
                    if (i == 43) {
                        builder.append((char) 43);
                    } else {
                        builder.append((char) i);
                    }
                } else {
                    System.out.println("Error: Passing in invalid data.");
                    throw new RuntimeException();
                }
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }

        String request = builder.toString();
        String[] tsk = request.split("&");

        tsk[0] = tsk[0].replaceAll("name=", "");
        tsk[0] = tsk[0].replaceAll("[+]", " ");

        tsk[1] = tsk[1].replaceAll("info=", "");
        tsk[1] = tsk[1].replaceAll("[+]", " ");

        if (tsk[0].length() < 65) {
            task.setName(tsk[0]);
            task.setInfo(tsk[1]);
        } else {
            throw new RuntimeException("Task name over 64 characters.");
        }

        return task;
    }

    /**
     * parseEditRequest parses requests for editing tasks.
     * 
     * @param inStream
     * @return
     * @throws RuntimeException
     */
    public static Task[] parseEditRequest(InputStream inStream) throws RuntimeException {
        StringBuilder builder = new StringBuilder();
        Task task = new Task();
        Task newTask = new Task();

        try {
            int i;
            while ((i = inStream.read()) != -1) {
                if (isValidStreamVal(i)) {
                    if (i == 43) {
                        builder.append((char) 43);
                    } else {
                        builder.append((char) i);
                    }
                } else {
                    System.out.println("Error: Passing in invalid data.");
                    throw new RuntimeException();
                }
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }

        String request = builder.toString();
        String[] tsk = request.split("&");

        tsk[0] = tsk[0].replaceAll("name=", "");
        tsk[0] = tsk[0].replaceAll("[+]", " ");

        tsk[1] = tsk[1].replaceAll("info=", "");
        tsk[1] = tsk[1].replaceAll("[+]", " ");

        tsk[2] = tsk[2].replaceAll("newname=", "");
        tsk[2] = tsk[2].replaceAll("[+]", " ");

        tsk[3] = tsk[3].replaceAll("newinfo=", "");
        tsk[3] = tsk[3].replaceAll("[+]", " ");

        if (tsk[0].length() < 65 || tsk[2].length() < 65) {
            task.setName(tsk[0]);
            task.setInfo(tsk[1]);
            newTask.setName(tsk[2]);
            newTask.setInfo(tsk[3]);
        } else {
            throw new RuntimeException("Task name over 64 characters.");
        }

        Task[] tasks = { task, newTask };

        return tasks;
    }

    /**
     * isValidStream returns true if i is the ASCII int
     * value for characters 0-9, A-Z, a-z, =, &, and space.
     * 
     * @param i
     * @return
     */
    private static boolean isValidStreamVal(int i) {
        return ((47 < i && i < 58) || (64 < i && i < 91) || (96 < i && i < 123) || i == 61 || i == 38 || i == 43);
    }
}