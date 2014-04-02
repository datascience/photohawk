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

package at.ac.tuwien.photohawk.evaluation.qa;

import at.ac.tuwien.photohawk.evaluation.colorconverter.StaticColor;
import at.ac.tuwien.photohawk.evaluation.colorconverter.srgb.SRGBColorConverter;
import at.ac.tuwien.photohawk.evaluation.operation.TransientOperation;
import at.ac.tuwien.photohawk.evaluation.operation.metric.AEMetric;
import at.ac.tuwien.photohawk.evaluation.preprocessing.CheckEqualSizePreprocessor;
import at.ac.tuwien.photohawk.evaluation.util.ConvenientBufferedImageWrapper;

import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * Quality assurance for absolute error.
 */
public class AeQa implements Qa<Float, StaticColor> {

    private StaticColor threshold;

    /**
     * Creates a new QA object with default parameters.
     */
    public AeQa() {
        this.threshold = null;
    }

    /**
     * Sets the threshold.
     *
     * @param threshold the threshold to use
     * @return this Qa object
     */
    public AeQa threshold(final StaticColor threshold) {
        this.threshold = threshold;
        return this;
    }

    @Override
    public TransientOperation<Float, StaticColor> evaluate(final BufferedImage left, final BufferedImage right) {
        // Check size
        CheckEqualSizePreprocessor equalSize = new CheckEqualSizePreprocessor(left, right);
        equalSize.process();
        equalSize = null;

        // Run metric
        AEMetric metric;
        if (threshold == null) {
            metric = new AEMetric(new SRGBColorConverter(new ConvenientBufferedImageWrapper(left)),
                                  new SRGBColorConverter(new ConvenientBufferedImageWrapper(right)), new Point(0, 0),
                                  new Point(left.getWidth(), left.getHeight()));
        } else {
            metric = new AEMetric(new SRGBColorConverter(new ConvenientBufferedImageWrapper(left)),
                                  new SRGBColorConverter(new ConvenientBufferedImageWrapper(right)), threshold,
                                  new Point(0, 0), new Point(left.getWidth(), left.getHeight()));
        }

        // Evaluate
        return metric.execute();
    }
}
