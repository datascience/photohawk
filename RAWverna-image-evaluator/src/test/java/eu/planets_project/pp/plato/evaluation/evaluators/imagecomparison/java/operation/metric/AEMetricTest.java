package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.operation.metric;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Point;

import org.junit.Assert;
import org.junit.Test;

import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.ColorConverter;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.StaticColor;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.srgb.SRGBStaticColor;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.operation.TransientOperation;

public class AEMetricTest {
    private static final float FLOAT_ASSERT_DELTA = 0.0001f;

    private static final int[][] ZERO_PATTERN = new int[][] { {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}};
    private static final int[][] ONE_PATTERN = new int[][] { {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}};
    private static final int[][] DIAGONAL_ONE_PATTERN = new int[][] { {1, 0, 0, 0, 0}, {0, 1, 0, 0, 0},
        {0, 0, 1, 0, 0}, {0, 0, 0, 1, 0}, {0, 0, 0, 0, 1}};

    @Test
    public void executeTest_equal_zeroPattern() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(), ZERO_PATTERN);
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(), ZERO_PATTERN);

        AEMetric metric = new AEMetric(img1, img2, new Point(0, 0), new Point(3, 3));

        TransientOperation<Float, StaticColor> op = metric.execute();
        Float aggregatedResult = op.getAggregatedResult();
        StaticColor result = op.getResult();

        Assert.assertEquals(new Float(0.0f), aggregatedResult);
        Assert.assertEquals(0.0f, result.getChannelValue(0), FLOAT_ASSERT_DELTA);
        Assert.assertEquals(0.0f, result.getChannelValue(1), FLOAT_ASSERT_DELTA);
        Assert.assertEquals(0.0f, result.getChannelValue(2), FLOAT_ASSERT_DELTA);
    }

    @Test
    public void executeTest_equal_onePattern() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(), ONE_PATTERN);
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(), ONE_PATTERN);

        AEMetric metric = new AEMetric(img1, img2, new Point(0, 0), new Point(3, 3));

        TransientOperation<Float, StaticColor> op = metric.execute();
        Float aggregatedResult = op.getAggregatedResult();
        StaticColor result = op.getResult();

        Assert.assertEquals(new Float(0.0f), aggregatedResult);
        Assert.assertEquals(0.0f, result.getChannelValue(0), FLOAT_ASSERT_DELTA);
        Assert.assertEquals(0.0f, result.getChannelValue(1), FLOAT_ASSERT_DELTA);
        Assert.assertEquals(0.0f, result.getChannelValue(2), FLOAT_ASSERT_DELTA);
    }

    @Test
    public void executeTest_notEqual_zeroPattern_onePattern() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(), ZERO_PATTERN);
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(), ONE_PATTERN);

        AEMetric metric1 = new AEMetric(img1, img2, new Point(0, 0), new Point(3, 3));

        TransientOperation<Float, StaticColor> op1 = metric1.execute();
        Float aggregatedResult1 = op1.getAggregatedResult();
        StaticColor result1 = op1.getResult();

        Assert.assertEquals(new Float(1.0f), aggregatedResult1);
        Assert.assertEquals(1.0f, result1.getChannelValue(0), FLOAT_ASSERT_DELTA);
        Assert.assertEquals(1.0f, result1.getChannelValue(1), FLOAT_ASSERT_DELTA);
        Assert.assertEquals(1.0f, result1.getChannelValue(2), FLOAT_ASSERT_DELTA);

        AEMetric metric2 = new AEMetric(img2, img1, new Point(0, 0), new Point(3, 3));

        TransientOperation<Float, StaticColor> op2 = metric2.execute();
        Float aggregatedResult2 = op2.getAggregatedResult();
        StaticColor result2 = op2.getResult();

        Assert.assertEquals(new Float(1.0f), aggregatedResult2);
        Assert.assertEquals(1.0f, result2.getChannelValue(0), FLOAT_ASSERT_DELTA);
        Assert.assertEquals(1.0f, result2.getChannelValue(1), FLOAT_ASSERT_DELTA);
        Assert.assertEquals(1.0f, result2.getChannelValue(2), FLOAT_ASSERT_DELTA);
    }

    @Test
    public void executeTest_notEqual_zeroPattern_diagonalOnePattern() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(), ZERO_PATTERN);
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(), DIAGONAL_ONE_PATTERN);

        AEMetric metric1 = new AEMetric(img1, img2, new Point(0, 0), new Point(5, 5));

        TransientOperation<Float, StaticColor> op1 = metric1.execute();
        Float aggregatedResult1 = op1.getAggregatedResult();
        StaticColor result1 = op1.getResult();

        Assert.assertEquals(new Float(0.2f), aggregatedResult1);
        Assert.assertEquals(0.2f, result1.getChannelValue(0), FLOAT_ASSERT_DELTA);
        Assert.assertEquals(0.2f, result1.getChannelValue(1), FLOAT_ASSERT_DELTA);
        Assert.assertEquals(0.2f, result1.getChannelValue(2), FLOAT_ASSERT_DELTA);

        AEMetric metric2 = new AEMetric(img2, img1, new Point(0, 0), new Point(5, 5));

        TransientOperation<Float, StaticColor> op2 = metric2.execute();
        Float aggregatedResult2 = op2.getAggregatedResult();
        StaticColor result2 = op2.getResult();

        Assert.assertEquals(new Float(0.2f), aggregatedResult2);
        Assert.assertEquals(0.2f, result2.getChannelValue(0), FLOAT_ASSERT_DELTA);
        Assert.assertEquals(0.2f, result2.getChannelValue(1), FLOAT_ASSERT_DELTA);
        Assert.assertEquals(0.2f, result2.getChannelValue(2), FLOAT_ASSERT_DELTA);
    }

    /**
     * Mocks an (incomplete) ColorConverter.
     * 
     * @param colors
     *            the colors to use
     * @param pattern
     *            pattern of colors
     * @return the mocked color converter
     */
    private ColorConverter<SRGBStaticColor> mockColorConverter(SRGBStaticColor[] colors, int[][] pattern) {
        @SuppressWarnings("unchecked")
        ColorConverter<SRGBStaticColor> img = mock(ColorConverter.class);
        when(img.getNumberOfChannels()).thenReturn(3);
        when(img.getNullColor()).thenReturn(new SRGBStaticColor(0, 0, 0));

        for (int i = 0; i < pattern.length; i++) {
            for (int j = 0; j < pattern[i].length; j++) {
                when(img.getColorChannels(i, j)).thenReturn(colors[pattern[i][j]]);
            }
        }
        return img;
    }

    /**
     * Returns a set of predefined colors.
     * 
     * @return an array of colors
     */
    private SRGBStaticColor[] getColors() {
        SRGBStaticColor[] colors = new SRGBStaticColor[3];

        colors[0] = new SRGBStaticColor(0, 0, 0);
        colors[1] = new SRGBStaticColor(1.0f, 1.0f, 1.0f);
        colors[2] = new SRGBStaticColor(0.0f, 0.5f, 1.0f);

        return colors;
    }
}
