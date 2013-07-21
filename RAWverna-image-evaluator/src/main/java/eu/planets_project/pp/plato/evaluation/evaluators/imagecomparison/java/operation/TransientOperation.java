package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.operation;

/**
 * This class represents an OperationExecution. It can be seen as state machine, performing,
 * calculating and storing result.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 * @version 1.0
 */
public abstract class TransientOperation<AggregatedResult, Result> {

	public TransientOperation() {
	}

	public abstract void init();

	public void execute(int[] x, int[] y) {
		for (int i = 0; i < x.length; i++) {
			execute(x[i], y[i]);
		}
	}

	public void execute(int x, int y) {
		throw new RuntimeException();
	}

	public abstract void complete();

	public abstract AggregatedResult getAggregatedResult();

	public abstract Result getResult();

	public abstract int getGranularityX();

	public abstract int getGranularityY();
}
