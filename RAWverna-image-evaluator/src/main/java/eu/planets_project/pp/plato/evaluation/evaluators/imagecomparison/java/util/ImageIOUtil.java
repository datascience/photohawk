package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.util;

import javax.imageio.ImageIO;

/**
 * This class provides convenience methods for interaction with image IO.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 * @version 1.0
 */
public class ImageIOUtil {

	/**
	 * Return true if it is most likely that the normal Java Image API can read the ImageAPI. If
	 * there is a more preferable reader than the standard one, this method will return false.
	 * 
	 * @param suffix
	 *            The suffix specifying the file type (may be null)
	 * @param mime
	 *            The mime type of the file (may be null)
	 * @return Whether or not the image is readable by the Java Image API
	 */
	public static boolean isReadable(String suffix, String mime) {
		if ("DNG".equalsIgnoreCase(suffix)) {
			return false;
		}
		if ("image/x-raw".equalsIgnoreCase(mime)) {
			return false;
		}
		if (null == mime || ImageIO.getImageReadersByMIMEType(mime).hasNext() == false) {
			if (null == suffix || ImageIO.getImageReadersBySuffix(suffix).hasNext() == false) {
				return false;
			}
		}
		return true;
	}

}
