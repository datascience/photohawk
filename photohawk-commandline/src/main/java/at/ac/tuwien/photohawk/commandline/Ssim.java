package at.ac.tuwien.photohawk.commandline;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import at.ac.tuwien.photohawk.evaluation.qa.SsimQa;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;
import at.ac.tuwien.photohawk.commandline.result.HRFloatResultPrinter;
import at.ac.tuwien.photohawk.commandline.result.ResultPrinter;
import at.ac.tuwien.photohawk.evaluation.colorconverter.StaticColor;
import at.ac.tuwien.photohawk.evaluation.colorconverter.hsb.HSBColorConverter;
import at.ac.tuwien.photohawk.evaluation.colorconverter.srgb.SRGBColorConverter;
import at.ac.tuwien.photohawk.evaluation.operation.TransientOperation;
import at.ac.tuwien.photohawk.evaluation.operation.metric.SimpleSSIMMetric;
import at.ac.tuwien.photohawk.evaluation.preprocessing.ScaleToNearestFactorPreprocessor;
import at.ac.tuwien.photohawk.evaluation.preprocessing.ShrinkResizePreprocessor;
import at.ac.tuwien.photohawk.evaluation.util.ConvenientBufferedImageWrapper;

class Ssim implements Command {

	private static final String LEFT = "left";
	private static final String RIGHT = "right";

	private Namespace n = null;

	private Subparsers subparsers;

	private Subparser subparser;

    private SsimQa ssimQa;

	private ResultPrinter<Float, StaticColor> resultPrinter;

	public Ssim(Subparsers subparsers) {
		this.subparsers = subparsers;
	}

	@Override
	public void init() {
		subparser = subparsers.addParser("ssim")
				.help("Structured similarity metric")
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
        ssimQa = new SsimQa();
	}

	@Override
	public void evaluate() {
		File left = (File) n.get(LEFT);
		File right = (File) n.get(RIGHT);

		try {
			BufferedImage leftImg = ImageIO.read(left);
			BufferedImage rightImg = ImageIO.read(right);

			// Evaluate
			TransientOperation<Float, StaticColor> op = ssimQa.evaluate(leftImg, rightImg);

			resultPrinter.print(op);
		} catch (IOException e) {
			subparser.printUsage();
			System.err.print("Could not read file");
		}
	}
}