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
 * Unit tests for ScaleToNearestFactorPreprocessor.
 */
public class ScaleToNearestFactorPreprocessorTest {

    @Test
    public void executeTest_equalSize() {
        BufferedImage img1 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0));
        BufferedImage img2 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0));
        ScaleToNearestFactorPreprocessor pp = new ScaleToNearestFactorPreprocessor(img1, img2, 500);
        pp.process();

        Assert.assertEquals(img1, pp.getResult1());
        Assert.assertEquals(img2, pp.getResult2());
    }

    @Test
    public void executeTest_ltSize() {
        BufferedImage img1 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0));
        BufferedImage img2 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0));
        ScaleToNearestFactorPreprocessor pp = new ScaleToNearestFactorPreprocessor(img1, img2,
                                                                                   BufferedImageHelper.DEFAULT_WIDTH /
                                                                                           2);
        pp.process();

        Assert.assertEquals(BufferedImageHelper.DEFAULT_WIDTH / 2, pp.getResult1().getWidth());
        Assert.assertEquals(BufferedImageHelper.DEFAULT_HEIGHT / 2, pp.getResult1().getHeight());
        Assert.assertEquals(BufferedImageHelper.DEFAULT_WIDTH / 2, pp.getResult2().getWidth());
        Assert.assertEquals(BufferedImageHelper.DEFAULT_HEIGHT / 2, pp.getResult2().getHeight());
    }

    @Test
    public void executeTest_equalSize_gtSize() {
        BufferedImage img1 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0), 400, 200);
        BufferedImage img2 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0), 100, 50);
        ScaleToNearestFactorPreprocessor pp = new ScaleToNearestFactorPreprocessor(img1, img2, 200);
        pp.process();

        Assert.assertEquals(400, pp.getResult1().getWidth());
        Assert.assertEquals(200, pp.getResult1().getHeight());
        Assert.assertEquals(100, pp.getResult2().getWidth());
        Assert.assertEquals(50, pp.getResult2().getHeight());
    }

    @Test
    public void executeTest_ltSize_gtSize() {
        BufferedImage img1 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0), 400, 200);
        BufferedImage img2 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0), 100, 50);
        ScaleToNearestFactorPreprocessor pp = new ScaleToNearestFactorPreprocessor(img1, img2, 100);
        pp.process();

        Assert.assertEquals(200, pp.getResult1().getWidth());
        Assert.assertEquals(100, pp.getResult1().getHeight());
        Assert.assertEquals(100, pp.getResult2().getWidth());
        Assert.assertEquals(50, pp.getResult2().getHeight());
    }

    @Test
    public void executeTest_ltSize_gtSize_floor() {
        BufferedImage img1 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0), 400, 200);
        BufferedImage img2 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0), 100, 50);
        ScaleToNearestFactorPreprocessor pp = new ScaleToNearestFactorPreprocessor(img1, img2, 120);
        pp.process();

        Assert.assertEquals(200, pp.getResult1().getWidth());
        Assert.assertEquals(100, pp.getResult1().getHeight());
        Assert.assertEquals(100, pp.getResult2().getWidth());
        Assert.assertEquals(50, pp.getResult2().getHeight());
    }

    @Test
    public void executeTest_ltSize_ltSize_ceil_floor() {
        BufferedImage img1 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0), 400, 200);
        BufferedImage img2 = BufferedImageHelper.createSolidImage(new Color(0, 0, 0), 360, 300);
        ScaleToNearestFactorPreprocessor pp = new ScaleToNearestFactorPreprocessor(img1, img2, 90);
        pp.process();

        Assert.assertEquals(200, pp.getResult1().getWidth());
        Assert.assertEquals(100, pp.getResult1().getHeight());
        Assert.assertEquals(120, pp.getResult2().getWidth());
        Assert.assertEquals(100, pp.getResult2().getHeight());
    }
}
