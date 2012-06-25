package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.hsb;

import java.awt.Color;

import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.ColorConverter;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.StaticColor;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.srgb.SRGBColorConverter;

/**
 * This ColorConverter can convert from sRGB to HSB.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 * @version 1.0
 */
public class HSBColorConverter implements ColorConverter<HSBStaticColor> {

	private SRGBColorConverter img;

	public HSBColorConverter(SRGBColorConverter img) {
		this.img = img;
	}

	public HSBStaticColor getColorChannels(int x, int y) {
		float[] data = img.getColorChannels(x, y).getChannelValues();
		float[] converted = Color.RGBtoHSB((int) (data[0] * 255), (int) (data[1] * 255), (int) (data[2] * 255), null);
		return new HSBStaticColor(converted[2], converted[1], converted[0]);
	}

	public String[] getChannelDescription() {
		return HSBStaticColor.channelNames;
	}

	public String getChannelDescription(int idx) {
		return getChannelDescription()[idx];
	}

	public StaticColor getNullColor() {
		return new HSBStaticColor(0, 0, 0);
	}

	public int getNumberOfChannels() {
		return 3;
	}

	public static double normalizeHueDifference(double value) {
		if (value > 0.5) {
			return (1 - value);
		} else {
			return value;
		}
	}

}
