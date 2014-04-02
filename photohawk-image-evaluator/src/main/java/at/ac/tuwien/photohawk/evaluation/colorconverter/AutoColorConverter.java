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
package at.ac.tuwien.photohawk.evaluation.colorconverter;


import at.ac.tuwien.photohawk.evaluation.colorconverter.ciexyz.CIEXYZColorConverter;
import at.ac.tuwien.photohawk.evaluation.colorconverter.noconversion.NoConversionColorConverter;
import at.ac.tuwien.photohawk.evaluation.colorconverter.srgb.SRGBColorConverter;
import at.ac.tuwien.photohawk.evaluation.util.ConvenientBufferedImageWrapper;

import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;

/**
 * This ColorConverter performs a migration only, if necessary. If both images
 * have the same color system and space, there is no conversion performed.
 * Otherwise a conversion into the specified system is performed.
 *
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 */
public class AutoColorConverter implements ColorConverter<StaticColor> {
    private ColorConverter<?> converter;

    /**
     * Creates a new color converter for the provided image depending on the match.
     *
     * @param img       the image for the color converter
     * @param match     the matching image
     * @param otherwise the color converter to use
     */
    public AutoColorConverter(ConvenientBufferedImageWrapper img, ConvenientBufferedImageWrapper match,
                              AlternativeColorConverter otherwise) {
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

    @Override
    public StaticColor getColorChannels(int x, int y) {
        return converter.getColorChannels(x, y);
    }

    @Override
    public String[] getChannelDescription() {
        return converter.getChannelDescription();
    }

    @Override
    public String getChannelDescription(int idx) {
        return converter.getChannelDescription(idx);
    }

    @Override
    public int getNumberOfChannels() {
        return converter.getNumberOfChannels();
    }

    @Override
    public StaticColor getNullColor() {
        return converter.getNullColor();
    }

    /**
     * Color converters.
     */
    public enum AlternativeColorConverter {
        CIEXYZ,
        SRGB
    }
}
