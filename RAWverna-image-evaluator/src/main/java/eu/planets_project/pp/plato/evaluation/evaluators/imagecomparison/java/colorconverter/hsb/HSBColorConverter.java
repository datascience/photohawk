/*******************************************************************************
 * Copyright 2010-2013 Vienna University of Technology
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
package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.hsb;

import java.awt.Color;

import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.ColorConverter;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.StaticColor;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.srgb.SRGBColorConverter;

/**
 * This ColorConverter can convert from sRGB to HSB.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 */
public class HSBColorConverter implements ColorConverter<HSBStaticColor> {
    private static final int COMPONENT_FACTOR = 255;
    private SRGBColorConverter img;

    /**
     * Creates a new HSBColorConverter.
     * 
     * @param img
     *            the image
     */
    public HSBColorConverter(SRGBColorConverter img) {
        this.img = img;
    }

    /**
     * Returns the color channels at the specified coordinates.
     * 
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     * @return the color channels
     */
    public HSBStaticColor getColorChannels(int x, int y) {
        float[] data = img.getColorChannels(x, y).getChannelValues();
        float[] converted = Color.RGBtoHSB((int) (data[0] * COMPONENT_FACTOR), (int) (data[1] * COMPONENT_FACTOR),
            (int) (data[2] * COMPONENT_FACTOR), null);
        return new HSBStaticColor(converted[2], converted[1], converted[0]);
    }

    @Override
    public String[] getChannelDescription() {
        return HSBStaticColor.CHANNEL_NAMES;
    }

    @Override
    public String getChannelDescription(int idx) {
        return getChannelDescription()[idx];
    }

    @Override
    public int getNumberOfChannels() {
        return getChannelDescription().length;
    }

    @Override
    public StaticColor getNullColor() {
        return new HSBStaticColor(0, 0, 0);
    }

    /**
     * Normalizes the hue difference.
     * 
     * @param value
     *            heu value
     * @return the normalized value
     */
    public static double normalizeHueDifference(double value) {
        if (value > 0.5) {
            return 1 - value;
        } else {
            return value;
        }
    }

}
