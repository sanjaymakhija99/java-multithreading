/**
 * ============================================================
 * LESSON 3: RACE CONDITIONS & SYNCHRONIZATION
 * ============================================================
 *
 * THE PROBLEM:
 * When multiple threads read and modify the SAME shared piece of
 * data (like a variable) at the same time, without any protection,
 * you can get a "race condition" - the final result becomes
 * unpredictable and often WRONG.
 *
 * Why does this happen? Because something as simple as "counter++"
 * is NOT a single atomic (indivisible) operation. It's actually
 * THREE steps:
 *   1) READ the current value of counter
 *   2) ADD 1 to it
 *   3) WRITE the new value back to counter
 *
 * If two threads interleave these steps, one thread's update can
 * get "lost". Example with counter starting at 0:
 *   Thread A reads counter (0)
 *   Thread B reads counter (0)      <-- both read the SAME old value!
 *   Thread A computes 0 + 1 = 1, writes 1
 *   Thread B computes 0 + 1 = 1, writes 1
 *   Final result: counter = 1  (WRONG! It should be 2, since both
 *   threads tried to increment it once each)
 *
 * THE FIX: the 'synchronized' keyword.
 * Marking a method (or block) as 'synchronized' means: "only ONE
 * thread may execute this code at a time, for a given lock object."
 * Any other thread that tries to enter will be BLOCKED until the
 * first thread finishes and releases the lock.
 */
public class Lesson3_RaceConditionsAndSynchronization {

    // A simple counter class WITHOUT synchronization - this is unsafe!
    static class UnsafeCounter {
        private int count = 0;

        // NOT synchronized - multiple threads calling this at the same
        // time can interfere with each other (see explanation above).
        public void increment() {
            count++; // looks like one step, but is really read-modify-write
        }

        public int getCount() {
            return count;
        }
    }

    // The SAME counter, but fixed using the 'synchronized' keyword.
    static class SafeCounter {
        private int count = 0;

        // 'synchronized' here means: only one thread can be executing
        // this increment() method AT A TIME (for a given SafeCounter
        // instance). Any other thread calling increment() on the SAME
        // object must wait its turn.
        public synchronized void increment() {
            count++;
        }

        public synchronized int getCount() {
            return count;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== LESSON 3: Race Conditions & Synchronization ===\n");

        final int NUM_THREADS = 10;
        final int INCREMENTS_PER_THREAD = 1000;
        // Expected final result should be: 10 threads * 1000 increments = 10,000

        // ------------------------------------------------------------
        // PART 1: Demonstrate the BROKEN unsynchronized counter
        // ------------------------------------------------------------
        System.out.println("--- Testing UNSAFE counter (no synchronization) ---");
        UnsafeCounter unsafeCounter = new UnsafeCounter();
        Thread[] unsafeThreads = new Thread[NUM_THREADS];

        for (int i = 0; i < NUM_THREADS; i++) {
            unsafeThreads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    unsafeCounter.increment();
                }
            });
        }

        // Start all threads
        for (Thread t : unsafeThreads) {
            t.start();
        }
        // Wait for all threads to finish before checking the result
        for (Thread t : unsafeThreads) {
            t.join();
        }

        System.out.println("Expected count: " + (NUM_THREADS * INCREMENTS_PER_THREAD));
        System.out.println("Actual count:   " + unsafeCounter.getCount());
        System.out.println("(Notice this is often LESS than expected - some increments were lost!)\n");

        // ------------------------------------------------------------
        // PART 2: Demonstrate the FIXED synchronized counter
        // ------------------------------------------------------------
        System.out.println("--- Testing SAFE counter (with synchronization) ---");
        SafeCounter safeCounter = new SafeCounter();
        Thread[] safeThreads = new Thread[NUM_THREADS];

        for (int i = 0; i < NUM_THREADS; i++) {
            safeThreads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    safeCounter.increment();
                }
            });
        }

        for (Thread t : safeThreads) {
            t.start();
        }
        for (Thread t : safeThreads) {
            t.join();
        }

        System.out.println("Expected count: " + (NUM_THREADS * INCREMENTS_PER_THREAD));
        System.out.println("Actual count:   " + safeCounter.getCount());
        System.out.println("(This should ALWAYS match exactly, thanks to 'synchronized'!)\n");

        System.out.println("Lesson 3 complete! Key takeaways:");
        System.out.println(" 1) Shared mutable data accessed by multiple threads needs protection.");
        System.out.println(" 2) 'synchronized' ensures only one thread executes the guarded code");
        System.out.println("    at a time, preventing lost updates.");
        System.out.println(" 3) Synchronization has a performance cost (threads must wait their");
        System.out.println("    turn), so only synchronize the code that truly needs it.");
    }
}
