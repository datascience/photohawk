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

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;

/**
 * Preprocessor that resizes images to the specified width and height.
 */
public class ResizePreprocessor implements Preprocessor {

    private static final Logger LOGGER = LogManager.getLogger(ResizePreprocessor.class);

    private static final int INTERPOLATION_TYPE = AffineTransformOp.TYPE_BICUBIC;

    private static final int[] SIGNIFICANT_BITS_PER_COMPONENT = new int[]{16, 16, 16};

    protected BufferedImage img1;
    protected BufferedImage img2;

    protected int width;
    protected int height;

    protected BufferedImage result1;
    protected BufferedImage result2;

    /**
     * Creates a new resize preprocessor.
     *
     * @param img1   image 1
     * @param img2   image 2
     * @param width  with to scale to
     * @param height height to scale to
     */
    public ResizePreprocessor(final BufferedImage img1, final BufferedImage img2, final int width, final int height) {
        this.img1 = img1;
        this.img2 = img2;
        this.width = width;
        this.height = height;
    }

    @Override
    public void process() {
        result1 = resizeImage(img1);
        result2 = resizeImage(img2);
    }

    /**
     * Scales an image to the width and height of this instance.
     *
     * @param img the image to scale
     * @return a new scaled image or the original image if no scaling was
     * necessary
     */
    private BufferedImage resizeImage(final BufferedImage img) {
        if (img.getWidth() == width && img.getHeight() == height) {
            LOGGER.debug("Image already has the width [{}] and height [{}]", img.getWidth(), img.getHeight());
            return img;
        }

        LOGGER.info("Resizing image to width [{}], height [{}].", width, height);

        // Create new image with new dimensions
        AffineTransformOp op = new AffineTransformOp(
                AffineTransform.getScaleInstance(width / (double) img.getWidth(), height / (double) img.getHeight()),
                INTERPOLATION_TYPE);

        BufferedImage result = op.createCompatibleDestImage(img,
                                                            new ComponentColorModel(img.getColorModel().getColorSpace(),
                                                                                    SIGNIFICANT_BITS_PER_COMPONENT,
                                                                                    img.getColorModel().hasAlpha(),
                                                                                    img.getColorModel().isAlphaPremultiplied(),
                                                                                    img.getColorModel().getTransparency(),
                                                                                    DataBuffer.TYPE_INT)
        );

        // Draw image
        Graphics2D graphics = result.createGraphics();
        graphics.drawImage(img, -(img.getWidth() - width) / 2, -(img.getHeight() - height) / 2, null);

        return result;
    }

    @Override
    public BufferedImage getResult1() {
        return result1;
    }

    @Override
    public BufferedImage getResult2() {
        return result2;
    }
}
