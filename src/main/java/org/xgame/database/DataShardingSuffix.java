package org.xgame.database;

/**
 * @Name: DataShardingSuffix.class
 * @Description: //
 * @Create: DerekWu on 2018/10/28 20:37
 * @Version: V1.0
 */
public abstract class DataShardingSuffix extends DataShardingBase {

    private String tableSuffix;

    public DataShardingSuffix(Short dbNum, Short tableNum) {
        super(dbNum, tableNum);
    }

    public String getTableSuffix() {
        return tableSuffix;
    }

    public void setTableSuffix(String tableSuffix) {
        this.tableSuffix = tableSuffix;
    }

}
