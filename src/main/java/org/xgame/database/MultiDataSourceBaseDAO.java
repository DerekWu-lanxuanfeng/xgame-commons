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
import java.sql.SQLException;
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

    /**
     * 关闭
     * @param sqlSession
     * @param connection
     */
    private void close(SqlSession sqlSession, Connection connection) {
        if (sqlSession != null) {
            sqlSession.close();
        }
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                LOG.error(e);
            }
        }
    }

    @Override
    public int insert(DataShardingBase dataShardingBase) {
        Statements statements = statementsManager.get(dataShardingBase.getClass());
        Connection connection = null;
        SqlSession sqlSession = null;
        try {
            connection = multiDataSourceManager.getConnection(dataShardingBase.getDbNum());
            sqlSession = sqlSessionFactory.openSession(connection);
            return sqlSession.insert(statements.getInsertStatement(), dataShardingBase);
        } catch (Exception e) {
            LOG.error("# insert "+ dataShardingBase.getClass().getSimpleName() +" error.", e);
        } finally {
            this.close(sqlSession, connection);
        }
        return 0;
    }

    @Override
    public int update(DataShardingBase dataShardingBase) {
        Statements statements = statementsManager.get(dataShardingBase.getClass());
        Connection connection = null;
        SqlSession sqlSession = null;
        try {
            connection = multiDataSourceManager.getConnection(dataShardingBase.getDbNum());
            sqlSession = sqlSessionFactory.openSession(connection);
            return sqlSession.update(statements.getUpdateStatement(), dataShardingBase);
        } catch (Exception e) {
            LOG.error("# update "+ dataShardingBase.getClass().getSimpleName() +" error.", e);
        } finally {
            this.close(sqlSession, connection);
        }
        return 0;
    }

    @Override
    public int update(Short dbNum, Short tableNum, String statement, Map<String, Object> param) {
        param.put("tableFullNum", DataShardingUtils.getTableFullNum(dbNum, tableNum));
        Connection connection = null;
        SqlSession sqlSession = null;
        try {
            connection = multiDataSourceManager.getConnection(dbNum);
            sqlSession = sqlSessionFactory.openSession(connection);
            return sqlSession.update(statement, param);
        } catch (Exception e) {
            LOG.error("# update statement error.", e);
        } finally {
            this.close(sqlSession, connection);
        }
        return 0;
    }

    @Override
    public int delete(DataShardingBase dataShardingBase) {
        Statements statements = statementsManager.get(dataShardingBase.getClass());
        Connection connection = null;
        SqlSession sqlSession = null;
        try {
            connection = multiDataSourceManager.getConnection(dataShardingBase.getDbNum());
            sqlSession = sqlSessionFactory.openSession(connection);
            return sqlSession.delete(statements.getDeleteStatement(), dataShardingBase);
        } catch (Exception e) {
            LOG.error("# delete "+ dataShardingBase.getClass().getSimpleName() +" error.", e);
        } finally {
            this.close(sqlSession, connection);
        }
        return 0;
    }

    @Override
    public int delete(Short dbNum, Short tableNum, String statement, Map<String, Object> param) {
        param.put("tableFullNum", DataShardingUtils.getTableFullNum(dbNum, tableNum));
        Connection connection = null;
        SqlSession sqlSession = null;
        try {
            connection = multiDataSourceManager.getConnection(dbNum);
            sqlSession = sqlSessionFactory.openSession(connection);
            return sqlSession.delete(statement, param);
        } catch (Exception e) {
            LOG.error("# delete statement error.", e);
        } finally {
            this.close(sqlSession, connection);
        }
        return 0;
    }

    @Override
    public <T extends DataShardingBase> void batchDump(Short dbNum, List<T> dataShardingBaseList, DumpStat dumpStat) {
        Connection connection = null;
        SqlSession sqlSession = null;
        try {
            connection = multiDataSourceManager.getConnection(dbNum);
            connection.setAutoCommit(false); // 不自动提交
            sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, connection);
            for (DataShardingBase dataShardingBase : dataShardingBaseList) {
                Statements statements = statementsManager.get(dataShardingBase.getClass());
                if ((dataShardingBase.getInsertTimes() > 0L) && (dataShardingBase.getDeleteTimes() == 0L)) {
                    String insertStatement = dataShardingBase.getInsertStatement();
                    if (insertStatement == null) {
                        insertStatement = statements.getInsertStatement();
                    }
                    sqlSession.insert(insertStatement, dataShardingBase);
                    dataShardingBase.processInsertOver();
                    if (dumpStat != null ) dumpStat.insertIncr();
                } else if ((dataShardingBase.getUpdateTimes() > 0L) && (dataShardingBase.getInsertTimes() == 0L) && (dataShardingBase.getDeleteTimes() == 0L)) {
                    String updateStatement = dataShardingBase.getUpdateStatement();
                    if (updateStatement == null) {
                        updateStatement = statements.getUpdateStatement();
                    }
                    sqlSession.update(updateStatement, dataShardingBase);
                    if (dumpStat != null ) dumpStat.updateIncr();
                } else if ((dataShardingBase.getInsertTimes() == 0L) && dataShardingBase.getDeleteTimes() > 0L) {
                    String deleteStatement = dataShardingBase.getDeleteStatement();
                    if (deleteStatement == null) {
                        deleteStatement = statements.getDeleteStatement();
                    }
                    sqlSession.delete(deleteStatement, dataShardingBase);
                    if (dumpStat != null ) dumpStat.deleteIncr();
                }
            }
            sqlSession.commit(); // 提交
        } catch (Exception e) {
            LOG.error("# batchDump err dbNum=" + dbNum, e);
            if (sqlSession != null) {
                sqlSession.rollback();
            }
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true); // 设置回自动提交
                }
            } catch (SQLException e2) {
                LOG.error(e2);
            }
            this.close(sqlSession, connection);
        }
    }

    @Override
    public <T extends DataShardingBase> void batchDump(Map<Short, List<T>> dbMapList, Map<Short, DumpStat> dumpStatMap) {
        for (Map.Entry<Short, List<T>> oneEntry : dbMapList.entrySet()) {
            Short dbNum = oneEntry.getKey();

            List<T> dataShardingBaseList = oneEntry.getValue();

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
        param.put("tableNum", tableNum);
        return selectOne(dbNum, tableNum, statements.getSelectOneStatement(), param, clazz);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends DataShardingBase> T selectOne(Short dbNum, Short tableNum, String statement, Map<String, Object> param, Class<T> clazz) {
        param.put("tableNum", tableNum);
        param.put("tableFullNum", DataShardingUtils.getTableFullNum(dbNum, tableNum));
        Connection connection = null;
        SqlSession sqlSession = null;
        try {
            connection = multiDataSourceManager.getConnection(dbNum);
            sqlSession = sqlSessionFactory.openSession(connection);
            Object obj = sqlSession.selectOne(statement, param);
            if (obj == null) {
                return null;
            }
            T oneDataShardingBase = (T) obj;
            oneDataShardingBase.setDbNum(dbNum);
            oneDataShardingBase.setTableNum(tableNum);
            return oneDataShardingBase;
        } catch (Exception e) {
            LOG.error("# selectOne "+ clazz.getSimpleName() +" error.", e);
        } finally {
            this.close(sqlSession, connection);
        }
        return null;
    }

    @Override
    public <T> T selectObj(Short dbNum, Short tableNum, String statement, Map<String, Object> param, Class<T> clazz) {
        param.put("dbNum", dbNum);
        param.put("tableNum", tableNum);
        param.put("tableFullNum", DataShardingUtils.getTableFullNum(dbNum, tableNum));
        Connection connection = null;
        SqlSession sqlSession = null;
        try {
            connection = multiDataSourceManager.getConnection(dbNum);
            sqlSession = sqlSessionFactory.openSession(connection);
            return sqlSession.selectOne(statement, param);
        } catch (Exception e) {
            LOG.error("# selectOne "+ clazz.getSimpleName() +" error.", e);
        } finally {
            this.close(sqlSession, connection);
        }
        return null;
    }

    @Override
    public <E extends DataShardingBase> List<E> selectList(Short dbNum, Short tableNum, String statement, Map<String, Object> param, Class<E> clazz) {
        param.put("dbNum", dbNum);
        param.put("tableNum", tableNum);
        param.put("tableFullNum", DataShardingUtils.getTableFullNum(dbNum, tableNum));
        Connection connection = null;
        SqlSession sqlSession = null;
        try {
            connection = multiDataSourceManager.getConnection(dbNum);
            sqlSession = sqlSessionFactory.openSession(connection);
            List<E> list = sqlSession.selectList(statement, param);
            for (DataShardingBase oneDataShardingBase : list) {
                oneDataShardingBase.setDbNum(dbNum);
                oneDataShardingBase.setTableNum(tableNum);
            }
            return list;
        } catch (Exception e) {
            LOG.error("# selectList "+ clazz.getSimpleName() +" error.", e);
        } finally {
            this.close(sqlSession, connection);
        }
        return null;
    }

    @Override
    public <E> List<E> selectObjList(Short dbNum, Short tableNum, String statement, Map<String, Object> param, Class<E> clazz) {
        param.put("dbNum", dbNum);
        param.put("tableNum", tableNum);
        param.put("tableFullNum", DataShardingUtils.getTableFullNum(dbNum, tableNum));
        Connection connection = null;
        SqlSession sqlSession = null;
        try {
            connection = multiDataSourceManager.getConnection(dbNum);
            sqlSession = sqlSessionFactory.openSession(connection);
            return sqlSession.selectList(statement, param);
        } catch (Exception e) {
            LOG.error("# selectList "+ clazz.getSimpleName() +" error.", e);
        } finally {
            this.close(sqlSession, connection);
        }
        return null;
    }

    @Override
    public <E> List<E> selectObjListByCustomQueryParam(Short dbNum, String statement, Object paramObj, Class<E> paramClass) {
        Connection connection = null;
        SqlSession sqlSession = null;
        try {
            connection = multiDataSourceManager.getConnection(dbNum);
            sqlSession = sqlSessionFactory.openSession(connection);
            return sqlSession.selectList(statement, paramObj);
        } catch (Exception e) {
            LOG.error("# selectObjListByCustomQueryParam "+ paramClass.getSimpleName() +" error.", e);
        } finally {
            this.close(sqlSession, connection);
        }
        return null;
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
