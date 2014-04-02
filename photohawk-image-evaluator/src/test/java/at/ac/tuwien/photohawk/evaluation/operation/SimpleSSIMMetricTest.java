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
package at.ac.tuwien.photohawk.evaluation.operation;

import at.ac.tuwien.photohawk.evaluation.AbstractOperationTest;
import at.ac.tuwien.photohawk.evaluation.colorconverter.ColorConverter;
import at.ac.tuwien.photohawk.evaluation.colorconverter.StaticColor;
import at.ac.tuwien.photohawk.evaluation.colorconverter.srgb.SRGBStaticColor;
import at.ac.tuwien.photohawk.evaluation.operation.metric.SimpleSSIMMetric;
import org.junit.Test;

import java.awt.Point;

import static at.ac.tuwien.photohawk.evaluation.operation.ColorConverterHelper.*;

/**
 * Unit tests for SSIM metric.
 */
public class SimpleSSIMMetricTest extends AbstractOperationTest {

    private static final int DEFAULT_IMAGE_SIZE = 11;

    private static final Point DEFAULT_ENDPOINT = new Point(DEFAULT_IMAGE_SIZE, DEFAULT_IMAGE_SIZE);

    private static final int LARGE_IMAGE_SIZE = 22;

    private static final Point LARGE_ENDPOINT = new Point(LARGE_IMAGE_SIZE, LARGE_IMAGE_SIZE);

