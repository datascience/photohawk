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

import at.ac.tuwien.photohawk.commandline.result.HRBooleanResultPrinter;
import at.ac.tuwien.photohawk.commandline.result.ResultPrinter;
import at.ac.tuwien.photohawk.commandline.util.ImageReader;
import at.ac.tuwien.photohawk.evaluation.operation.TransientOperation;
import at.ac.tuwien.photohawk.evaluation.preprocessing.PreprocessingException;
import at.ac.tuwien.photohawk.evaluation.qa.EqualQa;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Equal command.
 */
public class Equal implements Command {

    private static final String LEFT = "left";
    private static final String RIGHT = "right";
    private static final String BASE_KEY = "dcraw.equal";

    private Namespace n = null;

    private Subparsers subparsers;

    private Subparser subparser;

    private EqualQa equalQa;

    private ResultPrinter<Boolean, Boolean> resultPrinter;

    private ImageReader ir;

    /**
     * Creates a new equal command.
     *
     * @param subparsers the subparser to use
     */
    public Equal(Subparsers subparsers) {
        this.subparsers = subparsers;
    }

    @Override
    public void init() {
        subparser = subparsers.addParser("equal").help("Equal").setDefault("command", this);

        subparser.addArgument(LEFT).type(Arguments.fileType().verifyCanRead()).help("Left file for comparison");
        subparser.addArgument(RIGHT).type(Arguments.fileType().verifyCanRead()).help("Right file for comparison");
    }

    @Override
    public void configure(Namespace n) {
        this.n = n;
        resultPrinter = new HRBooleanResultPrinter(System.out);
        ir = new ImageReader(BASE_KEY);
        equalQa = new EqualQa();
    }

    @Override
    public void evaluate() {
        File left = n.get(LEFT);
        File right = n.get(RIGHT);

        try {
            String leftMode = ir.determineReadMode(left, n.getString(Photohawk.READ_LEFT_KEY));
            String rightMode = ir.determineReadMode(right, n.getString(Photohawk.READ_RIGHT_KEY));
            BufferedImage leftImg = ir.readImage(left, leftMode, rightMode);
            BufferedImage rightImg = ir.readImage(right, rightMode, leftMode);

            // Evaluate
            TransientOperation<Boolean, Boolean> op = equalQa.evaluate(leftImg, rightImg);

            resultPrinter.print(op);
        } catch (PreprocessingException e) {
            subparser.printUsage();
            System.err.println("Cannot process files");
            System.err.println(e.getMessage());
        } catch (IOException e) {
            subparser.printUsage();
            System.err.println("Cannot read file");
            System.err.println(e.getMessage());
        }
    }

}
