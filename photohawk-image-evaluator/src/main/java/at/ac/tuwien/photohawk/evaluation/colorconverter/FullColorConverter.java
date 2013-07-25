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

import at.ac.tuwien.photohawk.evaluation.util.ConvenientBufferedImageWrapper;

/**
 * This interface describes ColorConverters that can transform an image at once.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 */
public interface FullColorConverter {

    /**
     * Returns the converted image.
     * 
     * @return the image
     */
    ConvenientBufferedImageWrapper getImage();
}
