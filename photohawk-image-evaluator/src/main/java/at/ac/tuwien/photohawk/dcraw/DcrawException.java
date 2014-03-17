package at.ac.tuwien.photohawk.dcraw;

/**
 * Created by markus on 22.03.14.
 */
public class DcrawException extends RuntimeException {
    public DcrawException(String message) {
        super(message);
    }

    public DcrawException(String message, Throwable cause) {
        super(message, cause);
    }
}
