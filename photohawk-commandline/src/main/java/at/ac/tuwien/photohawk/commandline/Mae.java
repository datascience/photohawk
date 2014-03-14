package at.ac.tuwien.photohawk.commandline;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;
import at.ac.tuwien.photohawk.commandline.result.HRFloatResultPrinter;
import at.ac.tuwien.photohawk.commandline.result.ResultPrinter;
import at.ac.tuwien.photohawk.evaluation.colorconverter.StaticColor;
import at.ac.tuwien.photohawk.evaluation.colorconverter.srgb.SRGBColorConverter;
import at.ac.tuwien.photohawk.evaluation.operation.TransientOperation;
import at.ac.tuwien.photohawk.evaluation.operation.metric.MAEMetric;
import at.ac.tuwien.photohawk.evaluation.preprocessing.CheckEqualSizePreprocessor;
import at.ac.tuwien.photohawk.evaluation.preprocessing.PreprocessingException;
import at.ac.tuwien.photohawk.evaluation.util.ConvenientBufferedImageWrapper;

class Mae implements Command {

	private static final String LEFT = "left";
	private static final String RIGHT = "right";

	private Namespace n = null;

	private Subparsers subparsers;

	private Subparser subparser;

	private ResultPrinter<Float, StaticColor> resultPrinter;

	public Mae(Subparsers subparsers) {
		this.subparsers = subparsers;
	}

	@Override
	public void init() {
		subparser = subparsers.addParser("mae").help("Mean absolute error metric")
				.setDefault("command", this);
		
		subparser.addArgument(LEFT).type(Arguments.fileType().verifyCanRead())
				.help("Left file for comparison");
		subparser.addArgument(RIGHT).type(Arguments.fileType().verifyCanRead())
				.help("Right file for comparison");
	}

	@Override
	public void configure(Namespace n) {
		this.n = n;
		resultPrinter = new HRFloatResultPrinter(System.out);
	}

	@Override
	public void evaluate() {
		File left = (File) n.get(LEFT);
		File right = (File) n.get(RIGHT);

		try {
			BufferedImage leftImg = ImageIO.read(left);
			BufferedImage rightImg = ImageIO.read(right);

			// Check size
			CheckEqualSizePreprocessor equalSize = new CheckEqualSizePreprocessor(
					leftImg, rightImg);
			equalSize.process();
			leftImg = equalSize.getResult1();
			rightImg = equalSize.getResult2();
			equalSize = null;
			
			MAEMetric metric = new MAEMetric(new SRGBColorConverter(
					new ConvenientBufferedImageWrapper(leftImg)),
					new SRGBColorConverter(new ConvenientBufferedImageWrapper(
							rightImg)), new Point(0, 0), new Point(
							leftImg.getWidth(), rightImg.getHeight()));

			// Evaluate
			TransientOperation<Float, StaticColor> op = metric.execute();

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