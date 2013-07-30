package at.ac.tuwien.photohawk.evaluation.operation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Point;

import org.junit.Assert;
import org.junit.Test;

import at.ac.tuwien.photohawk.evaluation.colorconverter.ColorConverter;
import at.ac.tuwien.photohawk.evaluation.colorconverter.StaticColor;
import at.ac.tuwien.photohawk.evaluation.colorconverter.srgb.SRGBStaticColor;
import at.ac.tuwien.photohawk.evaluation.operation.metric.AEMetric;
import at.ac.tuwien.photohawk.evaluation.operation.metric.AEMetric.AEMetricTransientOperation;


public class AEMetricTransientOperationTest {

    private static final float FLOAT_ASSERT_DELTA = 0.0001f;

    @Test
    public void executeTest_allChannelsEqual() {
        ColorConverter<SRGBStaticColor> img1 = ColorConverterHelper.mockColorConverter(new float[] {0.0f, 0.0f, 0.0f});
        ColorConverter<SRGBStaticColor> img2 = ColorConverterHelper.mockColorConverter(new float[] {0.0f, 0.0f, 0.0f});

        SRGBStaticColor threshold = new SRGBStaticColor(new float[] {0.0f, 0.0f, 0.0f});

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
        ColorConverter<SRGBStaticColor> img1 = ColorConverterHelper.mockColorConverter(new float[] {0.0f, 0.0f, 0.0f});
        ColorConverter<SRGBStaticColor> img2 = ColorConverterHelper.mockColorConverter(new float[] {1.0f, 1.0f, 1.0f});

        SRGBStaticColor threshold = new SRGBStaticColor(new float[] {0.0f, 0.0f, 0.0f});

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
        ColorConverter<SRGBStaticColor> img1 = ColorConverterHelper.mockColorConverter(new float[] {0.0f, 0.0f, 0.0f});
        ColorConverter<SRGBStaticColor> img2 = ColorConverterHelper.mockColorConverter(new float[] {0.5f, 0.5f, 0.5f});

        SRGBStaticColor threshold = new SRGBStaticColor(new float[] {0.0f, 0.5f, 1.0f});
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