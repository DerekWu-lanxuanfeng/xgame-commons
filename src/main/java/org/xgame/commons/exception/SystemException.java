package org.xgame.commons.exception;

/**
 * @Name: SystemException.class
 * @Description: //
 * @Create: DerekWu on 2018/9/2 18:13
 * @Version: V1.0
 */
public class SystemException extends RuntimeException {

    private static final long serialVersionUID = 7895811740186691192L;

    public SystemException() {
        super();
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(Throwable cause) {
        super(cause);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
