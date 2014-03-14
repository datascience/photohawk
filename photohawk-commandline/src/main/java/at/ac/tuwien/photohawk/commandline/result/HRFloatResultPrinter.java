package at.ac.tuwien.photohawk.commandline.result;

import java.io.OutputStream;
import java.io.PrintWriter;

import at.ac.tuwien.photohawk.evaluation.colorconverter.StaticColor;
import at.ac.tuwien.photohawk.evaluation.operation.TransientOperation;

public class HRFloatResultPrinter implements ResultPrinter<Float, StaticColor> {

	private PrintWriter w;

	public HRFloatResultPrinter(OutputStream out) {
		w = new PrintWriter(out);
	}

	@Override
	public void print(TransientOperation<Float, StaticColor> op) {
		w.format("%f%n", op.getAggregatedResult());

		for (int i = 0; i < op.getResult().getNumberOfChannels(); i++) {
			w.print(op.getResult().getChannelDescription(i));
			w.format(": %f%n", op.getResult().getChannelValue(i));
		}

		w.flush();
	}
}
