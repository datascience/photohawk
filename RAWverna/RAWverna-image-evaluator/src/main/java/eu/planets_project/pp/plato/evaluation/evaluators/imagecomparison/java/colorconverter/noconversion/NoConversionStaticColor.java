package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.noconversion;

import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.StaticColor;

/**
 * A simple generic Color.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 * @version 1.0
 */
public class NoConversionStaticColor implements StaticColor {

	private final String[] channelNames;

	private float[] values;

	public NoConversionStaticColor(String[] channelNames, float[] values) {
		setChannelValues(values);

		if (null == channelNames) {
			channelNames = new String[getNumberOfChannels()];
			for (int i = 0; i < getNumberOfChannels(); i++) {
				channelNames[i] = String.valueOf(i + 1);
			}
		}
		this.channelNames = channelNames;

	}

	@Deprecated
	public NoConversionStaticColor(float[] values) {
		this(null, values);
	}

	public float getChannelValue(int idx) {
		return values[idx];
	}

	public float[] getChannelValues() {
		return values;
	}

	public void setChannelValues(float[] values) {
		this.values = values;
	}

	public void setChannelValue(int idx, float value) {
		values[idx] = value;
	}

	public String toString() {
		String result = "";
		for (int i = 0; i < values.length; i++) {
			result += getChannelDescription(i) + ": " + values[i] + "\n";
		}
		return result;
	}

	public String[] getChannelDescription() {
		return channelNames;
	}

	public String getChannelDescription(int idx) {
		return getChannelDescription()[idx];
	}

	public int getNumberOfChannels() {
		return values.length;
	}

}
