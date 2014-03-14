package at.ac.tuwien.photohawk.commandline;

import at.ac.tuwien.photohawk.commandline.result.HRFloatResultPrinter;
import at.ac.tuwien.photohawk.commandline.result.ResultPrinter;
import at.ac.tuwien.photohawk.evaluation.colorconverter.StaticColor;
import at.ac.tuwien.photohawk.evaluation.operation.TransientOperation;
import at.ac.tuwien.photohawk.evaluation.preprocessing.PreprocessingException;
import at.ac.tuwien.photohawk.evaluation.qa.AeQa;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class Ae implements Command {

    private static final String LEFT = "left";
    private static final String RIGHT = "right";

    private Namespace n = null;

    private Subparsers subparsers;

    private Subparser subparser;

    private AeQa aeQa;

    private ResultPrinter<Float, StaticColor> resultPrinter;

    public Ae(Subparsers subparsers) {
        this.subparsers = subparsers;
    }

    @Override
    public void init() {
        subparser = subparsers.addParser("ae").help("Absolute error metric")
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
        aeQa = new AeQa();
    }

    @Override
    public void evaluate() {
        File left = (File) n.get(LEFT);
        File right = (File) n.get(RIGHT);

        try {
            BufferedImage leftImg = ImageIO.read(left);
            BufferedImage rightImg = ImageIO.read(right);

            // Evaluate
            TransientOperation<Float, StaticColor> op = aeQa.evaluate(leftImg, rightImg);

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