package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.srgb;

import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.StaticColor;

/**
 * A simple RGB Color.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 * @version 1.0
 */
public class SRGBStaticColor implements StaticColor {

	public static final String[] channelNames = new String[] { "R", "G", "B" };

	private float[] values;

	public SRGBStaticColor(float[] values) {
		setChannelValues(values);
	}

	public SRGBStaticColor(float r, float g, float b) {
		this(new float[] { r, g, b });
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
		return SRGBStaticColor.channelNames;
	}

	public String getChannelDescription(int idx) {
		return getChannelDescription()[idx];
	}

	public int getNumberOfChannels() {
		return 3;
	}

}
