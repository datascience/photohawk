package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter;

import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.util.ConvenientBufferedImageWrapper;

/**
 * This interface describes ColorConverters that can transform an image at once.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 * @version 1.0
 */
public interface FullColorConverter {

	ConvenientBufferedImageWrapper getImage();

}
