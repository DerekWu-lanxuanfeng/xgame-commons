package org.xgame.database;

/**
 * @Name: DumpStat.class
 * @Description: // dump 数据统计
 * @Create: DerekWu on 2018/9/1 22:19
 * @Version: V1.0
 */
public class DumpStat {

    private final Short dbNum;
    private int insertCount;
    private int updateCount;
    private int deleteCount;

    public DumpStat(Short dbNum) {
        this.dbNum = dbNum;
    }

    public Short getDbNum() {
        return dbNum;
    }

    public int getInsertCount() {
        return insertCount;
    }

    public int getUpdateCount() {
        return updateCount;
    }

    public int getDeleteCount() {
        return deleteCount;
    }

    public void reset() {
        insertCount = 0;
        updateCount = 0;
        deleteCount = 0;
    }

    public void insertIncr() {
        ++insertCount;
    }

    public void updateIncr() {
        ++updateCount;
    }

    public void deleteIncr() {
        ++deleteCount;
    }

    @Override
    public String toString() {
        return "DumpStat{" +
                "dbNum=" + dbNum +
                ", insertNumer=" + insertCount +
                ", updateNumer=" + updateCount +
                ", deleteNumer=" + deleteCount +
                '}';
    }
    
}
