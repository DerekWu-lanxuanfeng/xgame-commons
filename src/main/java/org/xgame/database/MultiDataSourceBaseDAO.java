package org.xgame.database;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xgame.database.datasource.MultiDataSourceManager;
import org.xgame.database.mybatis.Statements;
import org.xgame.database.mybatis.StatementsManager;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Name: MultiDataSourceBaseDAO.class
 * @Description: //
 * @Create: DerekWu on 2018/9/1 18:16
 * @Version: V1.0
 */
public class MultiDataSourceBaseDAO implements IMultiDataSourceBaseDAO {

    private Logger LOG = LogManager.getLogger(getClass());

    private MultiDataSourceManager multiDataSourceManager;

    private SqlSessionFactory sqlSessionFactory;

    private StatementsManager statementsManager;

    @Override
    public int insert(DataShardingBase dataShardingBase) {
        Statements statements = statementsManager.get(dataShardingBase.getClass());
        Connection connection = multiDataSourceManager.getConnection(dataShardingBase.getDbNum());
        SqlSession sqlSession = sqlSessionFactory.openSession(connection);
        return sqlSession.insert(statements.getInsertStatement(), dataShardingBase);
    }

    @Override
    public int update(DataShardingBase dataShardingBase) {
        Statements statements = statementsManager.get(dataShardingBase.getClass());
        Connection connection = multiDataSourceManager.getConnection(dataShardingBase.getDbNum());
        SqlSession sqlSession = sqlSessionFactory.openSession(connection);
        return sqlSession.update(statements.getUpdateStatement(), dataShardingBase);
    }

    @Override
    public int update(Short dbNum, Short tableNum, String statement, Map<String, Object> param) {
        param.put("tableFullNum", DataShardingUtils.getTableFullNum(dbNum, tableNum));
        Connection connection = multiDataSourceManager.getConnection(dbNum);
        SqlSession sqlSession = sqlSessionFactory.openSession(connection);
        return sqlSession.update(statement, param);
    }

    @Override
    public int delete(DataShardingBase dataShardingBase) {
        Statements statements = statementsManager.get(dataShardingBase.getClass());
        Connection connection = multiDataSourceManager.getConnection(dataShardingBase.getDbNum());
        SqlSession sqlSession = sqlSessionFactory.openSession(connection);
        return sqlSession.delete(statements.getDeleteStatement(), dataShardingBase);
    }

    @Override
    public int delete(Short dbNum, Short tableNum, String statement, Map<String, Object> param) {
        param.put("tableFullNum", DataShardingUtils.getTableFullNum(dbNum, tableNum));
        Connection connection = multiDataSourceManager.getConnection(dbNum);
        SqlSession sqlSession = sqlSessionFactory.openSession(connection);
        return sqlSession.delete(statement, param);
    }

