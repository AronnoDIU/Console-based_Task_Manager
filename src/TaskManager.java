import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TaskManager {
    private static final String FILE_NAME = "tasks.ser";
    private static final Scanner scanner = new Scanner(System.in);
    private static List<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        loadTasksFromFile();

        while (true) {
            displayMenu();
            String choice = scanner.nextLine().toLowerCase();

            switch (choice) {
                case "add":
                    addTask();
                    break;
                case "view all":
                    viewAllTasks();
                    break;
                case "view completed":
                    viewCompletedTasks();
                    break;
                case "view pending":
                    viewPendingTasks();
                    break;
                case "complete":
                    markTaskAsCompleted();
                    break;
                case "delete":
                    deleteTask();
                    break;
                case "sort by date":
                    sortByDueDate();
                    break;
                case "sort by priority":
                    sortByPriority();
                    break;
                case "search":
                    searchTasks();
                    break;
                case "exit":
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
        String title = scanner.nextLine();

        System.out.print("Enter task description: ");
        String description = scanner.nextLine();

        System.out.print("Enter due date (yyyy-MM-dd): ");
        Date dueDate = parseDate(scanner.nextLine());

        System.out.print("Enter priority (1 - Low, 2 - Medium, 3 - High): ");
        int priority = Integer.parseInt(scanner.nextLine());

        Task task = new Task(title, description, dueDate, priority);
        tasks.add(task);

        System.out.println("Task added successfully.");
    }

    private static void viewAllTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
        } else {
            tasks.forEach(System.out::println);
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
        int taskId = Integer.parseInt(scanner.nextLine());

        if (taskId >= 0 && taskId < tasks.size()) {
            Task task = tasks.get(taskId);
            task.setCompleted(true);
            System.out.println("Task marked as completed.");
        } else {
            System.out.println("Invalid task ID.");
        }
    }

    private static void deleteTask() {
        viewAllTasks();
        System.out.print("Enter the ID of the task to delete: ");
        int taskId = Integer.parseInt(scanner.nextLine());

        if (taskId >= 0 && taskId < tasks.size()) {
            Task task = tasks.remove(taskId);
            System.out.println("Task deleted: " + task.getTitle());
        } else {
            System.out.println("Invalid task ID.");
        }
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
        String keyword = scanner.nextLine().toLowerCase();

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