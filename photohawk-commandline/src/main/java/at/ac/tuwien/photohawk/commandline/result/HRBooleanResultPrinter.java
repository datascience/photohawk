package at.ac.tuwien.photohawk.commandline.result;

import java.io.OutputStream;
import java.io.PrintWriter;

import at.ac.tuwien.photohawk.evaluation.operation.TransientOperation;

public class HRBooleanResultPrinter implements ResultPrinter<Boolean, Boolean> {

	private PrintWriter w;

	public HRBooleanResultPrinter(OutputStream out) {
		w = new PrintWriter(out);
	}

	@Override
	public void print(TransientOperation<Boolean, Boolean> op) {
		w.print(op.getAggregatedResult());
		w.flush();
	}
}
