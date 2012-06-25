package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.util;

/**
 * This class indicates a problem related with an image.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 * @version 1.0
 */
public class ImageException extends Exception {

	private static final long serialVersionUID = 4016147838698182331L;

	public ImageException() {
	}

	public ImageException(String message) {
		super(message);
	}

	public ImageException(Throwable cause) {
		super(cause);
	}

	public ImageException(String message, Throwable cause) {
		super(message, cause);
	}

}
