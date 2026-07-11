# Java Multithreading — Beginner's Guide

A step-by-step Java tutorial that teaches core multithreading
concepts through **heavily commented, runnable code**. Every file
explains not just *what* the code does, but *why*, in plain language
for someone new to threads.

## Lessons (`src/`)

| File | Concept |
|---|---|
| `Lesson1_CreatingThreads.java` | The two main ways to create a thread: extending `Thread`, and implementing `Runnable` (plus the lambda shortcut). Explains `start()` vs `run()` and `join()`. |
| `Lesson2_ThreadLifecycle.java` | The 6 states every thread passes through: `NEW`, `RUNNABLE`, `BLOCKED`, `WAITING`, `TIMED_WAITING`, `TERMINATED` — printed live as they happen. |
| `Lesson3_RaceConditionsAndSynchronization.java` | What a race condition is and why it happens, demonstrated with an unsafe counter vs a `synchronized` counter. |
| `Lesson4_ThreadCommunication.java` | How threads coordinate using `wait()` / `notifyAll()`, built as a simple producer/consumer message box. |
| `Lesson5_ThreadPools.java` | Why creating threads manually doesn't scale, and how `ExecutorService`, `Callable`, and `Future` solve it. |
| `Main.java` | Menu to run any lesson individually, or all of them in order. |

## How to run

You need a JDK installed (Java 11+ recommended).

```bash
cd src
javac *.java
java Main
```

You'll be asked to pick a lesson (1-5), or `6` to run them all back
to back. To run everything without typing anything:

```bash
echo "6" | java Main
```

## How to get the most out of this

Open the lesson's `.java` file in your editor **while its output is
printing in the terminal**. Every non-trivial line has a comment
explaining what it does and why it matters — matching the printed
output to the code that produced it is the fastest way to build a
real mental model of how threads behave.

## Suggested learning order

1. **Lesson 1** — learn how to even create and start a thread.
2. **Lesson 2** — understand what "state" a thread is in at any moment.
3. **Lesson 3** — see why shared data is dangerous, and how to fix it.
4. **Lesson 4** — learn how threads can signal each other instead of
   just guessing/polling.
5. **Lesson 5** — graduate to the tools professionals actually use
   day-to-day (thread pools) instead of managing raw `Thread` objects.

Once you're comfortable with all 5 lessons, you'll have the
foundation needed to understand higher-level concurrency tools like
`CompletableFuture`, `ConcurrentHashMap`, and reactive frameworks.
