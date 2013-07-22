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
package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter;

import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;

import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.ciexyz.CIEXYZColorConverter;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.noconversion.NoConversionColorConverter;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.srgb.SRGBColorConverter;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.util.ConvenientBufferedImageWrapper;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.util.ImageException;

/**
 * This ColorConverter performs a migration only, if necessary. If both images
 * have the same color system and space, there is no conversion performed.
 * Otherwise a conversion into the specified system is performed.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public class AutoColorConverter implements ColorConverter {

    private ColorConverter converter;

    @SuppressWarnings("deprecation")
    public AutoColorConverter(ConvenientBufferedImageWrapper img, ConvenientBufferedImageWrapper match,
        AlternativeColorConverter otherwise) throws ImageException {
        boolean needConversion = false;
        needConversion = needConversion || !img.getColorModel().equals(match.getColorModel());
        ColorSpace imgCS = img.getColorModel().getColorSpace();
        ColorSpace matchCS = match.getColorModel().getColorSpace();

        needConversion = needConversion || imgCS.getType() != matchCS.getType();
        needConversion = needConversion || imgCS.getNumComponents() != matchCS.getNumComponents();
        needConversion = needConversion || !imgCS.getClass().isAssignableFrom(matchCS.getClass());
        needConversion = needConversion || !matchCS.getClass().isAssignableFrom(imgCS.getClass());
        if (imgCS instanceof ICC_ColorSpace && matchCS instanceof ICC_ColorSpace) {
            ICC_Profile imgProf = ((ICC_ColorSpace) imgCS).getProfile();
            ICC_Profile matchProf = ((ICC_ColorSpace) matchCS).getProfile();
            needConversion = needConversion || imgProf.getColorSpaceType() != matchProf.getColorSpaceType();
            needConversion = needConversion || imgProf.getMajorVersion() != matchProf.getMajorVersion();
            needConversion = needConversion || imgProf.getMinorVersion() != matchProf.getMinorVersion();
            needConversion = needConversion || imgProf.getPCSType() != matchProf.getPCSType();
            needConversion = needConversion || imgProf.getProfileClass() != matchProf.getProfileClass();
            byte[] imgProfByte = imgProf.getData();
            byte[] matchProfByte = matchProf.getData();
            for (int i = 0; i < imgProfByte.length; i++) {
                if (i >= matchProfByte.length) {
                    needConversion = true;
                    break;
                } else {
                    needConversion = needConversion || imgProfByte[i] != matchProfByte[i];
                }
            }
        } else {
            needConversion = true;
        }

        if (needConversion) {
            if (otherwise.equals(AlternativeColorConverter.CIEXYZ)) {
                converter = new CIEXYZColorConverter(img);
            } else if (otherwise.equals(AlternativeColorConverter.SRGB)) {
                converter = new SRGBColorConverter(img);
            }
        } else {
            converter = new NoConversionColorConverter(img);
        }
    }

    public StaticColor getColorChannels(int x, int y) {
        return converter.getColorChannels(x, y);
    }

    public String[] getChannelDescription() {
        return converter.getChannelDescription();
    }

    public String getChannelDescription(int idx) {
        return converter.getChannelDescription(idx);
    }

    public StaticColor getNullColor() {
        return converter.getNullColor();
    }

    public int getNumberOfChannels() {
        return converter.getNumberOfChannels();
    }

    public enum AlternativeColorConverter {

        CIEXYZ,
        SRGB

    }

}
