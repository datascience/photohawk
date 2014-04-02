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

import at.ac.tuwien.photohawk.evaluation.colorconverter.AbstractStaticColor;

/**
 * A simple CIE XYZ Color.
 *
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 */
public class CIEXYZStaticColor extends AbstractStaticColor {

    public static final String[] CHANNEL_NAMES = new String[]{"X", "Y", "Z"};

    /**
     * Creates a new CIEXYZStaticColor.
     *
     * @param values the channel values
     */
    public CIEXYZStaticColor(float[] values) {
        super(values);
    }

    /**
     * Creates a new CIEXYZStaticColor.
     *
     * @param x X channel value
     * @param y Y channel value
     * @param z Z channel value
     */
    public CIEXYZStaticColor(float x, float y, float z) {
        this(new float[]{x, y, z});
    }

    public String[] getChannelDescription() {
        return CIEXYZStaticColor.CHANNEL_NAMES;
    }
}
