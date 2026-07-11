import java.util.Scanner;

/**
 * ============================================================
 * MAIN MENU - Java Multithreading for Beginners
 * ============================================================
 *
 * This project teaches multithreading step by step, from the
 * very basics to more advanced tools, with heavily commented
 * code at every step. Recommended order: run Lessons 1 to 5
 * in sequence, reading the comments in each file as you go.
 *
 *   Lesson 1: Creating Threads (Thread class vs Runnable vs lambda)
 *   Lesson 2: The Thread Lifecycle (NEW, RUNNABLE, BLOCKED, etc.)
 *   Lesson 3: Race Conditions & Synchronization (the 'synchronized' keyword)
 *   Lesson 4: Thread Communication (wait / notify)
 *   Lesson 5: Thread Pools (ExecutorService, Callable, Future)
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=================================================");
        System.out.println(" JAVA MULTITHREADING FOR BEGINNERS");
        System.out.println("=================================================");
        System.out.println("1. Creating Threads");
        System.out.println("2. Thread Lifecycle");
        System.out.println("3. Race Conditions & Synchronization");
        System.out.println("4. Thread Communication (wait/notify)");
        System.out.println("5. Thread Pools (ExecutorService)");
        System.out.println("6. Run ALL lessons in order");
        System.out.println("-------------------------------------------------");
        System.out.println("TIP: Open the matching .java file in `src/` while");
        System.out.println("     the lesson runs, to read the detailed comments!");
        System.out.println("-------------------------------------------------");
        System.out.print("Enter your choice (1-6): ");

        int choice = 6; // default: run everything if no input is available
        try {
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            } else {
                System.out.println("(No input detected, running ALL lessons by default)");
            }
        } catch (Exception e) {
            System.out.println("(Invalid input, running ALL lessons by default)");
        }

        switch (choice) {
            case 1:
                Lesson1_CreatingThreads.main(args);
                break;
            case 2:
                Lesson2_ThreadLifecycle.main(args);
                break;
            case 3:
                Lesson3_RaceConditionsAndSynchronization.main(args);
                break;
            case 4:
                Lesson4_ThreadCommunication.main(args);
                break;
            case 5:
                Lesson5_ThreadPools.main(args);
                break;
            default:
                Lesson1_CreatingThreads.main(args);
                divider();
                Lesson2_ThreadLifecycle.main(args);
                divider();
                Lesson3_RaceConditionsAndSynchronization.main(args);
                divider();
                Lesson4_ThreadCommunication.main(args);
                divider();
                Lesson5_ThreadPools.main(args);
        }

        System.out.println("\nAll done! You've now covered the core building blocks of Java multithreading.");
    }

    private static void divider() {
        System.out.println("\n=================================================\n");
    }
}
