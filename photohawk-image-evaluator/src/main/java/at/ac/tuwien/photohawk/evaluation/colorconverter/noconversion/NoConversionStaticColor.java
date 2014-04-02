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
package at.ac.tuwien.photohawk.evaluation.colorconverter.noconversion;

import at.ac.tuwien.photohawk.evaluation.colorconverter.AbstractStaticColor;

/**
 * A simple generic Color.
 *
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 */
public class NoConversionStaticColor extends AbstractStaticColor {

    private final String[] channelNames;

    /**
     * Creates a new NoConversionStaticColor.
     *
     * @param channelNames the channel names
     * @param values       the channel values
     */
    public NoConversionStaticColor(String[] channelNames, float[] values) {
        super(values);

        if (null == channelNames) {
            channelNames = new String[getNumberOfChannels()];
            for (int i = 0; i < getNumberOfChannels(); i++) {
                channelNames[i] = String.valueOf(i + 1);
            }
        }
        this.channelNames = channelNames;
    }

    /**
     * Creates a new NoConversionStaticColor with consecutive numbers as channel
     * names.
     *
     * @param values the channel values
     */
    @Deprecated
    public NoConversionStaticColor(float[] values) {
        this(null, values);
    }

    public String[] getChannelDescription() {
        return channelNames;
    }

    public int getNumberOfChannels() {
        return channelValues.length;
    }
}
