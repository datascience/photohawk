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

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Quality assurance for structured similarity metric.
 */
public class SsimQa implements Qa<Float, StaticColor> {

    private int targetSize;
    private boolean doThreaded;
    private int numThreads;

    /**
     * Create a new SSIM QA with default parameters.
     */
    public SsimQa() {
        this.targetSize = SimpleSSIMMetric.DEFAULT_TARGET_SIZE;
        this.doThreaded = SimpleSSIMMetric.DEFAULT_DO_THREADED;
        this.numThreads = SimpleSSIMMetric.DEFAULT_THREADPOOL_SIZE;
    }

    /**
     * Create a new SSIM QA with the provided parameters.
     *
     * @param targetSize SSIM target size
     */
    public SsimQa(final int targetSize, final boolean doThreaded, final int numThreads) {
        this.targetSize = targetSize;
        this.doThreaded = doThreaded;
        this.numThreads = numThreads;
    }

    @Override
    public TransientOperation<Float, StaticColor> evaluate(final BufferedImage left, final BufferedImage right) {
        // Convert to SRGB
        BufferedImage leftImg = new SRGBColorConverter(
                new ConvenientBufferedImageWrapper(left)).getImage()
                .getBufferedImage();
        BufferedImage rightImg = new SRGBColorConverter(
                new ConvenientBufferedImageWrapper(right)).getImage()
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
                leftImg, rightImg, targetSize);
        scale.process();
        leftImg = scale.getResult1();
        rightImg = scale.getResult2();
        scale = null;

        // Evaluate
        HSBColorConverter c1 = new HSBColorConverter(new SRGBColorConverter(new ConvenientBufferedImageWrapper(leftImg)));
        HSBColorConverter c2 = new HSBColorConverter(new SRGBColorConverter(new ConvenientBufferedImageWrapper(rightImg)));

        // TODO: What happens if one image is smaller than the SCALE_TARGET_SIZE?
        SimpleSSIMMetric metric = null;
        if (!doThreaded || numThreads <= 0) {
            metric = new SimpleSSIMMetric(c1, c2, new Point(0, 0), new Point(leftImg.getWidth(), leftImg.getHeight()), doThreaded);
        } else {
            metric = new SimpleSSIMMetric(c1, c2, new Point(0, 0), new Point(leftImg.getWidth(), leftImg.getHeight()), doThreaded, numThreads);
        }

        // Evaluate
        return metric.execute();
    }
}
