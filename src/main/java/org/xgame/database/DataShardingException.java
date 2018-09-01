package org.xgame.database;

/**
 * @Name: DataShardingException.class
 * @Description: //
 * @Create: DerekWu on 2018/9/1 20:52
 * @Version: V1.0
 */
public class DataShardingException extends RuntimeException {

    public DataShardingException() {
        super();
    }

    public DataShardingException(String message) {
        super(message);
    }

    public DataShardingException(Throwable cause) {
        super(cause);
    }

    public DataShardingException(String message, Throwable cause) {
        super(message, cause);
    }

}
