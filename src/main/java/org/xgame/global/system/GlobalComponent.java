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
    private SystemStateEnum systemState = null;

    /** 系统开始启动的时间 */
    private long startingTimes;

    /** 系统开始运行的时间 */
    private long runningTimes;
 
    /** 系统开始停止时间 */
    private long stoppingTimes;

    public synchronized void start() {
        if (this.systemState == null) {
            this.systemState = SystemStateEnum.STARTING;
            this.startingTimes = System.currentTimeMillis();
            LOG.info("########### GlobalComponent.start() START #########");
        } else {
            LOG.error("########### GlobalComponent.start() ERROR #########");
        }
    }

    public synchronized void startSuccess() {
        if (this.systemState != null && SystemStateEnum.STARTING.equals(this.systemState)) {
            this.systemState = SystemStateEnum.RUNNING;
            this.runningTimes = System.currentTimeMillis();
            LOG.info("########### GlobalComponent.startSuccess() SUCCESS #########");
        } else {
            LOG.error("########### GlobalComponent.startSuccess() ERROR #########");
        }
    }

    public synchronized void stop() {
        if (this.isRunning()) {
            this.systemState = SystemStateEnum.STOPPING;
            this.stoppingTimes = System.currentTimeMillis();
            LOG.info("########### GlobalComponent.stop() SUCCESS #########");
        } else {
            LOG.error("########### GlobalComponent.stop() ERROR #########");
        }
    }

    public boolean isRunning() {
        if (this.systemState != null && SystemStateEnum.RUNNING.equals(this.systemState)) {
            return true;
        }
        return false;
    }

    public boolean isStoping() {
        if (this.systemState != null && SystemStateEnum.STOPPING.equals(this.systemState)) {
            return true;
        }
        return false;
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
