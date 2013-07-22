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
package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.util;

import javax.imageio.ImageIO;

/**
 * This class provides convenience methods for interaction with image IO.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 * @version 1.0
 */
public class ImageIOUtil {

    /**
     * Return true if it is most likely that the normal Java Image API can read
     * the ImageAPI. If there is a more preferable reader than the standard one,
     * this method will return false.
     * 
     * @param suffix
     *            The suffix specifying the file type (may be null)
     * @param mime
     *            The mime type of the file (may be null)
     * @return Whether or not the image is readable by the Java Image API
     */
    public static boolean isReadable(String suffix, String mime) {
        if ("DNG".equalsIgnoreCase(suffix)) {
            return false;
        }
        if ("image/x-raw".equalsIgnoreCase(mime)) {
            return false;
        }
        if (null == mime || ImageIO.getImageReadersByMIMEType(mime).hasNext() == false) {
            if (null == suffix || ImageIO.getImageReadersBySuffix(suffix).hasNext() == false) {
                return false;
            }
        }
        return true;
    }

}
