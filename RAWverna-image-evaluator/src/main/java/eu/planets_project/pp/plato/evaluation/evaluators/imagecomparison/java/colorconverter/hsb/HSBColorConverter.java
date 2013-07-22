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
 * @version 1.0
 */
public class HSBColorConverter implements ColorConverter<HSBStaticColor> {

    private SRGBColorConverter img;

    public HSBColorConverter(SRGBColorConverter img) {
        this.img = img;
    }

    public HSBStaticColor getColorChannels(int x, int y) {
        float[] data = img.getColorChannels(x, y).getChannelValues();
        float[] converted = Color.RGBtoHSB((int) (data[0] * 255), (int) (data[1] * 255), (int) (data[2] * 255), null);
        return new HSBStaticColor(converted[2], converted[1], converted[0]);
    }

    public String[] getChannelDescription() {
        return HSBStaticColor.channelNames;
    }

    public String getChannelDescription(int idx) {
        return getChannelDescription()[idx];
    }

    public StaticColor getNullColor() {
        return new HSBStaticColor(0, 0, 0);
    }

    public int getNumberOfChannels() {
        return 3;
    }

    public static double normalizeHueDifference(double value) {
        if (value > 0.5) {
            return (1 - value);
        } else {
            return value;
        }
    }

}
