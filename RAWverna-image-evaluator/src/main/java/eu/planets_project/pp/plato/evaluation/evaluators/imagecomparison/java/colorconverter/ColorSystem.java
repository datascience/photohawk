package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter;

/**
 * This interface describes some generic ColorSystem.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 * @version 1.0
 */
public interface ColorSystem {

	int getNumberOfChannels();

	String[] getChannelDescription();

	String getChannelDescription(int idx);

}
