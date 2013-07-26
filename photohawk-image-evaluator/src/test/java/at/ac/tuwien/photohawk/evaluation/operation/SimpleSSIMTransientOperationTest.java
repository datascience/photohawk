package at.ac.tuwien.photohawk.evaluation.operation;

import java.awt.Point;

import org.junit.Assert;
import org.junit.Test;

import at.ac.tuwien.photohawk.evaluation.colorconverter.ColorConverter;
import at.ac.tuwien.photohawk.evaluation.colorconverter.StaticColor;
import at.ac.tuwien.photohawk.evaluation.colorconverter.srgb.SRGBStaticColor;
import at.ac.tuwien.photohawk.evaluation.operation.metric.SimpleSSIMMetric;
import at.ac.tuwien.photohawk.evaluation.operation.metric.SimpleSSIMMetric.SSIMTransientOperation;


public class SimpleSSIMTransientOperationTest {

    private static final float FLOAT_ASSERT_DELTA = 0.0001f;

    @Test
    public void executeTest_1x1_allChannelsZero() {
        ColorConverter<SRGBStaticColor> img1 = ColorConverterHelper.mockColorConverter(new float[] {0.0f, 0.0f, 0.0f});
        ColorConverter<SRGBStaticColor> img2 = ColorConverterHelper.mockColorConverter(new float[] {0.0f, 0.0f, 0.0f});

        SRGBStaticColor threshold = new SRGBStaticColor(new float[] {0.0f, 0.0f, 0.0f});

        SimpleSSIMMetric metric = new SimpleSSIMMetric(img1, img2, new Point(0, 0), new Point(1, 1), 1);

        SSIMTransientOperation op = metric.new SSIMTransientOperation();
        op.init();
        op.execute(new int[] {0}, new int[] {0});
        op.complete();
        Float aggregatedResult = op.getAggregatedResult();
        StaticColor result = op.getResult();

        Assert.assertEquals(new Float(1.0f), aggregatedResult);
        Assert.assertEquals(1.0f, result.getChannelValue(0), FLOAT_ASSERT_DELTA);
        Assert.assertEquals(1.0f, result.getChannelValue(1), FLOAT_ASSERT_DELTA);
        Assert.assertEquals(1.0f, result.getChannelValue(2), FLOAT_ASSERT_DELTA);
    }
}