    @Override
    public void batchDump(Short dbNum, List<DataShardingBase> dataShardingBaseList, DumpStat dumpStat) {
        Connection connection = multiDataSourceManager.getConnection(dbNum);
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, connection);
            for (DataShardingBase dataShardingBase : dataShardingBaseList) {
                Statements statements = statementsManager.get(dataShardingBase.getClass());
                if ((dataShardingBase.getInsertTimes() > 0L) && (dataShardingBase.getDeleteTimes() == 0L)) {
                    sqlSession.insert(statements.getInsertStatement(), dataShardingBase);
                    dataShardingBase.processInsertOver();
                    if (dumpStat != null ) dumpStat.insertIncr();
                } else if ((dataShardingBase.getUpdateTimes() > 0L) && (dataShardingBase.getInsertTimes() == 0L) && (dataShardingBase.getDeleteTimes() == 0L)) {
                    sqlSession.update(statements.getUpdateStatement(), dataShardingBase);
                    if (dumpStat != null ) dumpStat.updateIncr();
                } else if ((dataShardingBase.getInsertTimes() == 0L) && dataShardingBase.getDeleteTimes() > 0L) {
                    sqlSession.delete(statements.getDeleteStatement(), dataShardingBase);
                    if (dumpStat != null ) dumpStat.deleteIncr();
                }
            }
            sqlSession.commit();
        } catch (Exception e) {
            LOG.error("# batchDump err dbNum=" + dbNum, e);
            if (sqlSession != null) sqlSession.rollback();
        } finally {
            if (sqlSession != null) sqlSession.close();
        }
    }

    @Override
    public void batchDump(Map<Short, List<DataShardingBase>> dbMapList, Map<Short, DumpStat> dumpStatMap) {
        for (Map.Entry<Short, List<DataShardingBase>> oneEntry : dbMapList.entrySet()) {
            Short dbNum = oneEntry.getKey();

            List<DataShardingBase> dataShardingBaseList = oneEntry.getValue();

            //dump 统计
            DumpStat dumpStat = null;
            if (dumpStatMap != null) {
                dumpStat = dumpStatMap.get(dbNum);
                if (dumpStat == null) {
                    dumpStat = new DumpStat(dbNum);
                    dumpStatMap.put(dbNum, dumpStat);
                }
            }

            this.batchDump(dbNum, dataShardingBaseList, dumpStat);
        }
    }

    @Override
    public <T extends DataShardingBase> T selectOne(Short dbNum, Short tableNum, Object id, Class<T> clazz) {
        Statements statements = statementsManager.get(clazz);
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        return selectOne(dbNum, tableNum, statements.getSelectOneStatement(), param, clazz);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends DataShardingBase> T selectOne(Short dbNum, Short tableNum, String statement, Map<String, Object> param, Class<T> clazz) {
        param.put("tableFullNum", DataShardingUtils.getTableFullNum(dbNum, tableNum));
        Connection connection = multiDataSourceManager.getConnection(dbNum);
        SqlSession sqlSession = sqlSessionFactory.openSession(connection);
        Object obj = sqlSession.selectOne(statement, param);
        if (obj == null) {
            return null;
        }
        T oneDataShardingBase = (T) obj;
        oneDataShardingBase.setDbNum(dbNum);
        oneDataShardingBase.setTableNum(tableNum);
        return oneDataShardingBase;
    }

    @Override
    public <T> T selectObj(Short dbNum, Short tableNum, String statement, Map<String, Object> param, Class<T> clazz) {
        param.put("dbNum", dbNum);
        param.put("tableNum", tableNum);
        param.put("tableFullNum", DataShardingUtils.getTableFullNum(dbNum, tableNum));
        Connection connection = multiDataSourceManager.getConnection(dbNum);
        SqlSession sqlSession = sqlSessionFactory.openSession(connection);
        return sqlSession.selectOne(statement, param);
    }

    @Override
    public <E extends DataShardingBase> List<E> selectList(Short dbNum, Short tableNum, String statement, Map<String, Object> param, Class<E> clazz) {
        param.put("dbNum", dbNum);
        param.put("tableNum", tableNum);
        param.put("tableFullNum", DataShardingUtils.getTableFullNum(dbNum, tableNum));
        Connection connection = multiDataSourceManager.getConnection(dbNum);
        SqlSession sqlSession = sqlSessionFactory.openSession(connection);
        List<E> list = sqlSession.selectList(statement, param);
        for (DataShardingBase oneDataShardingBase : list) {
            oneDataShardingBase.setDbNum(dbNum);
            oneDataShardingBase.setTableNum(tableNum);
        }
        return list;
    }

    @Override
    public <T> List<T> selectObjList(Short dbNum, Short tableNum, String statement, Map<String, Object> param, Class<T> clazz) {
        param.put("dbNum", dbNum);
        param.put("tableNum", tableNum);
        param.put("tableFullNum", DataShardingUtils.getTableFullNum(dbNum, tableNum));
        Connection connection = multiDataSourceManager.getConnection(dbNum);
        SqlSession sqlSession = sqlSessionFactory.openSession(connection);
        return sqlSession.selectList(statement, param);
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public MultiDataSourceManager getMultiDataSourceManager() {
        return multiDataSourceManager;
    }

    public void setMultiDataSourceManager(MultiDataSourceManager multiDataSourceManager) {
        this.multiDataSourceManager = multiDataSourceManager;
    }

    public StatementsManager getStatementsManager() {
        return statementsManager;
    }

    public void setStatementsManager(StatementsManager statementsManager) {
        this.statementsManager = statementsManager;
    }

}
