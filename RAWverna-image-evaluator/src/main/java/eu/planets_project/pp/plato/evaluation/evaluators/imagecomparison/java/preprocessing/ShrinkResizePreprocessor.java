/*******************************************************************************
 * Copyright 2013 Vienna University of Technology
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
package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.preprocessing;

import java.awt.image.BufferedImage;

/**
 * Preprocessor that resizes images to the smaller width and height of the
 * images respecitively.
 */
public class ShrinkResizePreprocessor extends ResizePreprocessor {

    /**
     * Creates a new resize preprocessor.
     * 
     * @param img1
     *            image 1
     * @param img2
     *            image 2
     */
    public ShrinkResizePreprocessor(BufferedImage img1, BufferedImage img2) {
        super(img1, img2, Math.min(img1.getWidth(), img2.getWidth()), Math.min(img1.getWidth(), img2.getWidth()));
    }
}
