package org.xgame.commons.utils.statistics;

/**
 * @Name: StatisticsValue.class
 * @Description: // 统计的值
 * @Create: DerekWu on 2018/10/13 23:32
 * @Version: V1.0
 */
public class StatisticsValue {

    private final long key;

    private long value = 0;

    public StatisticsValue(long key) {
        this.key = key;
    }

    public void increment() {
        ++this.value;
    }

    public void decrement() {
        --this.value;
    }

    public long getKey() {
        return key;
    }

    public long getValue() {
        return this.value;
    }

}
