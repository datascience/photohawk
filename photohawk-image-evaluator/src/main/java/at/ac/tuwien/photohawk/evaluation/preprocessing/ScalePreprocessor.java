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
 * Preprocessor that scales images to the specified width and height.
 */
public class ScalePreprocessor implements Preprocessor {

    private static final Logger LOGGER = LogManager.getLogger(ScalePreprocessor.class);

    private static final int INTERPOLATION_TYPE = AffineTransformOp.TYPE_BICUBIC;

    private static final int[] SIGNIFICANT_BITS_PER_COMPONENT = new int[]{16, 16, 16};

    protected BufferedImage img1;
    protected BufferedImage img2;

    protected double widthFactor1;
    protected double heightFactor1;
    protected double widthFactor2;
    protected double heightFactor2;

    protected BufferedImage result1;
    protected BufferedImage result2;

    /**
     * Creates a new scale preprocessor.
     *
     * @param img1          image 1
     * @param img2          image 2
     * @param widthFactor1  with scale factor for image 1
     * @param heightFactor1 height scale factor for image 1
     * @param widthFactor2  with scale factor for image 2
     * @param heightFactor2 height scale factor for image 2
     */
    public ScalePreprocessor(final BufferedImage img1, final BufferedImage img2, final double widthFactor1,
                             final double heightFactor1, final double widthFactor2, final double heightFactor2) {
        this.img1 = img1;
        this.img2 = img2;
        this.widthFactor1 = widthFactor1;
        this.heightFactor1 = heightFactor1;
        this.widthFactor2 = widthFactor2;
        this.heightFactor2 = heightFactor2;
    }

    /**
     * Creates a new scale preprocessor.
     *
     * @param img1         image 1
     * @param img2         image 2
     * @param widthFactor  with scale factor for the images
     * @param heightFactor height scale factor for the images
     */
    public ScalePreprocessor(final BufferedImage img1, final BufferedImage img2, final double widthFactor,
                             final double heightFactor) {
        this(img1, img2, widthFactor, heightFactor, widthFactor, heightFactor);
    }

    @Override
    public void process() {
        result1 = scaleImage(img1, widthFactor1, heightFactor1);
        result2 = scaleImage(img2, widthFactor2, heightFactor2);
    }

    /**
     * Scales an image to the width and height of this instance.
     *
     * @param img the image to scale
     * @param sx  sx
     * @param sy  sy
     * @return a new scaled image or the original image if no scaling was
     * necessary
     */
    protected BufferedImage scaleImage(final BufferedImage img, final double sx, final double sy) {
        LOGGER.info("Scaling image by factors width [{}], height [{}].", sx, sy);

        // Create new result image
        AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(sx, sy), INTERPOLATION_TYPE);
        BufferedImage result = op.createCompatibleDestImage(img,
                                                            new ComponentColorModel(img.getColorModel().getColorSpace(),
                                                                                    SIGNIFICANT_BITS_PER_COMPONENT,
                                                                                    img.getColorModel().hasAlpha(),
                                                                                    img.getColorModel().isAlphaPremultiplied(),
                                                                                    img.getColorModel().getTransparency(),
                                                                                    DataBuffer.TYPE_INT)
        );

        // Draw scaled image to result
        AffineTransform scale = AffineTransform.getScaleInstance(sx, sy);
        Graphics2D graphics = result.createGraphics();
        graphics.setTransform(scale);
        graphics.drawImage(img, 0, 0, null);

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
