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

import at.ac.tuwien.photohawk.evaluation.colorconverter.ColorConverter;
import at.ac.tuwien.photohawk.evaluation.colorconverter.StaticColor;
import at.ac.tuwien.photohawk.evaluation.colorconverter.srgb.SRGBStaticColor;
import at.ac.tuwien.photohawk.evaluation.operation.metric.AEMetric;
import at.ac.tuwien.photohawk.evaluation.operation.metric.AEMetric.AEMetricTransientOperation;
import org.junit.Assert;
import org.junit.Test;

import java.awt.Point;

/**
 * Unit tests for AE metric transient operation.
 */
public class AEMetricTransientOperationTest {

    private static final float FLOAT_ASSERT_DELTA = 0.0001f;

    @Test
    public void executeTest_allChannelsEqual() {
        ColorConverter<SRGBStaticColor> img1 = ColorConverterHelper.mockColorConverter(new float[]{0.0f, 0.0f, 0.0f});
        ColorConverter<SRGBStaticColor> img2 = ColorConverterHelper.mockColorConverter(new float[]{0.0f, 0.0f, 0.0f});

        SRGBStaticColor threshold = new SRGBStaticColor(new float[]{0.0f, 0.0f, 0.0f});

        AEMetric metric = new AEMetric(img1, img2, threshold, new Point(0, 0), new Point(1, 1));

        AEMetricTransientOperation op = metric.new AEMetricTransientOperation();
        op.init();
        op.execute(0, 0);
        op.complete();
        Float aggregatedResult = op.getAggregatedResult();
        StaticColor result = op.getResult();

        Assert.assertEquals(new Float(0.0f), aggregatedResult);
        Assert.assertEquals(0.0f, result.getChannelValue(0), FLOAT_ASSERT_DELTA);
        Assert.assertEquals(0.0f, result.getChannelValue(1), FLOAT_ASSERT_DELTA);
        Assert.assertEquals(0.0f, result.getChannelValue(2), FLOAT_ASSERT_DELTA);
    }

    @Test
    public void executeTest_allChannelsOverThreshold() {
        ColorConverter<SRGBStaticColor> img1 = ColorConverterHelper.mockColorConverter(new float[]{0.0f, 0.0f, 0.0f});
        ColorConverter<SRGBStaticColor> img2 = ColorConverterHelper.mockColorConverter(new float[]{1.0f, 1.0f, 1.0f});

        SRGBStaticColor threshold = new SRGBStaticColor(new float[]{0.0f, 0.0f, 0.0f});

        AEMetric metric = new AEMetric(img1, img2, threshold, new Point(0, 0), new Point(1, 1));

        AEMetricTransientOperation op = metric.new AEMetricTransientOperation();
        op.init();
        op.execute(0, 0);
        op.complete();

        Float aggregatedResult = op.getAggregatedResult();
        StaticColor result = op.getResult();

        Assert.assertEquals(new Float(1.0f), aggregatedResult);
        Assert.assertEquals(1.0f, result.getChannelValue(0), FLOAT_ASSERT_DELTA);
        Assert.assertEquals(1.0f, result.getChannelValue(1), FLOAT_ASSERT_DELTA);
        Assert.assertEquals(1.0f, result.getChannelValue(2), FLOAT_ASSERT_DELTA);
    }

    @Test
    public void executeTest_thresholdPerChannel() {
        ColorConverter<SRGBStaticColor> img1 = ColorConverterHelper.mockColorConverter(new float[]{0.0f, 0.0f, 0.0f});
        ColorConverter<SRGBStaticColor> img2 = ColorConverterHelper.mockColorConverter(new float[]{0.5f, 0.5f, 0.5f});

        SRGBStaticColor threshold = new SRGBStaticColor(new float[]{0.0f, 0.5f, 1.0f});
        AEMetric metric = new AEMetric(img1, img2, threshold, new Point(0, 0), new Point(1, 1));

        AEMetricTransientOperation op = metric.new AEMetricTransientOperation();
        op.init();
        op.execute(0, 0);
        op.complete();

        Float aggregatedResult = op.getAggregatedResult();
        StaticColor result = op.getResult();

        Assert.assertEquals(new Float(1.0f), aggregatedResult);
        Assert.assertEquals(1.0f, result.getChannelValue(0), FLOAT_ASSERT_DELTA);
        Assert.assertEquals(0.0f, result.getChannelValue(1), FLOAT_ASSERT_DELTA);
        Assert.assertEquals(0.0f, result.getChannelValue(2), FLOAT_ASSERT_DELTA);
    }

}
