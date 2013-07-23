package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.preprocessing;

/**
 * Exception for preprocessors.
 */
public class PreprocessingException extends RuntimeException {
    private static final long serialVersionUID = 8036607858919524993L;

    /**
     * Creates a new PreprocessingException.
     */
    public PreprocessingException() {
        super();
    }

    /**
     * Creates a new PreprocessingException.
     * 
     * @param message
     *            exception message
     */
    public PreprocessingException(String message) {
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
    public PreprocessingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new PreprocessingException.
     * 
     * @param cause
     *            cause
     */
    public PreprocessingException(Throwable cause) {
        super(cause);
    }
}
