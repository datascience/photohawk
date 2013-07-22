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

import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.StaticColor;

/**
 * A simple HSB Color. Note that the channel order is changed to BSH.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 */
public class HSBStaticColor implements StaticColor {

    public static final String[] channelNames = new String[] {"B", "S", "H"};

    private float[] values;

    public HSBStaticColor(float[] values) {
        setChannelValues(values);
    }

    public HSBStaticColor(float b, float s, float h) {
        this(new float[] {b, s, h});
    }

    public float getChannelValue(int idx) {
        return values[idx];
    }

    public float[] getChannelValues() {
        return values;
    }

    public void setChannelValues(float[] values) {
        this.values = values;
    }

    public void setChannelValue(int idx, float value) {
        values[idx] = value;
    }

    public String toString() {
        String result = "";
        for (int i = 0; i < values.length; i++) {
            result += getChannelDescription(i) + ": " + values[i] + "\n";
        }
        return result;
    }

    public String[] getChannelDescription() {
        return HSBStaticColor.channelNames;
    }

    public String getChannelDescription(int idx) {
        return getChannelDescription()[idx];
    }

    public int getNumberOfChannels() {
        return 3;
    }

}
