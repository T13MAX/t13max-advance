package com.t13max.game.player;


import com.t13max.game.run.Application;

import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;


public class ActionQueue {

    private ActionExecutor executor;
    private ConcurrentLinkedQueue<Runnable> queue;
    private AtomicBoolean isRunning;

    public ActionQueue(ActionExecutor executor) {
        this.executor = executor;
        this.queue = new ConcurrentLinkedQueue<>();
        this.isRunning = new AtomicBoolean(false);
    }

    public void checkIn(Action action) {
        this.queue.offer(action);

        if (this.isRunning.compareAndSet(false, true)) {
            this.execNext();
        }
    }

    public void execute(JobName jobName, Runnable action) {
        checkIn(new Action(this, jobName) {
            @Override
            protected void exec() {
                try {
                    action.run();
                } catch (Throwable e) {
                    Application.autoLogger().error("ActionQueue.execute error jobName:{} exception:{}", jobName, e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    public void execute(Runnable action) {
        this.execute(JobName.NONAME, action);
    }

    private Runnable execNext() {
        Runnable next = this.queue.peek();
        if (Objects.nonNull(next) && next instanceof Action) {
        }
        if (next != null) {
            executor.execute(next);
        } else {
            this.isRunning.set(false);

            //double check
            next = this.queue.peek();
            if (next != null && this.isRunning.compareAndSet(false, true)) {
                executor.execute(next);
            }
        }
        return next;
    }

    void checkout(Runnable action) {
        this.queue.poll();
        this.execNext();
    }

    public int size() {
        return executor.queueSize();
    }

    public int queueSize() {
        return queue.size();
    }

}
