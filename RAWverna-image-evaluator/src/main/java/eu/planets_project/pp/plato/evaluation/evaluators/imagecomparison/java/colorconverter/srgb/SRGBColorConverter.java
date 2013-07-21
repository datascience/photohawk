package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.srgb;

import java.awt.color.ICC_ColorSpace;

import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.ColorConverter;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.ICCProfileColorConverter;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.StaticColor;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.util.ConvenientBufferedImageWrapper;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.util.ImageException;

/**
 * This class converts an Image into sRGB.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 * @version 1.0
 */
public class SRGBColorConverter extends ICCProfileColorConverter implements ColorConverter<SRGBStaticColor> {

	public SRGBColorConverter(ConvenientBufferedImageWrapper img) throws ImageException {
		super(img, ICC_ColorSpace.getInstance(ICC_ColorSpace.CS_sRGB));
	}

	public SRGBStaticColor getColorChannels(int x, int y) {
		return new SRGBStaticColor(img.getSample(x, y));
	}

	public String[] getChannelDescription() {
		return SRGBStaticColor.channelNames;
	}

	public String getChannelDescription(int idx) {
		return getChannelDescription()[idx];
	}

	public StaticColor getNullColor() {
		return new SRGBStaticColor(0, 0, 0);
	}

	public int getNumberOfChannels() {
		return 3;
	}

}
