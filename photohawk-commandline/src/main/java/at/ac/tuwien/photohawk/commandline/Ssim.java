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
	}

	@Override
	public void evaluate() {
		File left = (File) n.get(LEFT);
		File right = (File) n.get(RIGHT);

		try {
			BufferedImage leftImg = ImageIO.read(left);
			BufferedImage rightImg = ImageIO.read(right);

			// Convert to SRGB
			leftImg = new SRGBColorConverter(
					new ConvenientBufferedImageWrapper(leftImg)).getImage()
					.getBufferedImage();
			rightImg = new SRGBColorConverter(
					new ConvenientBufferedImageWrapper(rightImg)).getImage()
					.getBufferedImage();

			// Resize
			ShrinkResizePreprocessor shrink = new ShrinkResizePreprocessor(
					leftImg, rightImg);
			shrink.process();
			leftImg = shrink.getResult1();
			rightImg = shrink.getResult2();
			shrink = null;

			// Scale
			ScaleToNearestFactorPreprocessor scale = new ScaleToNearestFactorPreprocessor(
					leftImg, rightImg, SimpleSSIMMetric.DEFAULT_TARGET_SIZE);
			scale.process();
			leftImg = scale.getResult1();
			rightImg = scale.getResult2();
			scale = null;

			// Evaluate
			ConvenientBufferedImageWrapper wrapped1 = new ConvenientBufferedImageWrapper(
					leftImg);
			HSBColorConverter c1 = new HSBColorConverter(
					new SRGBColorConverter(wrapped1));
			ConvenientBufferedImageWrapper wrapped2 = new ConvenientBufferedImageWrapper(
					rightImg);
			HSBColorConverter c2 = new HSBColorConverter(
					new SRGBColorConverter(wrapped2));

			// TODO: What happens if one image is smaller than the
			// SCALE_TARGET_SIZE?
			 SimpleSSIMMetric metric = new SimpleSSIMMetric(c1, c2, new Point(0, 0), new Point(
					leftImg.getWidth(), rightImg.getHeight()), false);

			// Evaluate
			TransientOperation<Float, StaticColor> op = metric.execute();

			resultPrinter.print(op);
		} catch (IOException e) {
			subparser.printUsage();
			System.err.print("Could not read file");
		}
	}
}