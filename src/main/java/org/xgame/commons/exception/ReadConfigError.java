package org.xgame.commons.exception;

/**
 * @Name: ReadConfigError.class
 * @Description: //
 * @Create: DerekWu on 2018/9/2 16:57
 * @Version: V1.0
 */
public class ReadConfigError extends GameError {

    private static final String ERROR_FORMAT = "Read config file[%1$s] case exception[%2$s]";
    private static final long serialVersionUID = 6675712704613200243L;

    private String fileName;

    private String readErrorMessage;

    public ReadConfigError(String fileName, Throwable cause) {
        super(cause);
        this.fileName = fileName;
        this.readErrorMessage = String.format(ERROR_FORMAT, fileName,
                getMessage());
    }

    public ReadConfigError(String fileName, String message) {
        super(message);
        this.readErrorMessage = String.format(ERROR_FORMAT, fileName, message);
    }

    public String getFileName() {
        return fileName;
    }

    public String getReadMessage() {
        return readErrorMessage;
    }

}
