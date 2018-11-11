package org.xgame.commons.utils.statistics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Name: TimeConsumingStatistics.class
 * @Description: // 耗时统计
 * @Create: DerekWu on 2018/10/13 23:20
 * @Version: V1.0
 */
public class TimeConsumingStatistics {

    private final Logger LOG = LogManager.getLogger(getClass());

    /** 名字 */
    private final String name;
    /** 是否支持并发 */
    private final boolean isConcurrent;
    /** 打印间隔时间(毫秒) */
    private long printIntervalTime = 60 * 60 * 1000;
//    private long printIntervalTime = 60 * 1000;
    /** 最后一次打印时间 */
    private long lastPrintTimes = System.currentTimeMillis();
    /** 最短时间 */
    private long minTimes = Long.MAX_VALUE;
    private String minTimesDesc;
    /** 最长时间 */
    private long maxTimes = -1;
    private String maxTimesDesc;
    /** 并发锁 */
    private AtomicBoolean lock = new AtomicBoolean(false);

    /** 统计Map, 时间毫秒 为 key ，value 为数量 */
    private final Map<Long, StatisticsValue> statisticsMap = new HashMap<>();

    /** 排序 */
    private final static Comparator<StatisticsValue> STATISTICS_VALUE_COMPARATOR = (o1, o2) -> {
        if (o1.getKey() < o2.getKey()) {
            return -1;
        }
        return 1;
    };

    public TimeConsumingStatistics(String name, boolean isConcurrent) {
        this.name = name;
        this.isConcurrent = isConcurrent;
    }

    public long getPrintIntervalTime() {
        return printIntervalTime;
    }

    public void setPrintIntervalTime(long printIntervalTime) {
        this.printIntervalTime = printIntervalTime;
    }

    public void addStatistics(long times) {
        if (times < 0) {
            return;
        }
        this.addStatistics(times, null);
    }

    public void addStatistics(long times, String desc) {
        long statisticsKey = this.getStatisticsKey(times);
        boolean isNeedPrint;
        if (this.isConcurrent) {
            for (;;) {
                boolean isLocked = this.lock.compareAndSet(false, true);
                if (isLocked) {
                    try {
                        isNeedPrint = this.increment(statisticsKey, times, desc);
                    } finally {
                        this.lock.set(false);
                    }
                    break;
                }
            }
        } else {
            isNeedPrint = this.increment(statisticsKey, times, desc);
        }
        if (isNeedPrint) {
            this.printInfo();
        }
    }

    private long getStatisticsKey(long times) {
        if (times < 10) {
            return times;
        } else if (times < 20) {
            return times - times % 2;
        } else if (times < 50) {
            return times - times % 5;
        } else if (times < 100) {
            return times - times % 10;
        } else if (times < 200) {
            return times - times % 20;
        } else if (times < 400) {
            return times - times % 50;
        } else if (times < 1000) {
            return times - times % 100;
        } else if (times < 2000) {
            return times - times % 200;
        } else if (times < 5000) {
            return times - times % 500;
        } else if (times < 10000) {
            return times - times % 1000;
        } else if (times < 20000) {
            return times - times % 2000;
        } else if (times < 50000) {
            return times - times % 5000;
        } else if (times < 100000) {
            return times - times % 10000;
        } else if (times < 200000) {
            return times - times % 20000;
        } else if (times < 600000) {
            return times - times % 60000;
        } else {
            return 600000;
        }
    }

    private boolean increment(long statisticsKey, long times, String desc) {
        if (times < this.minTimes) {
            this.minTimes = times;
            this.minTimesDesc = desc;
        }
        if (times > this.maxTimes) {
            this.maxTimes = times;
            this.maxTimesDesc = desc;
        }
        StatisticsValue statisticsValue = this.statisticsMap.get(statisticsKey);
        if (statisticsValue == null) {
            statisticsValue = new StatisticsValue(statisticsKey);
            this.statisticsMap.put(statisticsKey, statisticsValue);
        }
        statisticsValue.increment();
        return this.isNeedPrint();
    }

    private boolean isNeedPrint() {
        long currTimes = System.currentTimeMillis();
        if (currTimes - this.lastPrintTimes >= this.printIntervalTime) {
            this.lastPrintTimes = currTimes;
            return true;
        } else {
            return false;
        }
    }

    private void printInfo() {
        List<StatisticsValue> statisticsValueList = new ArrayList<>();
        for (Map.Entry<Long, StatisticsValue> entry : statisticsMap.entrySet()) {
            statisticsValueList.add(entry.getValue());
        }
        statisticsValueList.sort(TimeConsumingStatistics.STATISTICS_VALUE_COMPARATOR);
        LOG.info("## TimeConsumingStatistics print START name = {} isConcurrent = {}", this.name, this.isConcurrent);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < statisticsValueList.size(); ++i) {
            StatisticsValue statisticsValue = statisticsValueList.get(i);
            sb.append("[").append(statisticsValue.getKey()).append(":").append(statisticsValue.getValue()).append("]");
            if (i > 1 && i % 10 == 0) {
                LOG.info(sb.toString());
                sb = new StringBuilder();
            }
        }
        if (sb.length() > 0) {
            LOG.info(sb.toString());
        }
        LOG.info("minTimes = {}, desc = {}", this.minTimes, this.minTimesDesc);
        LOG.info("maxTimes = {}, desc = {}", this.maxTimes, this.maxTimesDesc);
        LOG.info("## TimeConsumingStatistics print END name = {}", this.name);
    }

//    private class TimeConsumingNode {
//        private long times;
//        private long num;
//
//        public TimeConsumingNode(long times, long num) {
//            this.times = times;
//            this.num = num;
//        }
//
//        public long getTimes() {
//            return times;
//        }
//
//        public long getNum() {
//            return num;
//        }
//
//    }



}
