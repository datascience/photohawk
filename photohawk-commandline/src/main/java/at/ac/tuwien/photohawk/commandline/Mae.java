/*******************************************************************************
 * Copyright 2010-2014 Vienna University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package at.ac.tuwien.photohawk.commandline;

import at.ac.tuwien.photohawk.commandline.result.HRFloatResultPrinter;
import at.ac.tuwien.photohawk.commandline.result.ResultPrinter;
import at.ac.tuwien.photohawk.evaluation.colorconverter.StaticColor;
import at.ac.tuwien.photohawk.evaluation.operation.TransientOperation;
import at.ac.tuwien.photohawk.evaluation.preprocessing.PreprocessingException;
import at.ac.tuwien.photohawk.evaluation.qa.MaeQa;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Mean absolute error command.
 */
class Mae implements Command {

    private static final String LEFT = "left";
    private static final String RIGHT = "right";

    private Namespace n = null;

    private Subparsers subparsers;

    private Subparser subparser;

    private MaeQa maeQa;

    private ResultPrinter<Float, StaticColor> resultPrinter;

    /**
     * Creates a new MAE command.
     *
     * @param subparsers the subparser to use
     */
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
        maeQa = new MaeQa();
    }

    @Override
    public void evaluate() {
        File left = (File) n.get(LEFT);
        File right = (File) n.get(RIGHT);

        try {
            BufferedImage leftImg = ImageIO.read(left);
            BufferedImage rightImg = ImageIO.read(right);

            // Evaluate
            TransientOperation<Float, StaticColor> op = maeQa.evaluate(leftImg, rightImg);

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