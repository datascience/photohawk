package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.noconversion;

import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.ColorConverter;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.StaticColor;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.util.ConvenientBufferedImageWrapper;

/**
 * This ColorConverter performs no color conversion.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 * @version 1.0
 */
public class NoConversionColorConverter implements ColorConverter<NoConversionStaticColor> {

	private final String[] channelNames;

	private ConvenientBufferedImageWrapper img;

	public NoConversionColorConverter(ConvenientBufferedImageWrapper img, String[] channelNames) {
		this.img = img;

		if (null == channelNames) {
			channelNames = new String[getNumberOfChannels()];
			for (int i = 0; i < getNumberOfChannels(); i++) {
				channelNames[i] = String.valueOf(i + 1);
			}
		}
		this.channelNames = channelNames;

	}

	@Deprecated
	public NoConversionColorConverter(ConvenientBufferedImageWrapper img) {
		this(img, null);
	}

	public NoConversionStaticColor getColorChannels(int x, int y) {
		return new NoConversionStaticColor(channelNames, img.getSample(x, y));
	}

	public String[] getChannelDescription() {
		return channelNames;
	}

	public String getChannelDescription(int idx) {
		return getChannelDescription()[idx];
	}

	public StaticColor getNullColor() {
		float[] val = new float[this.getNumberOfChannels()];
		for (int i = 0; i < val.length; i++) {
			val[i] = 0;
		}
		return new NoConversionStaticColor(channelNames, val);
	}

	public int getNumberOfChannels() {
		return img.getSampleModel().getNumBands();
	}

}
