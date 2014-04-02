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
 * Preprocessor that checks if two images have the same size.
 */
public class CheckEqualSizePreprocessor implements Preprocessor {

    private static final Logger LOGGER = LogManager.getLogger(CheckEqualSizePreprocessor.class);

    private BufferedImage img1;
    private BufferedImage img2;

    /**
     * Creates a new preprocessor.
     *
     * @param img1 image 1
     * @param img2 image 2
     */
    public CheckEqualSizePreprocessor(final BufferedImage img1, final BufferedImage img2) {
        this.img1 = img1;
        this.img2 = img2;
    }

    /**
     * Checks if the images have the same size. Throws a
     * {@link PreprocessingException} if they don't have the same size.
     */
    @Override
    public void process() {
        if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
            throw new PreprocessingException("Image size does not match");
        }
        LOGGER.debug("Images have the same widht [{}] and height [{}]", img1.getWidth(), img1.getHeight());
    }

    @Override
    public BufferedImage getResult1() {
        return img1;
    }

    @Override
    public BufferedImage getResult2() {
        return img2;
    }
}
