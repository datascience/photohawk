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

/**
 * This interface describes a simple Color object.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 */
public interface StaticColor extends ColorSystem {

    /**
     * Returns the channel values.
     * 
     * @return an array of values
     */
    float[] getChannelValues();

    /**
     * Returns the channel value of channel idx.
     * 
     * @param idx
     *            the index of the channel
     * @return the channel value
     */
    float getChannelValue(int idx);

    /**
     * Sets the channel values.
     * 
     * @param channelValues
     *            an array of channel values.
     */
    void setChannelValues(float[] channelValues);

    /**
     * Sets the channel value of channel idx.
     * 
     * @param idx
     *            the index of the channel
     * @param channelValue
     *            the channel value
     */
    void setChannelValue(int idx, float channelValue);

}
