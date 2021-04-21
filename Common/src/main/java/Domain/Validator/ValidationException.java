package Domain.Validator;

public class ValidationException extends RuntimeException {

    /**
     * Default constructor
     */
    public ValidationException() {
    }

    /**
     *
     * @param message
     *          string message
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     *
     * @param message
     *         string message
     * @param cause
     *          Throwable cause
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param cause
     *          Throwable cause
     */
    public ValidationException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     *          String message
     * @param cause
     *          Throwable cause
     * @param enableSuppression
     *          Boolean
     * @param writableStackTrace
     *          Boolean
     */
    public ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

