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
 * Unit tests for ScalePreprocessor.
 */
public class ScalePreprocessorTest {

    @Test
    public void executeTest_equalSize() {
        BufferedImage img1 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0));
        BufferedImage img2 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0));
        ScalePreprocessor pp = new ScalePreprocessor(img1, img2, 1.0d, 1.0d);
        pp.process();

        Assert.assertEquals(BufferedImageHelper.DEFAULT_WIDTH, pp.getResult1().getWidth());
        Assert.assertEquals(BufferedImageHelper.DEFAULT_HEIGHT, pp.getResult1().getHeight());
        Assert.assertEquals(BufferedImageHelper.DEFAULT_WIDTH, pp.getResult2().getWidth());
        Assert.assertEquals(BufferedImageHelper.DEFAULT_HEIGHT, pp.getResult2().getHeight());
    }

    @Test
    public void executeTest_ltWidth_ltHeight() {
        BufferedImage img1 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0));
        BufferedImage img2 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0));
        ScalePreprocessor pp = new ScalePreprocessor(img1, img2, 0.5d, 0.5d);
        pp.process();

        Assert.assertEquals(BufferedImageHelper.DEFAULT_WIDTH / 2, pp.getResult1().getWidth());
        Assert.assertEquals(BufferedImageHelper.DEFAULT_HEIGHT / 2, pp.getResult1().getHeight());
        Assert.assertEquals(BufferedImageHelper.DEFAULT_WIDTH / 2, pp.getResult2().getWidth());
        Assert.assertEquals(BufferedImageHelper.DEFAULT_HEIGHT / 2, pp.getResult2().getHeight());
    }

    @Test
    public void executeTest_gtWidth_gtHeight() {
        BufferedImage img1 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0));
        BufferedImage img2 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0));
        ScalePreprocessor pp = new ScalePreprocessor(img1, img2, 2.0d, 2.0d);
        pp.process();

        Assert.assertEquals(2 * BufferedImageHelper.DEFAULT_WIDTH, pp.getResult1().getWidth());
        Assert.assertEquals(2 * BufferedImageHelper.DEFAULT_HEIGHT, pp.getResult1().getHeight());
        Assert.assertEquals(2 * BufferedImageHelper.DEFAULT_WIDTH, pp.getResult2().getWidth());
        Assert.assertEquals(2 * BufferedImageHelper.DEFAULT_HEIGHT, pp.getResult2().getHeight());
    }
}
