import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TaskManager {
    private static final String FILE_NAME = "tasks.ser";
    private static final Scanner userInput = new Scanner(System.in);
    private static List<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        loadTasksFromFile();

        while (true) {
            displayMenu();
            int choice = userInput.nextInt();
            userInput.nextLine();

            switch (choice) {
                case 1: // For Add a task
                    addTask();
                    break;
                case 2: // For View all tasks
                    viewAllTasks();
                    break;
                case 3: // For View completed tasks
                    viewCompletedTasks();
                    break;
                case 4: // For View pending tasks
                    viewPendingTasks();
                    break;
                case 5: // For Mark a task as completed
                    markTaskAsCompleted();
                    break;
                case 6: // For Delete a task
                    deleteTask();
                    break;
                case 7: // For Sort tasks by due date
                    sortByDueDate();
                    break;
                case 8: // For Sort tasks by priority
                    sortByPriority();
                    break;
                case 9: // For Search for tasks
                    searchTasks();
                    break;
                case 10: // For Exit
                    saveTasksToFile();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n===== Task Manager Menu =====");
        System.out.println("1. Add a task");
        System.out.println("2. View all tasks");
        System.out.println("3. View completed tasks");
        System.out.println("4. View pending tasks");
        System.out.println("5. Mark a task as completed");
        System.out.println("6. Delete a task");
        System.out.println("7. Sort tasks by due date");
        System.out.println("8. Sort tasks by priority");
        System.out.println("9. Search for tasks");
        System.out.println("10. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addTask() {
        System.out.print("Enter task title: ");
        String title = userInput.nextLine();

        System.out.print("Enter task description: ");
        String description = userInput.nextLine();

        Date dueDate = null;
        while (dueDate == null) {
            System.out.print("Enter due date (yyyy-MM-dd): ");
            dueDate = parseDate(userInput.nextLine());
        }

        int priority = getPriority();

        Task task = new Task(title, description, dueDate, priority);
        tasks.add(task);

        System.out.println("Task added successfully.");
    }

    private static int getPriority() {
        int priority = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.print("Enter priority (1 - Low, 2 - Medium, 3 - High): ");
                priority = Integer.parseInt(userInput.nextLine());

                if (priority >= 1 && priority <= 3) {
                    validInput = true;
                } else {
                    System.out.println("Invalid priority. Please enter a number between 1 and 3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        return priority;
    }

    private static void viewAllTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
        } else {
            tasks.forEach(task -> {
                System.out.println("Title: " + task.getTitle());
                System.out.println("Description: " + task.getDescription());
                System.out.println("Due Date: " + task.getDueDate());
                System.out.println("Priority: " + task.getPriority());
                System.out.println("Status: " + (task.isCompleted() ? "Completed" : "Pending"));
                System.out.println("---------------");
            });
        }
    }

    private static void viewCompletedTasks() {
        List<Task> completedTasks = getTasksByStatus(true);
        if (completedTasks.isEmpty()) {
            System.out.println("No completed tasks available.");
        } else {
            completedTasks.forEach(System.out::println);
        }
    }

    private static void viewPendingTasks() {
        List<Task> pendingTasks = getTasksByStatus(false);
        if (pendingTasks.isEmpty()) {
            System.out.println("No pending tasks available.");
        } else {
            pendingTasks.forEach(System.out::println);
        }
    }

    private static void markTaskAsCompleted() {
        viewPendingTasks();
        System.out.print("Enter the ID of the task to mark as completed: ");
        int taskId = getTaskId();

        if (taskId >= 0 && taskId < tasks.size()) {
            Task task = tasks.get(taskId);
            task.setCompleted(true);
            System.out.println("Task '" + task.getTitle() + "' marked as completed.");
        } else {
            System.out.println("Invalid task ID.");
        }
    }

    private static void deleteTask() {
        viewAllTasks();
        System.out.print("Enter the ID of the task to delete: ");
        int taskId = getTaskId();

        if (taskId >= 0 && taskId < tasks.size()) {
            Task task = tasks.remove(taskId);
            System.out.println("Task deleted: " + task.getTitle());
        } else {
            System.out.println("Invalid task ID.");
        }
    }

    private static int getTaskId() {
        int taskId = -1;
        boolean validInput = false;

        while (!validInput) {
            try {
                taskId = Integer.parseInt(userInput.nextLine());

                if (taskId >= 0 && taskId < tasks.size()) {
                    validInput = true;
                } else {
                    System.out.println("Invalid task ID. Please enter a valid ID.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        return taskId;
    }

    private static void sortByDueDate() {
        tasks.sort(Comparator.comparing(Task::getDueDate));
        System.out.println("Tasks sorted by due date.");
    }

    private static void sortByPriority() {
        tasks.sort(Comparator.comparing(Task::getPriority));
        System.out.println("Tasks sorted by priority.");
    }

    private static void searchTasks() {
        System.out.print("Enter keyword to search for tasks: ");
        String keyword = userInput.nextLine().toLowerCase();

        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getTitle().toLowerCase().contains(keyword) ||
                    task.getDescription().toLowerCase().contains(keyword)) {
                matchingTasks.add(task);
            }
        }

        if (matchingTasks.isEmpty()) {
            System.out.println("No matching tasks found.");
        } else {
            matchingTasks.forEach(System.out::println);
        }
    }

    private static List<Task> getTasksByStatus(boolean completed) {
        List<Task> filteredTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.isCompleted() == completed) {
                filteredTasks.add(task);
            }
        }
        return filteredTasks;
    }

    private static Date parseDate(String dateString) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadTasksFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            tasks = (List<Task>) ois.readObject();
            System.out.println("Tasks loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No existing tasks found. Starting with an empty task list.");
        }
    }

    private static void saveTasksToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(tasks);
            System.out.println("Tasks saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving tasks to file.");
        }
    }
}