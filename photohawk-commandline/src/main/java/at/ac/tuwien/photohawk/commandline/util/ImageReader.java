package at.ac.tuwien.photohawk.commandline.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ImageReader {
	public BufferedImage readImage(InputStream in) throws IOException {
		BufferedImage image = ImageIO.read(in);

		return image;
	}
}
