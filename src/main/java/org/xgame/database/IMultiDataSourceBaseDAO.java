package org.xgame.database;

import org.xgame.database.DataShardingBase;

import java.util.List;
import java.util.Map;

/**
 * @Name: IMultiDataSourceBaseDAO.class
 * @Description: //
 * @Create: DerekWu on 2018/9/1 18:13
 * @Version: V1.0
 */
public interface IMultiDataSourceBaseDAO {

    int insert(DataShardingBase dataShardingBase);

    int update(DataShardingBase dataShardingBase);

    int update(Short dbNum, Short tableNum, String statement, Map<String, Object> paramMap);

    int delete(DataShardingBase dataShardingBase);

    int delete(Short dbNum, Short tableNum, String statement, Map<String, Object> paramMap);

    void batchDump(Short dbNum, List<DataShardingBase> dataShardingBaseList, DumpStat dumpStat);

    void batchDump(Map<Short, List<DataShardingBase>> paramMap, Map<Short, DumpStat> dumpStatMap);

    <T extends DataShardingBase> T selectOne(Short dbNum, Short tableNum, Object id, Class<T> paramClass);

    <T extends DataShardingBase> T selectOne(Short dbNum, Short tableNum, String statement, Map<String, Object> paramMap, Class<T> paramClass);

    <T> T selectObj(Short dbNum, Short tableNum, String statement, Map<String, Object> paramMap, Class<T> paramClass);

    <T extends DataShardingBase> List<T> selectList(Short dbNum, Short tableNum, String statement, Map<String, Object> paramMap, Class<T> paramClass);

    <T> List<T> selectObjList(Short dbNum, Short tableNum, String statement, Map<String, Object> paramMap, Class<T> paramClass);
    
}
