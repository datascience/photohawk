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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Reader for parameters.
 */
public class ParameterReader {

    private static final String DCRAW_PROPERTIES = "dcraw.properties";
    private static final String RAW_KEY = ".raw";
    private static final String NON_RAW_KEY = ".non-raw";

    /**
     * Reads parameters for the provided base key depending on whether the other image is RAW.
     *
     * @param baseKey  the parameter base key
     * @param otherRaw specifies whether the other image is RAW
     * @return a list of parameters or an empty list if no parameters were found
     * @throws DcrawException if an error occurred during read
     */
    public List<String> readParameters(final String baseKey, final boolean otherRaw) {
        if (otherRaw) {
            return readParameters(baseKey + RAW_KEY);
        } else {
            return readParameters(baseKey + NON_RAW_KEY);
        }
    }

    /**
     * Reads the parameters for the provided key.
     *
     * @param key the parameter key
     * @return a list of parameters or an empty list if not paramaeters were found
     * @throws DcrawException if an error occurred during read
     */
    private List<String> readParameters(final String key) {
        List<String> parameters = new ArrayList<String>();
        String p = null;
        try {
            p = getProperties().getProperty(key);
            if (p != null) {
                parameters.addAll(Arrays.asList(p.split("\\s+")));
            }
        } catch (IOException e) {
            throw new DcrawException("Error reading dcraw properties.", e);
        }
        return parameters;
    }

    /**
     * Reads git properties containing information about dcraw parameters.
     *
     * @return the properties
     * @throws IOException if the properties could not be read
     */
    private Properties getProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream(DCRAW_PROPERTIES));
        return properties;
    }
}
