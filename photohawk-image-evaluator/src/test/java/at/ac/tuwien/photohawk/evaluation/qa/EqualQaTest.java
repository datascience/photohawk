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
package at.ac.tuwien.photohawk.evaluation.qa;

import at.ac.tuwien.photohawk.evaluation.AbstractOperationTest;
import at.ac.tuwien.photohawk.evaluation.BufferedImageHelper;
import at.ac.tuwien.photohawk.evaluation.operation.TransientOperation;
import at.ac.tuwien.photohawk.evaluation.preprocessing.PreprocessingException;
import org.junit.Assert;
import org.junit.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Unit tests for AE QE.
 */
public class EqualQaTest extends AbstractOperationTest {

    @Test(expected = PreprocessingException.class)
    public void evaluateTest_differentWidth() {
        BufferedImage left = BufferedImageHelper.createSolidImage(new Color(0, 0, 0), 500, 500);
        BufferedImage right = BufferedImageHelper.createSolidImage(new Color(0, 0, 0), 400, 500);

        EqualQa equalQa = new EqualQa();
        TransientOperation<Boolean, Boolean> op = equalQa.evaluate(left, right);
    }

    @Test(expected = PreprocessingException.class)
    public void evaluateTest_differentHeight() {
        BufferedImage left = BufferedImageHelper.createSolidImage(new Color(0, 0, 0), 500, 400);
        BufferedImage right = BufferedImageHelper.createSolidImage(new Color(0, 0, 0), 500, 500);

        EqualQa equalQa = new EqualQa();
        TransientOperation<Boolean, Boolean> op = equalQa.evaluate(left, right);
    }

    @Test
    public void evaluateTest_equal_solid000000() {
        BufferedImage left = BufferedImageHelper.createSolidImage(new Color(0, 0, 0));
        BufferedImage right = BufferedImageHelper.createSolidImage(new Color(0, 0, 0));

        EqualQa equalQa = new EqualQa();
        TransientOperation<Boolean, Boolean> op = equalQa.evaluate(left, right);
        Assert.assertEquals(true, op.getAggregatedResult());
        Assert.assertEquals(true, op.getResult());
    }

    @Test
    public void evaluateTest_equal_solid646464() {
        BufferedImage left = BufferedImageHelper.createSolidImage(new Color(100, 100, 100));
        BufferedImage right = BufferedImageHelper.createSolidImage(new Color(100, 100, 100));

        EqualQa equalQa = new EqualQa();
        TransientOperation<Boolean, Boolean> op = equalQa.evaluate(left, right);
        Assert.assertEquals(true, op.getAggregatedResult());
        Assert.assertEquals(true, op.getResult());
    }

    @Test
    public void evaluateTest_equal_solid9924cd() {
        BufferedImage left = BufferedImageHelper.createSolidImage(new Color(153, 36, 205));
        BufferedImage right = BufferedImageHelper.createSolidImage(new Color(153, 36, 205));

        EqualQa equalQa = new EqualQa();
        TransientOperation<Boolean, Boolean> op = equalQa.evaluate(left, right);
        Assert.assertEquals(true, op.getAggregatedResult());
        Assert.assertEquals(true, op.getResult());
    }

    @Test
    public void evaluateTest_solid000000_solid000164() {
        BufferedImage left = BufferedImageHelper.createSolidImage(new Color(0, 0, 100));
        BufferedImage right = BufferedImageHelper.createSolidImage(new Color(0, 1, 100));

        EqualQa equalQa = new EqualQa();
        TransientOperation<Boolean, Boolean> op = equalQa.evaluate(left, right);
        Assert.assertEquals(false, op.getAggregatedResult());
        Assert.assertEquals(false, op.getResult());
    }

    @Test
    public void evaluateTest_half000000_111111_half000000_111111() {
        BufferedImage left = BufferedImageHelper.createSolidHalfImage(new Color(0, 0, 0), new Color(255, 255, 255));
        BufferedImage right = BufferedImageHelper.createSolidHalfImage(new Color(0, 0, 0), new Color(255, 255, 255));

        EqualQa equalQa = new EqualQa();
        TransientOperation<Boolean, Boolean> op = equalQa.evaluate(left, right);
        Assert.assertEquals(true, op.getAggregatedResult());
        Assert.assertEquals(true, op.getResult());
    }

    @Test
    public void evaluateTest_half000000_000000_half000000_111111() {
        BufferedImage left = BufferedImageHelper.createSolidHalfImage(new Color(0, 0, 0), new Color(0, 0, 0));
        BufferedImage right = BufferedImageHelper.createSolidHalfImage(new Color(0, 0, 0), new Color(255, 255, 255));

        EqualQa equalQa = new EqualQa();
        TransientOperation<Boolean, Boolean> op = equalQa.evaluate(left, right);
        Assert.assertEquals(false, op.getAggregatedResult());
        Assert.assertEquals(false, op.getResult());
    }
}