    /**
     * Tests if the result for the same pattern is 1.0f.
     */
    @Test
    public void executeTest_equal_zeroPattern() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(),
                                                                  getUniformPattern(DEFAULT_IMAGE_SIZE, 0));
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(),
                                                                  getUniformPattern(DEFAULT_IMAGE_SIZE, 0));

        SimpleSSIMMetric metric = new SimpleSSIMMetric(img1, img2, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op = metric.execute();

        checkOperationEqual(op, 1.0f);
    }

    /**
     * Tests if the result for the same pattern is 1.0f.
     */
    @Test
    public void executeTest_equal_onePattern() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(),
                                                                  getUniformPattern(DEFAULT_IMAGE_SIZE, 1));
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(),
                                                                  getUniformPattern(DEFAULT_IMAGE_SIZE, 1));

        SimpleSSIMMetric metric = new SimpleSSIMMetric(img1, img2, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op = metric.execute();

        checkOperationEqual(op, 1.0f);
    }

    /**
     * Tests if the result for two different pattern is not 1.0f.
     */
    @Test
    public void executeTest_notEqual_zeroPattern_onePattern_notOne() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(),
                                                                  getUniformPattern(DEFAULT_IMAGE_SIZE, 0));
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(),
                                                                  getUniformPattern(DEFAULT_IMAGE_SIZE, 1));

        SimpleSSIMMetric metric1 = new SimpleSSIMMetric(img1, img2, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op1 = metric1.execute();

        checkOperationNotEqual(op1, 1.0f);

        SimpleSSIMMetric metric2 = new SimpleSSIMMetric(img2, img1, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op2 = metric2.execute();

        checkOperationNotEqual(op2, 1.0f);
    }

    /**
     * Tests if the metric for the patterns provides the correct result.
     * <p/>
     * Note: The result was checked against ssim.m available at
     * https://ece.uwaterloo.ca/~z70wang/research/ssim/ssim.m
     */
    @Test
    public void executeTest_notEqual_zeroPattern_onePattern() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(),
                                                                  getUniformPattern(DEFAULT_IMAGE_SIZE, 0));
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(),
                                                                  getUniformPattern(DEFAULT_IMAGE_SIZE, 1));

        SimpleSSIMMetric metric1 = new SimpleSSIMMetric(img1, img2, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op1 = metric1.execute();

        checkOperationEqual(op1, 0.00009999f);

        SimpleSSIMMetric metric2 = new SimpleSSIMMetric(img2, img1, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op2 = metric2.execute();

        checkOperationEqual(op2, 0.00009999f);
    }

    /**
     * Tests if the metric for the patterns provides the correct result.
     * <p/>
     * Note: The result was checked against ssim.m available at
     * https://ece.uwaterloo.ca/~z70wang/research/ssim/ssim.m
     */
    @Test
    public void executeTest_notEqual_zeroPattern_sixPattern() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(),
                                                                  getUniformPattern(DEFAULT_IMAGE_SIZE, 0));
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(),
                                                                  getUniformPattern(DEFAULT_IMAGE_SIZE, 6));

        SimpleSSIMMetric metric1 = new SimpleSSIMMetric(img1, img2, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op1 = metric1.execute();

        checkOperationEqual(op1, 0.0024938f);

        SimpleSSIMMetric metric2 = new SimpleSSIMMetric(img2, img1, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op2 = metric2.execute();

        checkOperationEqual(op2, 0.0024938f);
    }

    /**
     * Tests if the metric for the patterns provides the correct result.
     * <p/>
     * Note: The result was checked against ssim.m available at
     * https://ece.uwaterloo.ca/~z70wang/research/ssim/ssim.m
     */
    @Test
    public void executeTest_notEqual_zeroPattern_eightPattern() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(),
                                                                  getUniformPattern(DEFAULT_IMAGE_SIZE, 0));
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(),
                                                                  getUniformPattern(DEFAULT_IMAGE_SIZE, 8));

        SimpleSSIMMetric metric1 = new SimpleSSIMMetric(img1, img2, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op1 = metric1.execute();

        checkOperationEqual(op1, 0.028088f);

        SimpleSSIMMetric metric2 = new SimpleSSIMMetric(img2, img1, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op2 = metric2.execute();

        checkOperationEqual(op2, 0.028088f);
    }

    /**
     * Tests if the metric for the patterns provides the correct result.
     * <p/>
     * Note: The result was checked against ssim.m available at
     * https://ece.uwaterloo.ca/~z70wang/research/ssim/ssim.m
     */
    @Test
    public void executeTest_notEqual_zeroPattern_eightPattern_22x22() {
        final int size = 22;

        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(), getUniformPattern(size, 0));
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(), getUniformPattern(size, 8));

        SimpleSSIMMetric metric1 = new SimpleSSIMMetric(img1, img2, DEFAULT_STARTPOINT, new Point(size, size));
        TransientOperation<Float, StaticColor> op1 = metric1.execute();

        checkOperationEqual(op1, 0.028088f);

        SimpleSSIMMetric metric2 = new SimpleSSIMMetric(img2, img1, DEFAULT_STARTPOINT, new Point(size, size));
        TransientOperation<Float, StaticColor> op2 = metric2.execute();

        checkOperationEqual(op2, 0.028088f);
    }

    /**
     * Tests if the result for the same pattern is 1.0f.
     */
    @Test
    public void executeTest_equal_zeroPattern_22x22_threaded() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(), getUniformPattern(LARGE_IMAGE_SIZE, 0));
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(), getUniformPattern(LARGE_IMAGE_SIZE, 0));

        SimpleSSIMMetric metric = new SimpleSSIMMetric(img1, img2, DEFAULT_STARTPOINT, LARGE_ENDPOINT, 4);
        TransientOperation<Float, StaticColor> op = metric.execute();

        checkOperationEqual(op, 1.0f);
    }

    /**
     * Tests if the result for the same pattern is 1.0f.
     */
    @Test
    public void executeTest_equal_onePattern_22x22_threaded() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(), getUniformPattern(LARGE_IMAGE_SIZE, 1));
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(), getUniformPattern(LARGE_IMAGE_SIZE, 1));

        SimpleSSIMMetric metric = new SimpleSSIMMetric(img1, img2, DEFAULT_STARTPOINT, LARGE_ENDPOINT, 4);
        TransientOperation<Float, StaticColor> op = metric.execute();

        checkOperationEqual(op, 1.0f);
    }

    /**
     * Tests if the metric for the patterns provides the correct result.
     * <p/>
     * Note: The result was checked against ssim.m available at
     * https://ece.uwaterloo.ca/~z70wang/research/ssim/ssim.m
     */
    @Test
    public void executeTest_notEqual_zeroPattern_eightPattern_22x22_threaded() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(), getUniformPattern(LARGE_IMAGE_SIZE, 0));
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(), getUniformPattern(LARGE_IMAGE_SIZE, 8));

        SimpleSSIMMetric metric1 = new SimpleSSIMMetric(img1, img2, DEFAULT_STARTPOINT, LARGE_ENDPOINT, 4);
        TransientOperation<Float, StaticColor> op1 = metric1.execute();

        checkOperationEqual(op1, 0.028088f);

        SimpleSSIMMetric metric2 = new SimpleSSIMMetric(img2, img1, DEFAULT_STARTPOINT, LARGE_ENDPOINT, 4);
        TransientOperation<Float, StaticColor> op2 = metric2.execute();

        checkOperationEqual(op2, 0.028088f);
    }
}
