package org.xgame.commons.concurrent.queue;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Name: TagConcurrentLinkedQueue.class
 * @Description: // 标记同步链表队列
 * @Create: DerekWu on 2018/8/27 23:30
 * @Version: V1.0
 */
public class TagConcurrentLinkedQueue<E> {

    /** 标记 */
    private String tag;
    /** 同步链表队列 */
    private ConcurrentLinkedQueue<E> linkedQueue = new ConcurrentLinkedQueue<>();

    /** 使用中的锁 */
    private AtomicBoolean inUseLock = new AtomicBoolean(false);
    /** 最后使用时间 */
    private volatile long lastUseTimes = 0;
    /** 使用锁最多使用时间 */
    private volatile long maxUseTimes = 5000;

    /**
     * 申请使用队列，返回申请成功或者失败
     * @return
     */
    public boolean applyUseQueue() {
        if (!inUseLock.get() || System.currentTimeMillis() > lastUseTimes + maxUseTimes) {
            // 只能一个线城上锁成功
            boolean isLocked = inUseLock.compareAndSet(false, true);
            if (isLocked) {
                this.lastUseTimes = System.currentTimeMillis();
            }
            return isLocked;
        }
        return false;
    }

    /**
     * 添加到队列
     * @param e
     * @return
     */
    public boolean add(E e) {
        return this.linkedQueue.offer(e);
    }

    /**
     * 队列队列的头部取出一个
     * @return 队列为空则返回 null
     */
    public E poll() {
        return this.linkedQueue.poll();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getLastUseTimes() {
        return lastUseTimes;
    }

    public void setLastUseTimes(long lastUseTimes) {
        this.lastUseTimes = lastUseTimes;
    }
}
