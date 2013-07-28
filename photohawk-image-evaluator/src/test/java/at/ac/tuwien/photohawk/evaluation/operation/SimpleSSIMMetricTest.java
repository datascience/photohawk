package at.ac.tuwien.photohawk.evaluation.operation;

import static at.ac.tuwien.photohawk.evaluation.operation.ColorConverterHelper.getColors;
import static at.ac.tuwien.photohawk.evaluation.operation.ColorConverterHelper.getUniformPattern;
import static at.ac.tuwien.photohawk.evaluation.operation.ColorConverterHelper.mockColorConverter;

import java.awt.Point;

import org.junit.Test;

import at.ac.tuwien.photohawk.evaluation.colorconverter.ColorConverter;
import at.ac.tuwien.photohawk.evaluation.colorconverter.StaticColor;
import at.ac.tuwien.photohawk.evaluation.colorconverter.srgb.SRGBStaticColor;
import at.ac.tuwien.photohawk.evaluation.operation.metric.SimpleSSIMMetric;

public class SimpleSSIMMetricTest extends AbstractMetricTest {

    private int DEFAULT_IMAGE_SIZE = 11;

    private Point DEFAULT_ENDPOINT = new Point(DEFAULT_IMAGE_SIZE, DEFAULT_IMAGE_SIZE);

    @Test
    public void executeTest_equal_zeroPattern() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 0));
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 0));

        SimpleSSIMMetric metric = new SimpleSSIMMetric(img1, img2, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op = metric.execute();

        checkOperationEqual(op, 1.0f);
    }

    @Test
    public void executeTest_equal_onePattern() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 1));
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 1));

        SimpleSSIMMetric metric = new SimpleSSIMMetric(img1, img2, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op = metric.execute();

        checkOperationEqual(op, 1.0f);
    }

    @Test
    public void executeTest_notEqual_zeroPattern_onePattern() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 0));
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 1));

        SimpleSSIMMetric metric1 = new SimpleSSIMMetric(img1, img2, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op1 = metric1.execute();

        checkOperationNotEqual(op1, 1.0f);

        SimpleSSIMMetric metric2 = new SimpleSSIMMetric(img2, img1, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op2 = metric2.execute();

        checkOperationNotEqual(op2, 1.0f);
    }
}
