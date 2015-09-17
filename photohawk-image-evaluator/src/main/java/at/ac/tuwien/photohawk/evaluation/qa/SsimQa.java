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
import at.ac.tuwien.photohawk.evaluation.colorconverter.hsb.HSBColorConverter;
import at.ac.tuwien.photohawk.evaluation.colorconverter.srgb.SRGBColorConverter;
import at.ac.tuwien.photohawk.evaluation.operation.TransientOperation;
import at.ac.tuwien.photohawk.evaluation.operation.metric.SimpleSSIMMetric;
import at.ac.tuwien.photohawk.evaluation.preprocessing.ScaleToNearestFactorPreprocessor;
import at.ac.tuwien.photohawk.evaluation.preprocessing.ShrinkResizePreprocessor;
import at.ac.tuwien.photohawk.evaluation.util.ConvenientBufferedImageWrapper;

import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * Quality assurance for structured similarity metric.
 */
public class SsimQa implements Qa<Float, StaticColor> {

    private int targetSize;
    private int numThreads;

    /**
     * Create a new SSIM QA with default parameters.
     */
    public SsimQa() {
        this.targetSize = SimpleSSIMMetric.DEFAULT_TARGET_SIZE;
        this.numThreads = SimpleSSIMMetric.DEFAULT_THREADPOOL_SIZE;
    }

    /**
     * Sets the target size.
     *
     * @param targetSize the target size to set
     * @return this Qa object
     */
    public SsimQa targetSize(int targetSize) {
        this.targetSize = targetSize;
        return this;
    }

    /**
     * Sets the number of threads, if numThreads is < 0 no threads will be used.
     *
     * @param numThreads the number of threads to use
     * @return this Qa object
     */
    public SsimQa numThreads(int numThreads) {
        this.numThreads = numThreads;
        return this;
    }

    @Override
    public TransientOperation<Float, StaticColor> evaluate(final BufferedImage left, final BufferedImage right) {
        // Convert to SRGB
        BufferedImage leftImg = new SRGBColorConverter(
                new ConvenientBufferedImageWrapper(left)).getImage().getBufferedImage();
        BufferedImage rightImg = new SRGBColorConverter(
                new ConvenientBufferedImageWrapper(right)).getImage().getBufferedImage();

        // Resize
        ShrinkResizePreprocessor shrink = new ShrinkResizePreprocessor(leftImg, rightImg);
        shrink.process();
        leftImg = shrink.getResult1();
        rightImg = shrink.getResult2();
        shrink = null;

        // Scale
        ScaleToNearestFactorPreprocessor scale = new ScaleToNearestFactorPreprocessor(leftImg, rightImg, targetSize);
        scale.process();
        leftImg = scale.getResult1();
        rightImg = scale.getResult2();
        scale = null;

        // Evaluate
        HSBColorConverter c1 = new HSBColorConverter(
                new SRGBColorConverter(new ConvenientBufferedImageWrapper(leftImg)));
        HSBColorConverter c2 = new HSBColorConverter(
                new SRGBColorConverter(new ConvenientBufferedImageWrapper(rightImg)));

        SimpleSSIMMetric metric = new SimpleSSIMMetric(c1, c2, new Point(0, 0),
                                                       new Point(leftImg.getWidth(), leftImg.getHeight()), numThreads);

        // Evaluategi
        return metric.execute();
    }
}
