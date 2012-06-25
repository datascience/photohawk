package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.util.Arrays;

import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.util.ConvenientBufferedImageWrapper;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.util.ImageException;

/**
 * This class provides a ColorConverter that uses a ICC_Profile for conversion.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 * @version 1.0
 */
public abstract class ICCProfileColorConverter implements FullColorConverter {

	protected final ConvenientBufferedImageWrapper img;

	public ICCProfileColorConverter(ConvenientBufferedImageWrapper img, ColorSpace cs) throws ImageException {
		ColorConvertOp transform = new ColorConvertOp(null);

		// int id = App.startMeasureTime("Start image colorspace");
		int[] bits = new int[img.getColorModel().getNumComponents()];
		Arrays.fill(bits, 16);
		BufferedImage temp = transform.createCompatibleDestImage(img.getBufferedImage(), new ComponentColorModel(cs, bits, img.getColorModel().hasAlpha(), img.getColorModel().isAlphaPremultiplied(),
				img.getColorModel().getTransparency(), DataBuffer.TYPE_INT));

		temp = transform.filter(img.getBufferedImage(), temp);

		// App.endMeasureTime(id, "Convert image colorspace");
		this.img = new ConvenientBufferedImageWrapper(temp);

	}

	public ConvenientBufferedImageWrapper getImage() {
		return img;
	}

}
