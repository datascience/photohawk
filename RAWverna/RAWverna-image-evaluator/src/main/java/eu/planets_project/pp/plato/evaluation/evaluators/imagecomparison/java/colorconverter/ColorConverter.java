package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter;

/**
 * This interface describes a ColorConverter that can transform colro values from one into another
 * color system.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 * @version 1.0
 */
public interface ColorConverter<T extends StaticColor> extends ColorSystem {

	T getColorChannels(int x, int y);

	StaticColor getNullColor();

}
