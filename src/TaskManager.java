import java.io.*; // For File I/O
import java.text.ParseException; // For Date Parsing
import java.text.SimpleDateFormat; // For Date Formatting
import java.util.*; // For Collections

public class TaskManager {
    private static final String FILE_NAME = "tasks.txt"; // For File I/O
    private static final Scanner userInput = new Scanner(System.in);
    private static List<Task> tasks = new ArrayList<>(); // For storing tasks in memory.

    public static void main(String[] args) {
        loadTasksFromFile(); // Load tasks from file if it exists else start with an empty list.

        // Loop until the user exits the program.
        while (true) {
            displayMenu(); // Display menu options to user.
            int choice = userInput.nextInt(); // Get user choice from user input
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

    // Display menu options to user
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

    // Add a task to the list of tasks in memory and save it to the file.
    private static void addTask() {
        System.out.print("Enter task title: ");
        String title = userInput.nextLine();

        System.out.print("Enter task description: ");
        String description = userInput.nextLine();

        Date dueDate = null; // Initialize dueDate to null

        // Parse due date from user input or prompt user to enter it.
        while (dueDate == null) { // Check if dueDate is null
            System.out.print("Enter due date (yyyy-MM-dd): ");
            dueDate = parseDate(userInput.nextLine()); // Parse due date from user input
        }

        // Get priority from user input or prompt user to enter it.
        int priority = getPriority();

        // Create a new task and add it to the list of tasks.
        Task task = new Task(title, description, dueDate, priority);
        tasks.add(task); // Add a task to the list of tasks

        System.out.println("Task added successfully.");
    }

    // Get priority from user input or prompt user to enter it.
    private static int getPriority() {
        int priority = 0; // Initialize priority to 0
        boolean validInput = false; // Initialize validInput to false

        // Loop until a valid priority is entered
        while (!validInput) {
            try {
                System.out.print("Enter priority (1 - Low, 2 - Medium, 3 - High): ");
                priority = Integer.parseInt(userInput.nextLine());

                // Check if priority is between 1 and 3 inclusive.
                if (priority >= 1 && priority <= 3) {
                    validInput = true; // Set validInput to true
                } else {
                    System.out.println("Invalid priority. Please enter a number between 1 and 3.");
                }
            } catch (NumberFormatException e) { // Catch NumberFormatException
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        return priority; // Return priority if valid input is entered.
    }

    // Display all tasks in the list of tasks.
    private static void viewAllTasks() {

        // Check if a task list is empty
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
        } else {

            // Display all tasks in the list of tasks in a table format.
            tasks.forEach(task -> { // For each task in the list of tasks
                System.out.println("Title: " + task.getTitle());
                System.out.println("Description: " + task.getDescription());
                System.out.println("Due Date: " + task.getDueDate());
                System.out.println("Priority: " + task.getPriority());
                System.out.println("Status: " + (task.isCompleted() ? "Completed" : "Pending"));
                System.out.println("---------------");
            }); // End of forEach task in the list of tasks
        }
    }

    // Display all completed tasks in the list of tasks.
    private static void viewCompletedTasks() {
        List<Task> completedTasks = getTasksByStatus(true); // Get completed tasks

        // Check if a completed task list is empty
        if (completedTasks.isEmpty()) {
            System.out.println("No completed tasks available.");
        } else {
            // Display all completed tasks in the list of tasks in a table format.
            completedTasks.forEach(System.out::println); // For each completed task in the list of tasks.
        }
    }

    // Display all pending tasks in the list of tasks.
    private static void viewPendingTasks() {
        List<Task> pendingTasks = getTasksByStatus(false); // Get pending tasks

        // Check if a pending task list is empty
        if (pendingTasks.isEmpty()) {
            System.out.println("No pending tasks available.");
        } else {
            pendingTasks.forEach(System.out::println); // For each pending task in the list of tasks.
        }
    }

    // Mark a task as completed.
    private static void markTaskAsCompleted() {
        viewPendingTasks(); // Display all pending tasks
        System.out.print("Enter the ID of the task to mark as completed: ");
        int taskId = getTaskId(); // Get task ID from user input

        // Check if task ID is valid and mark the task as completed
        if (taskId >= 0 && taskId < tasks.size()) {
            Task task = tasks.get(taskId); // Get the task from the list of tasks
            task.setCompleted(true); // Mark the task as completed
            System.out.println("Task '" + task.getTitle() + "' marked as completed.");
        } else {
            System.out.println("Invalid task ID.");
        }
    }

    // Delete a task from the list of tasks.
    private static void deleteTask() {
        viewAllTasks(); // Display all tasks
        System.out.print("Enter the ID of the task to delete: ");
        int taskId = getTaskId(); // Get task ID from user input

        // Check if task ID is valid and delete the task from the list of tasks
        if (taskId >= 0 && taskId < tasks.size()) {
            Task task = tasks.remove(taskId); // Remove the task from the list of tasks
            System.out.println("Task deleted: " + task.getTitle());
        } else {
            System.out.println("Invalid task ID.");
        }
    }

    // Get task ID from user input and validate it
    private static int getTaskId() {
        int taskId = -1; // Initialize task ID to -1
        boolean validInput = false; // Initialize validInput to false

        // Loop until a valid task ID is entered
        while (!validInput) {
            try {
                taskId = Integer.parseInt(userInput.nextLine()); // Get task ID from user input

                // Check if task ID is valid
                if (taskId >= 0 && taskId < tasks.size()) {
                    validInput = true; // Set validInput to true
                } else {
                    System.out.println("Invalid task ID. Please enter a valid ID.");
                }
            } catch (NumberFormatException e) { // Catch NumberFormatException
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        return taskId; // Return task ID if valid input is entered.
    }

    // Sort tasks by due date or priority depending on user input
    private static void sortByDueDate() {
        tasks.sort(Comparator.comparing(Task::getDueDate)); // Sort tasks by due date.
        System.out.println("Tasks sorted by due date.");
    }

    // Sort tasks by priority or due date depending on user input
    private static void sortByPriority() {
        tasks.sort(Comparator.comparing(Task::getPriority)); // Sort tasks by priority.
        System.out.println("Tasks sorted by priority.");
    }

    // Search for tasks by title or description
    private static void searchTasks() {
        System.out.print("Enter keyword to search for tasks: ");
        String keyword = userInput.nextLine().toLowerCase(); // Get keyword from user input

        // Search for tasks in the list of tasks that contain the keyword
        List<Task> matchingTasks = new ArrayList<>();

        // Search for tasks in the list of tasks
        for (Task task : tasks) {

            // Check if the task title or description contains the keyword.
            if (task.getTitle().toLowerCase().contains(keyword) ||
                    task.getDescription().toLowerCase().contains(keyword)) {

                matchingTasks.add(task); // Add the task to the list of matching tasks
            }
        }

        // Display the matching tasks in a table format
        if (matchingTasks.isEmpty()) {
            System.out.println("No matching tasks found.");
        } else {

            // For each matching task in the list of tasks.
            matchingTasks.forEach(System.out::println);
        }
    }

    // Get tasks by status (completed or not)
    private static List<Task> getTasksByStatus(boolean completed) {
        List<Task> filteredTasks = new ArrayList<>(); // Create a new list of tasks

        // Get tasks by status (completed or not) in the list of tasks
        for (Task task : tasks) {

            // Check if the task is completed or not depending on user input
            if (task.isCompleted() == completed) {
                filteredTasks.add(task); // Add the task to the list of filtered tasks
            }
        }
        return filteredTasks; // Return the list of filtered tasks
    }

    // Parse date string to Date object.
    private static Date parseDate(String dateString) {
        try {

            // Parse date string to Date object.
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateString);

        } catch (ParseException e) { // Catch ParseException
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            return null;
        }
    }

    // Get priority from user input or prompt user to enter it.
    @SuppressWarnings("unchecked")
    private static void loadTasksFromFile() {

        // Load tasks from file
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            tasks = (List<Task>) ois.readObject(); // Read tasks from file
            System.out.println("Tasks loaded successfully.");
        } catch (IOException | ClassNotFoundException e) { // Catch IOException and ClassNotFoundException
            System.out.println("No existing tasks found. Starting with an empty task list.");
        }
    }

    // Save tasks to file.
    private static void saveTasksToFile() {

        // Save tasks to file.
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(tasks); // Write tasks to file.
            System.out.println("Tasks saved successfully.");
        } catch (IOException e) { // Catch IOException
            System.out.println("Error saving tasks to file.");
        }
    }
}