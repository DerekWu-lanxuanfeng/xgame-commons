package org.xgame.commons.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @Name: AbstractThread.class
 * @Description: //
 * @Create: DerekWu on 2018/9/22 0:29
 * @Version: V1.0
 */
public abstract class AbstractThread<T> implements Runnable {

    private final Logger LOG = LogManager.getLogger(getClass());

    protected final String threadName;
    protected volatile boolean bShutDown;
    private Thread thread;
    private final boolean isDaemon;

    private AtomicLong countNum = new AtomicLong(0);

    protected AbstractThread(String threadName) {
        this.threadName = threadName;
        this.bShutDown = false;
        this.thread = null;
        this.isDaemon = false;
    }

    protected AbstractThread(String threadName, boolean isDaemon) {
        this.threadName = threadName;
        this.bShutDown = false;
        this.thread = null;
        this.isDaemon = isDaemon;
    }

    public synchronized void shutdown() {
        this.bShutDown = true;
        if (this.thread == null) {
            throw new Error("Thread not exsit! ");
        }
        LOG.info(this.threadName + " thread begin shutdown...");
    }

    public synchronized void start() {
        if (this.thread != null) {
            throw new Error("Thread is run!");
        }
        this.thread = new Thread(this, this.threadName);
        if (this.isDaemon) { //是否守护进程
            this.thread.setDaemon(true);
        }
        this.thread.start();
        LOG.info(this.threadName + " thread started...");
    }

    public long incrCountNum() {
        return this.countNum.incrementAndGet();
    }

    public long decrCountNum() {
        return this.countNum.decrementAndGet();
    }

    public long getCountNum() {
        return this.countNum.get();
    }

    protected void execute(T t) throws Exception {}

}
