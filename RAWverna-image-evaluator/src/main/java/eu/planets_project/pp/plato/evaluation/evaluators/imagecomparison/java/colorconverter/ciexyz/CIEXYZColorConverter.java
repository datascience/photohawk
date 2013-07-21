package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.ciexyz;

import java.awt.color.ICC_ColorSpace;

import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.ColorConverter;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.ICCProfileColorConverter;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.StaticColor;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.util.ConvenientBufferedImageWrapper;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.util.ImageException;

/**
 * This ColorConverter can convert colors to CIE-XYZ.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 * @version 1.0
 */
public class CIEXYZColorConverter extends ICCProfileColorConverter implements ColorConverter<CIEXYZStaticColor> {

	public CIEXYZColorConverter(ConvenientBufferedImageWrapper img) throws ImageException {
		super(img, ICC_ColorSpace.getInstance(ICC_ColorSpace.CS_CIEXYZ));
	}

	public CIEXYZStaticColor getColorChannels(int x, int y) {
		return new CIEXYZStaticColor(img.getSample(x, y));
	}

	public int getNumberOfChannels() {
		return 3;
	}

	public StaticColor getNullColor() {
		return new CIEXYZStaticColor(0, 0, 0);
	}

	public String[] getChannelDescription() {
		return CIEXYZStaticColor.channelNames;
	}

	public String getChannelDescription(int idx) {
		return getChannelDescription()[idx];
	}
}
