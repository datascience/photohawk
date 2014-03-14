package at.ac.tuwien.photohawk.commandline.result;

import at.ac.tuwien.photohawk.evaluation.operation.TransientOperation;

public interface ResultPrinter<AggregatedResult, Result> {
	void print(TransientOperation<AggregatedResult, Result> op);
}
