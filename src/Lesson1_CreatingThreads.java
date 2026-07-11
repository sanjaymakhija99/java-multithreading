/**
 * ============================================================
 * LESSON 1: CREATING THREADS
 * ============================================================
 *
 * WHAT IS A THREAD?
 * A "thread" is simply a separate path of execution inside your
 * program. Every Java program has AT LEAST one thread already,
 * called the "main thread" - it's the one that runs your main()
 * method.
 *
 * "Multithreading" means creating ADDITIONAL threads so that your
 * program can do more than one thing at (roughly) the same time.
 * Think of it like a restaurant with one waiter (single-threaded)
 * versus a restaurant with 3 waiters (multi-threaded) - more
 * waiters means more customers can be served in parallel.
 *
 * There are TWO common ways to create a thread in Java. This
 * lesson shows both, with detailed comments on each.
 */
public class Lesson1_CreatingThreads {

    // ------------------------------------------------------------
    // METHOD 1: Extend the Thread class
    // ------------------------------------------------------------
    // We create our own class that "extends" (inherits from) the
    // built-in java.lang.Thread class. We then OVERRIDE the run()
    // method - this method contains the code that will execute on
    // the new thread.
    static class MyThread extends Thread {

        // Every Thread can have a name, which helps with debugging
        public MyThread(String name) {
            super(name); // calls the Thread class's constructor to set the name
        }

        // This is the method that actually runs on the new thread.
        // You NEVER call run() directly - Java calls it for you
        // automatically when you call start() (explained below).
        @Override
        public void run() {
            for (int i = 1; i <= 3; i++) {
                // getName() returns the name we gave this thread
                System.out.println(getName() + " -> printing number " + i);

                try {
                    // Thread.sleep() pauses THIS thread for the given
                    // number of milliseconds. It does NOT pause other
                    // threads - that's the whole point of multithreading!
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    // sleep() can throw this checked exception if another
                    // thread interrupts us while we're sleeping. In real
                    // apps you'd handle this properly; here we just log it.
                    System.out.println(getName() + " was interrupted!");
                }
            }
        }
    }

    // ------------------------------------------------------------
    // METHOD 2: Implement the Runnable interface
    // ------------------------------------------------------------
    // This is the MORE RECOMMENDED way in modern Java, because:
    //   1) Java doesn't support multiple inheritance, so if your class
    //      already extends something else, you can't also extend Thread.
    //   2) It separates "the task to run" (Runnable) from "the thing
    //      that runs it" (Thread) - cleaner design.
    //
    // Runnable is a "functional interface" with a single method: run().
    // Because of that, we can also create one using a lambda expression
    // (shown further below in main()).
    static class MyRunnableTask implements Runnable {

        private final String taskName;

        public MyRunnableTask(String taskName) {
            this.taskName = taskName;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 3; i++) {
                // Thread.currentThread() gets a reference to whichever
                // thread is CURRENTLY executing this code, and getName()
                // returns its name.
                System.out.println(taskName + " (on " + Thread.currentThread().getName()
                        + ") -> printing letter " + (char) ('A' + i - 1));
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    System.out.println(taskName + " was interrupted!");
                }
            }
        }
    }

    // ------------------------------------------------------------
    // MAIN METHOD - this runs on the "main thread"
    // ------------------------------------------------------------
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== LESSON 1: Creating Threads ===");
        System.out.println("Current thread running main(): " + Thread.currentThread().getName());
        System.out.println();




        // --- Using Method 2: implementing Runnable ---
        System.out.println("--- Starting a thread created by IMPLEMENTING Runnable ---");
        MyRunnableTask task = new MyRunnableTask("Task-A");

        // Notice: MyRunnableTask is NOT a Thread. It's just a plain task.
        // We wrap it inside an actual Thread object to run it.
        Thread thread2 = new Thread(task);
        thread2.start();
        thread2.join();

        System.out.println();

        // --- Bonus: Using a lambda expression (shortest way) ---
        // Since Runnable has only one method (run), Java lets us skip
        // writing a whole class and just supply the method body directly.
        System.out.println("--- Starting a thread created with a LAMBDA (shortcut) ---");
        Thread thread3 = new Thread(() -> {
            for (int i = 1; i <= 3; i++) {
                System.out.println("Lambda-Thread -> tick " + i);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    System.out.println("Lambda-Thread interrupted");
                }
            }
        }, "Worker-Thread-3");
        thread3.start();
        thread3.join();

        System.out.println();
        System.out.println("Lesson 1 complete! Key takeaways:");
        System.out.println(" 1) Call start(), never run() directly, to actually run on a new thread.");
        System.out.println(" 2) join() makes one thread wait for another to finish.");
        System.out.println(" 3) Implementing Runnable (or using a lambda) is usually preferred");
        System.out.println("    over extending Thread, because it's more flexible.");
    }
}
