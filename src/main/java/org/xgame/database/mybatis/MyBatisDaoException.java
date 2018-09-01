package org.xgame.database.mybatis;

/**
 * @Name: MyBatisDaoException.class
 * @Description: //
 * @Create: DerekWu on 2018/9/1 18:53
 * @Version: V1.0
 */
public class MyBatisDaoException extends RuntimeException {

    private static final long serialVersionUID = -135318601306812022L;

    public MyBatisDaoException() {
        super();
    }

    public MyBatisDaoException(String message) {
        super(message);
    }

    public MyBatisDaoException(Throwable cause) {
        super(cause);
    }

    public MyBatisDaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
