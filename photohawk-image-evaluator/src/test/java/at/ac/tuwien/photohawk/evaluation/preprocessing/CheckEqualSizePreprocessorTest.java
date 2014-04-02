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
package at.ac.tuwien.photohawk.evaluation.preprocessing;

import at.ac.tuwien.photohawk.evaluation.BufferedImageHelper;
import org.junit.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Unit tests for CheckEqualSizePreprocessor.
 */
public class CheckEqualSizePreprocessorTest {

    @Test
    public void executeTest_equalSize() {
        BufferedImage img1 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0));
        BufferedImage img2 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0));
        CheckEqualSizePreprocessor pp = new CheckEqualSizePreprocessor(img1, img2);
        pp.process();
    }

    @Test(expected = PreprocessingException.class)
    public void executeTest_equalWidth_diffHeight() {
        BufferedImage img1 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0), 500, 500);
        BufferedImage img2 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0), 500, 200);
        CheckEqualSizePreprocessor pp = new CheckEqualSizePreprocessor(img1, img2);
        pp.process();
    }

    @Test(expected = PreprocessingException.class)
    public void executeTest_diffWidth_equalHeight() {
        BufferedImage img1 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0), 200, 500);
        BufferedImage img2 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0), 500, 500);
        CheckEqualSizePreprocessor pp = new CheckEqualSizePreprocessor(img1, img2);
        pp.process();
    }

    @Test(expected = PreprocessingException.class)
    public void executeTest_diffWidth_diffHeight() {
        BufferedImage img1 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0), 200, 500);
        BufferedImage img2 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0), 500, 200);
        CheckEqualSizePreprocessor pp = new CheckEqualSizePreprocessor(img1, img2);
        pp.process();
    }
}
