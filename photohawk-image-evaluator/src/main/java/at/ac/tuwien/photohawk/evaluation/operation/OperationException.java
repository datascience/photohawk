package at.ac.tuwien.photohawk.evaluation.operation;

/**
 * Exception for preprocessors.
 */
public class OperationException extends RuntimeException {
    private static final long serialVersionUID = 8036607858919524993L;

    /**
     * Creates a new PreprocessingException.
     */
    public OperationException() {
        super();
    }

    /**
     * Creates a new PreprocessingException.
     * 
     * @param message
     *            exception message
     */
    public OperationException(String message) {
        super(message);
    }

    /**
     * Creates a new PreprocessingException.
     * 
     * @param message
     *            exception message
     * @param cause
     *            cause
     */
    public OperationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new PreprocessingException.
     * 
     * @param cause
     *            cause
     */
    public OperationException(Throwable cause) {
        super(cause);
    }
}
