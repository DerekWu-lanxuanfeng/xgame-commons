package org.xgame.commons.exception;

/**
 * @Name: GameError.class
 * @Description: //
 * @Create: DerekWu on 2018/9/2 16:56
 * @Version: V1.0
 */
public class GameError extends Error {

    private static final long serialVersionUID = 6958566597733761399L;

    public GameError(String message) {
        super(message);
    }

    public GameError(String message, Throwable cause) {
        super(message, cause);
    }

    public GameError(Throwable cause) {
        super(cause);
    }

}
