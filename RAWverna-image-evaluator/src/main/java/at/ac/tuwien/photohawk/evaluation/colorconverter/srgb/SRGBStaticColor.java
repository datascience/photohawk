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
package at.ac.tuwien.photohawk.evaluation.colorconverter.srgb;

import at.ac.tuwien.photohawk.evaluation.colorconverter.AbstractStaticColor;

/**
 * A simple RGB Color.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 */
public class SRGBStaticColor extends AbstractStaticColor {

    public static final String[] CHANNEL_NAMES = new String[] {"R", "G", "B"};

    /**
     * Creates a new SRGBStaticColor.
     * 
     * @param values
     *            the color values.
     */
    public SRGBStaticColor(float[] values) {
        super(values);
    }

    /**
     * Creates a new SRGBStaticColor.
     * 
     * @param r
     *            red value
     * @param g
     *            green value
     * @param b
     *            blue value
     */
    public SRGBStaticColor(float r, float g, float b) {
        this(new float[] {r, g, b});
    }

    @Override
    public String[] getChannelDescription() {
        return CHANNEL_NAMES;
    }
}
