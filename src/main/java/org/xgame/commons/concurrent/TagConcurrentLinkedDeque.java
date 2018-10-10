package org.xgame.commons.concurrent;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Name: TagConcurrentLinkedDeque.class
 * @Description: // 标记同步链表队列
 * @Create: DerekWu on 2018/8/27 23:30
 * @Version: V1.0
 */
public class TagConcurrentLinkedDeque<E> {

    /** 标记 */
    private String tag;
    /** 同步链表队列 */
    private ConcurrentLinkedDeque<E> linkedDeque = new ConcurrentLinkedDeque<>();

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
    public boolean applyUseDeque() {
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
     * 添加到队列尾部
     * @param e
     * @return
     */
    public boolean addLast(E e) {
        return this.linkedDeque.offerLast(e);
    }

    /**
     * 添加到队列头部
     * @param e
     * @return
     */
    public boolean addFirst(E e) {
        return this.linkedDeque.offerFirst(e);
    }

    /**
     * 队列队列的头部取出一个
     * @return 队列为空则返回 null
     */
    public E pollFirst() {
        return this.linkedDeque.pollFirst();
    }

    /**
     * 队列队列的尾部取出一个
     * @return 队列为空则返回 null
     */
    public E pollLast() {
        return this.linkedDeque.pollLast();
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

    public long getMaxUseTimes() {
        return maxUseTimes;
    }

    public void setMaxUseTimes(long maxUseTimes) {
        this.maxUseTimes = maxUseTimes;
    }

    public boolean isEmpty() {
        return linkedDeque.isEmpty();
    }

    public void useOver() {
        this.inUseLock.set(false);
    }

}
