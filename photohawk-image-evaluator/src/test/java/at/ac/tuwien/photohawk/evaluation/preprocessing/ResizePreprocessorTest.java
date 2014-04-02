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
import org.junit.Assert;
import org.junit.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Unit tests for ResizePreprocessor.
 */
public class ResizePreprocessorTest {

    @Test
    public void executeTest_equalSize() {
        BufferedImage img1 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0));
        BufferedImage img2 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0));
        ResizePreprocessor pp = new ResizePreprocessor(img1, img2, BufferedImageHelper.DEFAULT_WIDTH,
                                                       BufferedImageHelper.DEFAULT_HEIGHT);
        pp.process();

        Assert.assertEquals(img1, pp.getResult1());
        Assert.assertEquals(img2, pp.getResult2());
    }

    @Test
    public void executeTest_ltWidth_ltHeight() {
        BufferedImage img1 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0));
        BufferedImage img2 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0));
        ResizePreprocessor pp = new ResizePreprocessor(img1, img2, 100, 300);
        pp.process();

        Assert.assertEquals(100, pp.getResult1().getWidth());
        Assert.assertEquals(300, pp.getResult1().getHeight());
        Assert.assertEquals(100, pp.getResult2().getWidth());
        Assert.assertEquals(300, pp.getResult2().getHeight());
    }

    @Test
    public void executeTest_gtWidth_gtHeight() {
        BufferedImage img1 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0));
        BufferedImage img2 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0));
        ResizePreprocessor pp = new ResizePreprocessor(img1, img2, 600, 700);
        pp.process();

        Assert.assertEquals(600, pp.getResult1().getWidth());
        Assert.assertEquals(700, pp.getResult1().getHeight());
        Assert.assertEquals(600, pp.getResult2().getWidth());
        Assert.assertEquals(700, pp.getResult2().getHeight());
    }
}
