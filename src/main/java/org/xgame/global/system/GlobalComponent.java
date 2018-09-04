package org.xgame.global.system;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Name: GlobalData.class
 * @Description: // 全局数据
 * @Create: DerekWu on 2018/9/2 17:55
 * @Version: V1.0
 */
public class GlobalComponent {

    private final Logger LOG = LogManager.getLogger(getClass());

    /** 系统状态 */
    private SystemStateEnum systemState = SystemStateEnum.STOP;

    /** 系统开始启动的时间 */
    private long startingTimes;

    /** 系统开始运行的时间 */
    private long runningTimes;
 
    /** 系统开始停止时间 */
    private long stoppingTimes;

    public synchronized void start() {
        if (SystemStateEnum .STOP.equals(this.systemState)) {
            this.systemState = SystemStateEnum.STARTING;
            this.startingTimes = System.currentTimeMillis();
        } else {
            LOG.error("########### GlobalComponent.start() ERROR #########");
        }
    }

    public synchronized void startSuccess() {
        if (SystemStateEnum.STARTING.equals(this.systemState)) {
            this.systemState = SystemStateEnum.RUNNING;
            this.runningTimes = System.currentTimeMillis();
        } else {
            LOG.error("########### GlobalComponent.startSuccess() ERROR #########");
        }
    }

    public synchronized void stop() {
        if (SystemStateEnum.RUNNING.equals(this.systemState)) {
            this.systemState = SystemStateEnum.STOPPING;
            this.stoppingTimes = System.currentTimeMillis();
        } else {
            LOG.error("########### GlobalComponent.stop() ERROR #########");
        }
    }

    public SystemStateEnum getSystemState() {
        return systemState;
    }

    public long getStartingTimes() {
        return startingTimes;
    }

    public long getRunningTimes() {
        return runningTimes;
    }

    public long getStoppingTimes() {
        return stoppingTimes;
    }

}
