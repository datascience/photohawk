package at.ac.tuwien.photohawk.commandline;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import at.ac.tuwien.photohawk.evaluation.qa.EqualQa;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;
import at.ac.tuwien.photohawk.commandline.result.HRBooleanResultPrinter;
import at.ac.tuwien.photohawk.commandline.result.ResultPrinter;
import at.ac.tuwien.photohawk.evaluation.colorconverter.srgb.SRGBColorConverter;
import at.ac.tuwien.photohawk.evaluation.operation.TransientOperation;
import at.ac.tuwien.photohawk.evaluation.operation.metric.EqualMetric;
import at.ac.tuwien.photohawk.evaluation.preprocessing.CheckEqualSizePreprocessor;
import at.ac.tuwien.photohawk.evaluation.preprocessing.PreprocessingException;
import at.ac.tuwien.photohawk.evaluation.util.ConvenientBufferedImageWrapper;

public class Equal implements Command {

	private static final String LEFT = "left";
	private static final String RIGHT = "right";

	private Namespace n = null;

	private Subparsers subparsers;

	private Subparser subparser;

    private EqualQa equalQa;

	private ResultPrinter<Boolean, Boolean> resultPrinter;

	public Equal(Subparsers subparsers) {
		this.subparsers = subparsers;
	}

	@Override
	public void init() {
		subparser = subparsers.addParser("equal").help("Equal")
				.setDefault("command", this);

		subparser.addArgument(LEFT).type(Arguments.fileType().verifyCanRead())
				.help("Left file for comparison");
		subparser.addArgument(RIGHT).type(Arguments.fileType().verifyCanRead())
				.help("Right file for comparison");
	}

	@Override
	public void configure(Namespace n) {
		this.n = n;
		resultPrinter = new HRBooleanResultPrinter(System.out);
        equalQa = new EqualQa();
	}

	@Override
	public void evaluate() {
		File left = (File) n.get(LEFT);
		File right = (File) n.get(RIGHT);

		try {
			BufferedImage leftImg = ImageIO.read(left);
			BufferedImage rightImg = ImageIO.read(right);

			// Evaluate
			TransientOperation<Boolean, Boolean> op = equalQa.evaluate(leftImg, rightImg);

			resultPrinter.print(op);
		} catch (PreprocessingException e) {
			subparser.printUsage();
			System.err.print("Image size does not match");
		} catch (IOException e) {
			subparser.printUsage();
			System.err.print("Could not read file");
		}
	}

}