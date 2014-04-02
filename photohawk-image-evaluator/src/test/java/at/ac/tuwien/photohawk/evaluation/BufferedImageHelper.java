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
package at.ac.tuwien.photohawk.evaluation;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Helper class for creating test images.
 */
public final class BufferedImageHelper {

    public static final int DEFAULT_TYPE = BufferedImage.TYPE_INT_RGB;
    public static final int DEFAULT_WIDTH = 500;
    public static final int DEFAULT_HEIGHT = 500;

    /**
     * Constructor to avoid class instantiation.
     */
    private BufferedImageHelper() {
    }

    /**
     * Creates a new solid image with the provided color and default type, width and height.
     *
     * @param color the image color
     * @return the image
     */
    public static BufferedImage createSolidImage(Color color) {
        return createSolidImage(color, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Creates a new solid image with the provided color, width and height and default type.
     *
     * @param color  the image color
     * @param width  the image width
     * @param height the image height
     * @return the image
     */
    public static BufferedImage createSolidImage(Color color, int width, int height) {
        BufferedImage img = new BufferedImage(width, height, DEFAULT_TYPE);
        Graphics2D g = img.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, width, height);
        g.dispose();

        return img;
    }

    /**
     * Creates a new image with the top and bottom half containing a different color.
     *
     * @param topColor    the color of the top half of the image
     * @param bottomColor the color of the bottom half of the image
     * @return the image
     */
    public static BufferedImage createSolidHalfImage(Color topColor, Color bottomColor) {
        return createSolidHalfImage(topColor, bottomColor, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Creates a new image with the top and bottom half containing a different color.
     *
     * @param topColor    the color of the top half of the image
     * @param bottomColor the color of the bottom half of the image
     * @param width       the image width
     * @param height      the image height
     * @return the image
     */
    public static BufferedImage createSolidHalfImage(Color topColor, Color bottomColor, int width, int height) {
        BufferedImage img = new BufferedImage(width, height, DEFAULT_TYPE);
        Graphics2D g = img.createGraphics();
        g.setColor(topColor);
        g.fillRect(0, 0, height, width / 2);
        g.setColor(bottomColor);
        g.fillRect(0, width / 2, width, height);
        g.dispose();

        return img;
    }
}
