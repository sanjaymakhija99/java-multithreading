/**
 * ============================================================
 * LESSON 4: THREAD COMMUNICATION (wait / notify)
 * ============================================================
 *
 * Sometimes one thread needs to PAUSE and wait for another thread
 * to tell it "okay, you can continue now." This is called thread
 * communication or coordination.
 *
 * Java gives us three methods on every Object for this purpose:
 *
 *   - wait()    : Causes the CURRENT thread to release the lock it
 *                 holds and pause, until another thread calls
 *                 notify() or notifyAll() on the SAME object.
 *   - notify()  : Wakes up ONE thread that is waiting on this object.
 *   - notifyAll(): Wakes up ALL threads waiting on this object.
 *
 * IMPORTANT RULE: wait(), notify(), and notifyAll() can ONLY be
 * called from INSIDE a synchronized block/method for that same
 * object. Otherwise Java throws an IllegalMonitorStateException.
 *
 * This lesson implements a simple "message box" that can hold
 * ONE message at a time:
 *   - A "Producer" thread puts a message in the box.
 *   - A "Consumer" thread takes the message out.
 *   - If the box is already full, the producer must WAIT.
 *   - If the box is empty, the consumer must WAIT.
 */
public class Lesson4_ThreadCommunication {

    // A box that can hold exactly one message at a time.
    static class MessageBox {
        private String message;
        private boolean hasMessage = false; // tracks whether the box is full or empty

        // Producer calls this to put a message in the box
        public synchronized void putMessage(String newMessage) throws InterruptedException {
            // While the box already has an unread message, wait.
            // We use a WHILE loop (not an "if") because when this thread
            // wakes up, it should RE-CHECK the condition - it's possible
            // another thread grabbed the spot first (this is standard
            // best practice for using wait()).
            while (hasMessage) {
                System.out.println("[Producer] Box is full, waiting for it to be emptied...");
                wait(); // releases the lock and pauses this thread
            }

            this.message = newMessage;
            this.hasMessage = true;
            System.out.println("[Producer] Put message into box: \"" + newMessage + "\"");

            // Wake up any thread waiting on this object (e.g. the consumer
            // that might be waiting for a message to appear).
            notifyAll();
        }

        // Consumer calls this to take the message out of the box
        public synchronized String takeMessage() throws InterruptedException {
            // While the box is empty, wait for the producer to fill it.
            while (!hasMessage) {
                System.out.println("[Consumer] Box is empty, waiting for a message...");
                wait();
            }

            hasMessage = false;
            System.out.println("[Consumer] Took message from box: \"" + message + "\"");

            // Wake up the producer, who might be waiting for the box to
            // become empty so it can put in the NEXT message.
            notifyAll();

            return message;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== LESSON 4: Thread Communication (wait/notify) ===\n");

        MessageBox box = new MessageBox();
        String[] messagesToSend = {"Hello", "How are you?", "Goodbye"};

        // Producer thread: sends 3 messages, one at a time
        Thread producer = new Thread(() -> {
            for (String msg : messagesToSend) {
                try {
                    box.putMessage(msg);
                    Thread.sleep(300); // simulate time between messages
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "Producer-Thread");

        // Consumer thread: reads 3 messages, one at a time
        Thread consumer = new Thread(() -> {
            for (int i = 0; i < messagesToSend.length; i++) {
                try {
                    box.takeMessage();
                    Thread.sleep(500); // consumer is a bit slower than producer
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "Consumer-Thread");

        consumer.start();
        producer.start();

        producer.join();
        consumer.join();

        System.out.println();
        System.out.println("Lesson 4 complete! Key takeaways:");
        System.out.println(" 1) wait() pauses a thread and releases its lock until notified.");
        System.out.println(" 2) notify()/notifyAll() wake up threads that are waiting.");
        System.out.println(" 3) Always re-check your condition in a WHILE loop after waking up,");
        System.out.println("    since conditions can change before you actually get to run.");
        System.out.println(" 4) In real projects, java.util.concurrent classes like");
        System.out.println("    BlockingQueue handle this pattern for you automatically -");
        System.out.println("    but it's important to understand what happens underneath!");
    }
}
