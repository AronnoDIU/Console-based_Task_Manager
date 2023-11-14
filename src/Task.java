import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

class Task implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String title;
    private final String description;
    private final Date dueDate;
    private boolean completed;
    private final int priority;

    public Task(String title, String description, Date dueDate, int priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.completed = false;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return String.format("%s - %s - Priority: %d - Due: %tF - %s",
                title, completed ? "Completed" : "Pending", priority, dueDate, description);
    }
}