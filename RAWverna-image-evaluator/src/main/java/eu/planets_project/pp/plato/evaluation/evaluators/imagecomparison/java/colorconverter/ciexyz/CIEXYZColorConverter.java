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
package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.ciexyz;

import java.awt.color.ICC_ColorSpace;

import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.ColorConverter;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.ICCProfileColorConverter;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.StaticColor;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.util.ConvenientBufferedImageWrapper;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.util.ImageException;

/**
 * This ColorConverter can convert colors to CIE-XYZ.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 * @version 1.0
 */
public class CIEXYZColorConverter extends ICCProfileColorConverter implements ColorConverter<CIEXYZStaticColor> {

    public CIEXYZColorConverter(ConvenientBufferedImageWrapper img) throws ImageException {
        super(img, ICC_ColorSpace.getInstance(ICC_ColorSpace.CS_CIEXYZ));
    }

    public CIEXYZStaticColor getColorChannels(int x, int y) {
        return new CIEXYZStaticColor(img.getSample(x, y));
    }

    public int getNumberOfChannels() {
        return 3;
    }

    public StaticColor getNullColor() {
        return new CIEXYZStaticColor(0, 0, 0);
    }

    public String[] getChannelDescription() {
        return CIEXYZStaticColor.channelNames;
    }

    public String getChannelDescription(int idx) {
        return getChannelDescription()[idx];
    }
}
