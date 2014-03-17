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

package at.ac.tuwien.photohawk.commandline.result;

import at.ac.tuwien.photohawk.evaluation.colorconverter.StaticColor;
import at.ac.tuwien.photohawk.evaluation.operation.TransientOperation;

import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Human readable float result printer.
 */
public class HRFloatResultPrinter implements ResultPrinter<Float, StaticColor> {

    private PrintWriter w;

    /**
     * Creates a new result printer for the provided output stream.
     *
     * @param out the output stream to print to
     */
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
