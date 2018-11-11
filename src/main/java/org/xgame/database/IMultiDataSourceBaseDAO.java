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

    // int update(DataShardingBase dataShardingBase, String statement);

    int update(Short dbNum, Short tableNum, String statement, Map<String, Object> paramMap);

    int delete(DataShardingBase dataShardingBase);

    int delete(Short dbNum, Short tableNum, String statement, Map<String, Object> paramMap);

    <T extends DataShardingBase> void batchDump(Short dbNum, List<T> dataShardingBaseList);

    <T extends DataShardingBase> void batchDump(Short dbNum, List<T> dataShardingBaseList, DumpStat dumpStat);

    <T extends DataShardingBase> void batchDump(Map<Short, List<T>> paramMap, Map<Short, DumpStat> dumpStatMap);

    <T extends DataShardingBase> T selectOne(Short dbNum, Short tableNum, Object id, Class<T> paramClass);

    <T extends DataShardingBase> T selectOne(Short dbNum, Short tableNum, String statement, Map<String, Object> paramMap, Class<T> paramClass);

    <T> T selectObj(Short dbNum, Short tableNum, String statement, Map<String, Object> paramMap, Class<T> paramClass);

    <T> T selectObjByCustom(Short dbNum, String statement, Object paramObj, Class<T> paramClass);

    <E extends DataShardingBase> List<E> selectList(Short dbNum, Short tableNum, Map<String, Object> paramMap, Class<E> paramClass);

    <E extends DataShardingBase> List<E> selectList(Short dbNum, Short tableNum, String statement, Map<String, Object> paramMap, Class<E> paramClass);

    <E> List<E> selectObjList(Short dbNum, Short tableNum, String statement, Map<String, Object> paramMap, Class<E> paramClass);

    <E> List<E> selectObjListByCustomQueryParam(Short dbNum, String statement, Object paramObj, Class<E> paramClass);
    
}
