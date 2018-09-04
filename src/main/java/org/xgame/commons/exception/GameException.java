package org.xgame.commons.exception;

/**
 * @Name: GameException.class
 * @Description: //
 * @Create: DerekWu on 2018/9/2 16:56
 * @Version: V1.0
 */
public class GameException extends RuntimeException {

    private static final long serialVersionUID = 7895811740186691192L;

    public GameException() {
        super();
    }

    public GameException(String message) {
        super(message);
    }

    public GameException(Throwable cause) {
        super(cause);
    }

    public GameException(String message, Throwable cause) {
        super(message, cause);
    }

}
