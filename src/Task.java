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

    // Getters and setters (not shown for brevity)

    @Override
    public String toString() {
        return String.format("%s - %s - Priority: %d - Due: %tF - %s",
                title, completed ? "Completed" : "Pending", priority, dueDate, description);
    }

    public void setCompleted(boolean b) {
        this.completed = b;
    }

    public String getTitle() {
        return null;
    }

    public <U> U getDueDate() {
        return null;
    }

    public <U> U getPriority() {
        return null;
    }

    public String getDescription() {
        return null;
    }

    public boolean isCompleted() {
        return false;
    }
}