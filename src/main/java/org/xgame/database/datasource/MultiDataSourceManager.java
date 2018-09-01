package org.xgame.database.datasource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Name: MultiDataSourceManager.class
 * @Description: // 多数据源管理
 * @Create: DerekWu on 2018/9/1 9:39
 * @Version: V1.0
 */
public class MultiDataSourceManager {

    private final Logger LOG = LogManager.getLogger(getClass());

    /** 数据源Map （数据源编号） */
    private Map<Short, DataSource> dataSourceMap = new HashMap<>();

    /**
     * 放入一个数据源
     * @param dbNum
     * @param dataSource
     */
    public void putDataSource(Short dbNum, DataSource dataSource) {
        dataSourceMap.put(dbNum, dataSource);
    }

    public DataSource removeDataSource(Short dbNum) {
        return dataSourceMap.remove(dbNum);
    }

    public Map<Short, DataSource> getDataSourceMap() {
        return dataSourceMap;
    }

    /**
     * 获取一个连接
     * @param dbNum
     * @return
     */
    public Connection getConnection(Short dbNum) {
        try {
            DataSource dataSource = dataSourceMap.get(dbNum);
            if (dataSource != null) {
                return dataSource.getConnection();
            }
        } catch (SQLException e) {
            LOG.error("# MultiDataSourceManager getConnection error ", e);
        }
        return null;
    }

}
