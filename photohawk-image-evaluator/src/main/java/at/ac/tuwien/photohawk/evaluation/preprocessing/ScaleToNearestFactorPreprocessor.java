/*******************************************************************************
 * Copyright 2013 Vienna University of Technology
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
package at.ac.tuwien.photohawk.evaluation.preprocessing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;

/**
 * Preprocessor that scales images to the smaller width and height respectively.
 */
public class ScaleToNearestFactorPreprocessor extends ScalePreprocessor {

    private static final Logger LOGGER = LogManager.getLogger(ScaleToNearestFactorPreprocessor.class);

    private static final float THRESHOLD = 0.95f;

    /**
     * Creates a new scale preprocessor.
     *
     * @param img1       image 1
     * @param img2       image 2
     * @param targetSize maximum size of the smaller side
     */
    public ScaleToNearestFactorPreprocessor(final BufferedImage img1, final BufferedImage img2, int targetSize) {
        super(img1, img2, calcScaleFactor(img1, targetSize), calcScaleFactor(img1, targetSize),
              calcScaleFactor(img2, targetSize), calcScaleFactor(img2, targetSize));
    }

    /**
     * Calculates a scale factor for the provided image and the target size. The
     * scale factor is calculated from the smaller side of the image and the
     * provided target size. The resulting factor only allows scaling down, i.e.
     * is always <= 1. Further the divisor of the factor is rounded to the
     * nearest integer value.
     *
     * @param img        image to use
     * @param targetSize the target size
     * @return a scale factor
     */
    private static double calcScaleFactor(final BufferedImage img, final int targetSize) {
        return 1.0d / Math.max(1, Math.round(Math.min(img.getWidth(), img.getHeight()) / (double) targetSize));
    }

    @Override
    public void process() {
        if (widthFactor1 < THRESHOLD || heightFactor1 < THRESHOLD || widthFactor2 < THRESHOLD ||
                heightFactor2 < THRESHOLD) {
            LOGGER.info("Scaling image with factors [{}], [{}]", widthFactor1, heightFactor1);
            super.process();
        } else {
            LOGGER.info("All scale factors [{}], [{}], [{}], [{}] are above threshold [{}]", widthFactor1,
                        heightFactor1, widthFactor2, heightFactor2, THRESHOLD);
            result1 = img1;
            result2 = img2;
        }
    }
}
