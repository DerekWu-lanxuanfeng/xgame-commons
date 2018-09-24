package org.xgame.database;

/**
 * @Name: DataShardingBase.class
 * @Description: // 基础分片数据对象
 * @Create: DerekWu on 2018/9/1 12:09
 * @Version: V1.0
 */
public abstract class DataShardingBase {

    /** 数据库编号 */
    private Short dbNum;
    /** 表编号 */
    private Short tableNum;
    /** 插入、修改、删除 的时间 */
    private long insertTimes;
    private long updateTimes;
    private long deleteTimes;

    protected DataShardingBase(){}

    public DataShardingBase(Short dbNum, Short tableNum) {
        this.dbNum = dbNum;
        this.tableNum = tableNum;
    }

    /**
     * 子类实现
     * @return
     */
    public abstract Object id();

    public Short getDbNum() {
        return dbNum;
    }

    public void setDbNum(Short dbNum) {
        this.dbNum = dbNum;
    }

    public Short getTableNum() {
        return tableNum;
    }

    public void setTableNum(Short tableNum) {
        this.tableNum = tableNum;
    }

    public Integer getTableFullNum() {
        return DataShardingUtils.getTableFullNum(dbNum, tableNum);
    }

    public void flagInsert() throws DataShardingException {
        synchronized (this) {
            if ((this.insertTimes == 0L) && (this.updateTimes == 0L) && (this.deleteTimes == 0L)) {
                this.insertTimes = System.currentTimeMillis();
            } else {
                throw new DataShardingException("# flagInsert error.");
            }
        }
    }

    public void flagUpdate() throws DataShardingException {
        if (this.deleteTimes == 0L) {
            this.updateTimes = System.currentTimeMillis();
        } else {
            throw new DataShardingException("# flagUpdate error, this data is deleted.");
        }
    }

    public void flagDelete() throws DataShardingException {
        synchronized (this) {
            if (this.deleteTimes == 0L) {
                this.deleteTimes = System.currentTimeMillis();
            } else {
                throw new DataShardingException("# flagDelete error, this data is deleted.");
            }
        }
    }

    protected long getInsertTimes() {
        return this.insertTimes;
    }

    protected long getUpdateTimes() {
        return this.updateTimes;
    }

    protected long getDeleteTimes() {
        return this.deleteTimes;
    }

    protected void processInsertOver() {
        this.updateTimes = System.currentTimeMillis();
        this.insertTimes = 0L;
    }
    
}
