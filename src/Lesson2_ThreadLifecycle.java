/**
 * ============================================================
 * LESSON 2: THE THREAD LIFECYCLE
 * ============================================================
 *
 * Every thread in Java moves through a series of STATES during
 * its life. Understanding these states helps you understand what
 * a thread is "doing" at any given moment, and why your program
 * behaves the way it does.
 *
 * The states (from java.lang.Thread.State enum) are:
 *
 *   1) NEW           - Thread object created, but start() not called yet.
 *   2) RUNNABLE       - start() was called; thread is running OR ready to
 *                        run (waiting for CPU time from the OS scheduler).
 *   3) BLOCKED        - Thread is waiting to enter a 'synchronized' block
 *                        because another thread currently holds the lock.
 *   4) WAITING        - Thread is waiting indefinitely for another thread
 *                        to signal it (e.g. via wait()/notify(), or join()
 *                        with no timeout).
 *   5) TIMED_WAITING  - Like WAITING, but with a time limit (e.g. sleep(),
 *                        or join(timeout)).
 *   6) TERMINATED     - The run() method has finished executing (or threw
 *                        an uncaught exception). The thread is now "dead"
 *                        and can never be started again.
 *
 * This lesson prints out the state of a thread at different points
 * in time so you can see these transitions happening for real.
 */
public class Lesson2_ThreadLifecycle {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== LESSON 2: Thread Lifecycle ===\n");

        // Create a thread but do NOT start it yet.
        Thread worker = new Thread(() -> {
            try {
                System.out.println("[worker] I'm running now. Going to sleep for 1 second...");
                Thread.sleep(1000); // this will move the thread into TIMED_WAITING
                System.out.println("[worker] Woke up! Finishing my work now.");
            } catch (InterruptedException e) {
                System.out.println("[worker] I was interrupted while sleeping!");
            }
        }, "Worker");

        // STATE 1: NEW
        // The thread object exists, but start() hasn't been called,
        // so the underlying OS thread doesn't exist yet.
        System.out.println("Before start()      -> State: " + worker.getState()); // NEW

        worker.start();

        // STATE 2: RUNNABLE
        // Right after calling start(), the thread is eligible to run.
        // NOTE: It might already be running, or might still be waiting
        // for the CPU - Java calls both of these "RUNNABLE".
        System.out.println("Right after start() -> State: " + worker.getState()); // RUNNABLE

        // Give the worker thread a brief moment to enter its sleep() call
        Thread.sleep(200);

        // STATE 3: TIMED_WAITING
        // By now, the worker thread should be inside Thread.sleep(1000),
        // so its state should show as TIMED_WAITING.
        System.out.println("While worker sleeps -> State: " + worker.getState()); // TIMED_WAITING

        // Wait for the worker to completely finish before checking again
        worker.join();

        // STATE 4: TERMINATED
        // The run() method has completed, so the thread is now dead.
        System.out.println("After join()        -> State: " + worker.getState()); // TERMINATED

        System.out.println();
        System.out.println("--- BLOCKED state demo ---");
        demonstrateBlockedState();

        System.out.println();
        System.out.println("Lesson 2 complete! Key takeaways:");
        System.out.println(" 1) A thread's state changes automatically as it runs, sleeps, or waits.");
        System.out.println(" 2) TERMINATED threads are 'dead' - calling start() on them again");
        System.out.println("    would throw an IllegalThreadStateException.");
        System.out.println(" 3) You can inspect a thread's state anytime with getState(),");
        System.out.println("    which is great for debugging concurrency issues.");
    }

    // This method demonstrates the BLOCKED state: one thread holds a lock
    // (via a synchronized block) while another thread tries to enter the
    // same synchronized block and must wait.
    private static void demonstrateBlockedState() throws InterruptedException {
        final Object sharedLock = new Object();

        // Thread A grabs the lock and holds it for 1 second
        Thread threadA = new Thread(() -> {
            synchronized (sharedLock) {
                System.out.println("[Thread-A] Acquired the lock. Holding it for 1 second...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
                System.out.println("[Thread-A] Releasing the lock.");
            }
        }, "Thread-A");

        // Thread B tries to grab the SAME lock right after Thread A
        Thread threadB = new Thread(() -> {
            synchronized (sharedLock) {
                System.out.println("[Thread-B] Acquired the lock!");
            }
        }, "Thread-B");

        threadA.start();
        Thread.sleep(200); // give Thread-A time to grab the lock first
        threadB.start();
        Thread.sleep(200); // give Thread-B time to try (and fail) to grab the lock

        // At this point, Thread-B should be BLOCKED, waiting for Thread-A
        // to release the sharedLock.
        System.out.println("Thread-B's state while waiting for the lock -> " + threadB.getState()); // BLOCKED

        threadA.join();
        threadB.join();
    }
}
