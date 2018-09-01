package org.xgame.database.mybatis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xgame.database.DataShardingBase;

import java.util.HashMap;
import java.util.Map;

/**
 * @Name: StatementsManager.class
 * @Description: //
 * @Create: DerekWu on 2018/9/1 18:02
 * @Version: V1.0
 */
public class StatementsManager {

    private final Logger LOG = LogManager.getLogger(getClass());
    private Map<String, Statements> statementsMap = new HashMap<>();

    protected <T extends DataShardingBase> void register(Class<T> t, Statements statements) {
        if (statementsMap.containsKey(t.getSimpleName())) {
            LOG.error("========Statements:" + t.getSimpleName() + " repeated========System.exit(1)");
            System.exit(1);
        } else {
            statementsMap.put(t.getSimpleName(), statements);
        }
    }

    public <T extends DataShardingBase> Statements get(Class<T> t) {
        return statementsMap.get(t.getSimpleName());
    }

}
