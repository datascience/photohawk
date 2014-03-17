/*******************************************************************************
 * Copyright 2010-2014 Vienna University of Technology
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

package at.ac.tuwien.photohawk.dcraw;

import java.util.ArrayList;
import java.util.List;

/**
 * Dcraw conversion parameters.
 */
public class ConversionParameters {
    private boolean use16Bit;
    private boolean useTIFF;
    private ConversionColorSpace cs;
    private InterpretationType interpolation;
    private boolean useCameraWhiteBalance;
    private boolean useFixedWhiteLevel;
    private boolean useAutoStretchOrRotation;

    /**
     * Creates new conversion parameters with default values.
     */
    public ConversionParameters() {
        this(true, true, ConversionColorSpace.sRGB, InterpretationType.ColorInterpolated, true, true, false);
    }

    /**
     * Creates new conversion parameters with the provided values.
     *
     * @param use16Bit                 indicates whether to use 16 bit
     * @param useTIFF                  indicates whether to output tiff
     * @param cs                       the conversion color space
     * @param interpolation            the interpretation type
     * @param useCameraWhitebalance    indicates whether to use camera whitebalance
     * @param useFixedWhiteLevel       indicates whether to use fixed white level
     * @param useAutoStretchOrRotation indicate whether to use autostretch or rotation
     */
    public ConversionParameters(boolean use16Bit, boolean useTIFF, ConversionColorSpace cs,
                                InterpretationType interpolation, boolean useCameraWhitebalance, boolean useFixedWhiteLevel, boolean useAutoStretchOrRotation) {
        this.use16Bit = use16Bit;
        this.useTIFF = useTIFF;
        this.cs = cs;
        this.interpolation = interpolation;
        this.useCameraWhiteBalance = useCameraWhitebalance;
        this.useFixedWhiteLevel = useFixedWhiteLevel;
        this.useAutoStretchOrRotation = useAutoStretchOrRotation;
    }

    public boolean isUse16Bit() {
        return use16Bit;
    }

    public boolean isUseTIFF() {
        return useTIFF;
    }

    public boolean isUseCameraWhiteBalance() {
        return useCameraWhiteBalance;
    }

    public boolean isUseFixedWhiteLevel() {
        return useFixedWhiteLevel;
    }

    public ConversionColorSpace getCs() {
        return cs;
    }

    public InterpretationType getInterpolation() {
        return interpolation;
    }

    public boolean isUseAutoStretchOrRotation() {
        return useAutoStretchOrRotation;
    }

    /**
     * Returns the current state of this parameters as a list of arguments.
     *
     * @return a list of arguments
     */
    public List<String> arguments() {
        List<String> arguments = new ArrayList<String>();
        if (isUse16Bit()) {
            arguments.add("-6");
        }

        if (isUseTIFF()) {
            arguments.add("-T");
        }

        if (!isUseAutoStretchOrRotation()) {
            arguments.add("-j");
        }

        if (isUseFixedWhiteLevel()) {
            arguments.add("-W");
        }

        if (isUseCameraWhiteBalance()) {
            arguments.add("-w");
        }

        // Set wanted ColorSpace
        arguments.add("-o");
        arguments.add("params.getCs().getValue()");

        // Set interpolation level
        if (!getInterpolation().getValue().isEmpty()) {
            arguments.add(getInterpolation().getValue());
        }

        return arguments;
    }

    /**
     * The output colorspace. See dcraw's -o parameter for details.
     */
    public enum ConversionColorSpace {
        NoConversion(0),
        sRGB(1),
        AdobeRGB(2),
        WideGamutRGB(3),
        KodakProPhoto(4),
        XYZ(5);

        private final int value;

        ConversionColorSpace(int value) {
            this.value = value;
        }

        private int getValue() {
            return value;
        }
    }

    /**
     * Interpretation type.
     */
    public enum InterpretationType {
        /**
         * Document mode without scaling (totally raw)
         */
        NoInterpretation("-D"),

        /**
         * Document mode (no color, no interpolation)
         */
        Delinearize("-d"),
        ColorInterpolated("");

        private final String value;

        InterpretationType(String value) {
            this.value = value;
        }

        private String getValue() {
            return value;
        }
    }
}
