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
package at.ac.tuwien.photohawk.evaluation.colorconverter.ciexyz;

import at.ac.tuwien.photohawk.evaluation.colorconverter.ColorConverter;
import at.ac.tuwien.photohawk.evaluation.colorconverter.ICCProfileColorConverter;
import at.ac.tuwien.photohawk.evaluation.colorconverter.StaticColor;
import at.ac.tuwien.photohawk.evaluation.util.ConvenientBufferedImageWrapper;

import java.awt.color.ICC_ColorSpace;


/**
 * This ColorConverter can convert colors to CIE-XYZ.
 *
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 */
public class CIEXYZColorConverter extends ICCProfileColorConverter implements ColorConverter<CIEXYZStaticColor> {

    /**
     * Creates a new CIEXYZColorConverter.
     *
     * @param img the image
     */
    public CIEXYZColorConverter(ConvenientBufferedImageWrapper img) {
        super(img, ICC_ColorSpace.getInstance(ICC_ColorSpace.CS_CIEXYZ));
    }

    /**
     * Returns the color channels at the specified coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the color channels
     */
    public CIEXYZStaticColor getColorChannels(int x, int y) {
        return new CIEXYZStaticColor(img.getSample(x, y));
    }

    @Override
    public String[] getChannelDescription() {
        return CIEXYZStaticColor.CHANNEL_NAMES;
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
        return new CIEXYZStaticColor(0, 0, 0);
    }
}
