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
package at.ac.tuwien.photohawk.evaluation.colorconverter.hsb;

import at.ac.tuwien.photohawk.evaluation.colorconverter.AbstractStaticColor;

/**
 * A simple HSB Color. Note that the channel order is changed to BSH.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 */
public class HSBStaticColor extends AbstractStaticColor {

    public static final String[] CHANNEL_NAMES = new String[] {"B", "S", "H"};

    /**
     * Creates a new HSBStaticColor.
     * 
     * @param values
     *            the channel values
     */
    public HSBStaticColor(float[] values) {
        super(values);
    }

    /**
     * Creates a new HSBStaticColor.
     * 
     * @param b
     *            B channel value
     * @param s
     *            S channel value
     * @param h
     *            H channel value
     */
    public HSBStaticColor(float b, float s, float h) {
        this(new float[] {b, s, h});
    }

    public String[] getChannelDescription() {
        return HSBStaticColor.CHANNEL_NAMES;
    }
}
