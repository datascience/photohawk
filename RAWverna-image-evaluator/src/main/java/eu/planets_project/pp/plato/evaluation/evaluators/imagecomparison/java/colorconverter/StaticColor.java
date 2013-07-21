package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter;

/**
 * This interface describes a simple Color object.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 * @version 1.0
 */
public interface StaticColor extends ColorSystem {

	float[] getChannelValues();

	float getChannelValue(int idx);
	
	void setChannelValues(float[] values);
	
	void setChannelValue(int idx, float value);
	
}
