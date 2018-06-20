package com.example.salman.dude;

public class Task {
    String task_title;
    Boolean finished;

    public Task() {
    }

    public Task(String task_title, Boolean finished) {
        this.task_title = task_title;
        this.finished = finished;
    }

    public String getTask_title() {
        return task_title;
    }

    public void setTask_title(String task_title) {
        this.task_title = task_title;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
}
