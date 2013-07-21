package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.operation;

/**
 * This class represents a generic Operation.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 * @version 1.0
 */
public abstract class Operation<AggregatedResult, Result> {

	public abstract TransientOperation<AggregatedResult, Result> prepare();

}
