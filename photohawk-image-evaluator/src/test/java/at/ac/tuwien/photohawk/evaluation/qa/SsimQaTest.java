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
import at.ac.tuwien.photohawk.evaluation.colorconverter.StaticColor;
import at.ac.tuwien.photohawk.evaluation.operation.TransientOperation;
import org.junit.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Unit tests for SSIM QE.
 */
public class SsimQaTest extends AbstractOperationTest {

    @Test
    public void evaluateTest_differentSize() {
        BufferedImage left = BufferedImageHelper.createSolidImage(new Color(0, 0, 0), 500, 400);
        BufferedImage right = BufferedImageHelper.createSolidImage(new Color(0, 0, 0), 400, 500);

        SsimQa ssimQa = new SsimQa();
        TransientOperation<Float, StaticColor> op = ssimQa.evaluate(left, right);
    }

    @Test
    public void evaluateTest_equal_solid000000() {
        BufferedImage left = BufferedImageHelper.createSolidImage(new Color(0, 0, 0));
        BufferedImage right = BufferedImageHelper.createSolidImage(new Color(0, 0, 0));

        SsimQa ssimQa = new SsimQa();
        TransientOperation<Float, StaticColor> op = ssimQa.evaluate(left, right);
        checkOperationEqual(op, 1.0f);
    }

    @Test
    public void evaluateTest_equal_solid646464() {
        BufferedImage left = BufferedImageHelper.createSolidImage(new Color(100, 100, 100));
        BufferedImage right = BufferedImageHelper.createSolidImage(new Color(100, 100, 100));

        SsimQa ssimQa = new SsimQa();
        TransientOperation<Float, StaticColor> op = ssimQa.evaluate(left, right);
        checkOperationEqual(op, 1.0f);
    }

    @Test
    public void evaluateTest_equal_solid9924cd() {
        BufferedImage left = BufferedImageHelper.createSolidImage(new Color(153, 36, 205));
        BufferedImage right = BufferedImageHelper.createSolidImage(new Color(153, 36, 205));

        SsimQa ssimQa = new SsimQa();
        TransientOperation<Float, StaticColor> op = ssimQa.evaluate(left, right);
        checkOperationEqual(op, 1.0f);
    }

    @Test
    public void evaluateTest_half000000_111111_half000000_111111() {
        BufferedImage left = BufferedImageHelper.createSolidHalfImage(new Color(0, 0, 0), new Color(255, 255, 255));
        BufferedImage right = BufferedImageHelper.createSolidHalfImage(new Color(0, 0, 0), new Color(255, 255, 255));

        SsimQa ssimQa = new SsimQa();
        TransientOperation<Float, StaticColor> op = ssimQa.evaluate(left, right);
        checkOperationEqual(op, 1.0f);
    }

    @Test
    public void evaluateTest_size_equal() {
        BufferedImage left = BufferedImageHelper.createSolidImage(new Color(0, 0, 0), 100, 100);
        BufferedImage right = BufferedImageHelper.createSolidImage(new Color(0, 0, 0), 50, 50);

        SsimQa ssimQa = new SsimQa();
        TransientOperation<Float, StaticColor> op = ssimQa.evaluate(left, right);
        checkOperationEqual(op, 1.0f);
    }

    /*
     * Left image is resized to size of right image, making both the same.
     */
    @Test
    public void evaluateTest_size_half000000_111111_half000000_111111() {
        BufferedImage left = BufferedImageHelper.createSolidHalfImage(new Color(0, 0, 0), new Color(255, 255, 255), 100,
                                                                      100);
        BufferedImage right = BufferedImageHelper.createSolidHalfImage(new Color(0, 0, 0), new Color(255, 255, 255), 50,
                                                                       50);

        SsimQa ssimQa = new SsimQa();
        TransientOperation<Float, StaticColor> op = ssimQa.evaluate(left, right);
        checkOperationEqual(op, 1.0f);
    }
}
