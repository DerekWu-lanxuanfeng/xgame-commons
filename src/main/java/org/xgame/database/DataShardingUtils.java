package org.xgame.database;

/**
 * @Name: DataShardingUtils.class
 * @Description: //
 * @Create: DerekWu on 2018/9/1 19:01
 * @Version: V1.0
 */
public class DataShardingUtils {

    /**
     * 获得表全名编号，用来操作数据库的时候用
     * @param dbNum 10001 - 30000
     * @param tableNum 1-9999
     * @return
     */
    public static int getTableFullNum(short dbNum, short tableNum){
        return dbNum * 10000 + tableNum;
    }

}
