import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * ============================================================
 * LESSON 5: THREAD POOLS (ExecutorService)
 * ============================================================
 *
 * THE PROBLEM WITH CREATING THREADS MANUALLY:
 * Creating a new Thread object is somewhat expensive (it uses real
 * operating system resources). If your program needs to run
 * HUNDREDS or THOUSANDS of small tasks, creating a brand-new
 * Thread for every single one would be wasteful and could even
 * crash your program by using up all available system resources.
 *
 * THE SOLUTION: Thread Pools.
 * A thread pool creates a FIXED number of reusable worker threads
 * ahead of time. You then submit tasks to the pool, and whichever
 * worker thread is free will pick up and run the next task. Once a
 * worker finishes a task, it goes back into the pool, ready to pick
 * up another one - just like workers at a factory line.
 *
 * Java provides ExecutorService as an easy interface to work with
 * thread pools, so you don't have to manage the threads yourself.
 */
public class Lesson5_ThreadPools {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== LESSON 5: Thread Pools (ExecutorService) ===\n");

        // ------------------------------------------------------------
        // PART 1: Running simple tasks (Runnable) with a fixed thread pool
        // ------------------------------------------------------------
        System.out.println("--- Running 6 tasks using a pool of only 2 threads ---");

        // Executors.newFixedThreadPool(n) creates a pool with exactly
        // 'n' worker threads. Even if we submit 100 tasks, only 'n' of
        // them run at the same time - the rest wait in a queue.
        ExecutorService pool = Executors.newFixedThreadPool(6);

        for (int i = 1; i <= 6; i++) {
            final int taskId = i;

            // submit() hands the task to the pool. It returns immediately
            // (it does NOT block/wait) - the task will run whenever a
            // worker thread becomes free.
            pool.submit(() -> {
                System.out.println("Task " + taskId + " is running on " + Thread.currentThread().getName());
                try {
                    Thread.sleep(500);
                    System.out.println("hello");// simulate the task doing some work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Task " + taskId + " finished.");
            });


            System.out.println("Submitted task " + taskId + " to the pool.");
        }

        // shutdown() tells the pool: "don't accept any NEW tasks, but
        // please finish all the ones already submitted."
        pool.shutdown();

        // awaitTermination() blocks the main thread until either:
        //   a) all submitted tasks have finished, OR
        //   b) the given timeout (10 seconds here) has passed.
        boolean finishedInTime = pool.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println("All tasks finished before timeout? " + finishedInTime);

        System.out.println();

        // ------------------------------------------------------------
        // PART 2: Getting RESULTS back from tasks (Callable + Future)
        // ------------------------------------------------------------
        // Runnable's run() method returns nothing (void). But often we
        // want a task to CALCULATE something and give us back a result.
        // That's what Callable<T> is for - its call() method returns a
        // value of type T. When you submit() a Callable, you get back
        // a Future<T>, which is like a "claim ticket" for a result that
        // will be ready later.
        System.out.println("--- Getting results back from tasks using Callable + Future ---");

        ExecutorService calculatorPool = Executors.newFixedThreadPool(3);
        List<Future<Integer>> futures = new ArrayList<>();

        // Submit 5 tasks that each calculate the square of a number
        for (int i = 1; i <= 5; i++) {
            final int number = i;

            Callable<Integer> squareTask = () -> {
                Thread.sleep(200); // simulate some work being done
                int result = number * number;
                System.out.println("Calculated square of " + number + " = " + result
                        + " on " + Thread.currentThread().getName());
                return result;
            };

            // submit() returns a Future immediately, even though the
            // actual calculation might not be done yet.
            Future<Integer> future = calculatorPool.submit(squareTask);
            futures.add(future);
        }

        // Now we collect all the results. future.get() will BLOCK
        // (wait) until that specific task is complete, then give us
        // its return value.
        int sumOfSquares = 0;
        for (Future<Integer> future : futures) {
            try {
                sumOfSquares += future.get(); // waits for result if not ready yet
            } catch (Exception e) {
                System.out.println("A task failed: " + e.getMessage());
            }
        }

        System.out.println("Sum of all squares (1^2 + 2^2 + ... + 5^2) = " + sumOfSquares);
        System.out.println("(Expected: 1 + 4 + 9 + 16 + 25 = 55)");

        calculatorPool.shutdown();

        System.out.println();
        System.out.println("Lesson 5 complete! Key takeaways:");
        System.out.println(" 1) ExecutorService manages a pool of reusable threads for you.");
        System.out.println(" 2) submit(Runnable) runs a task with no return value.");
        System.out.println(" 3) submit(Callable<T>) runs a task that returns a value, giving");
        System.out.println("    you a Future<T> you can later call .get() on to retrieve it.");
        System.out.println(" 4) Always call shutdown() when you're done, so the pool's threads");
        System.out.println("    can be cleaned up and your program can exit normally.");
    }
}
