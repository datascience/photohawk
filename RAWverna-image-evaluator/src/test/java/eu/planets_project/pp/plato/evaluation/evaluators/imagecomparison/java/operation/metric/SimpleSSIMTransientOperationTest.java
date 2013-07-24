package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.operation.metric;

import java.awt.Point;

import org.junit.Assert;
import org.junit.Test;

import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.ColorConverter;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.StaticColor;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.srgb.SRGBStaticColor;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.operation.metric.SimpleSSIMMetric.SSIMTransientOperation;

public class SimpleSSIMTransientOperationTest {

    private static final float FLOAT_ASSERT_DELTA = 0.0001f;

    @Test
    public void executeTest_1x1_allChannelsZero() {
        ColorConverter<SRGBStaticColor> img1 = ColorCoverterHelper.mockColorConverter(new float[] {0.0f, 0.0f, 0.0f});
        ColorConverter<SRGBStaticColor> img2 = ColorCoverterHelper.mockColorConverter(new float[] {0.0f, 0.0f, 0.0f});

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
