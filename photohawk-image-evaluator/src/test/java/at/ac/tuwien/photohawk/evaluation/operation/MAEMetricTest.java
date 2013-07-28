package at.ac.tuwien.photohawk.evaluation.operation;

import static at.ac.tuwien.photohawk.evaluation.operation.ColorConverterHelper.DIAGONAL_ONE_PATTERN1;
import static at.ac.tuwien.photohawk.evaluation.operation.ColorConverterHelper.DIAGONAL_ONE_PATTERN2;
import static at.ac.tuwien.photohawk.evaluation.operation.ColorConverterHelper.TOP2_ONE_PATTERN;
import static at.ac.tuwien.photohawk.evaluation.operation.ColorConverterHelper.getColors;
import static at.ac.tuwien.photohawk.evaluation.operation.ColorConverterHelper.getUniformPattern;
import static at.ac.tuwien.photohawk.evaluation.operation.ColorConverterHelper.mockColorConverter;

import org.junit.Test;

import at.ac.tuwien.photohawk.evaluation.colorconverter.ColorConverter;
import at.ac.tuwien.photohawk.evaluation.colorconverter.StaticColor;
import at.ac.tuwien.photohawk.evaluation.colorconverter.srgb.SRGBStaticColor;
import at.ac.tuwien.photohawk.evaluation.operation.metric.MAEMetric;

public class MAEMetricTest extends AbstractMetricTest {

    @Test
    public void executeTest_equal_zeroPattern() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 0));
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 0));

        MAEMetric metric = new MAEMetric(img1, img2, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op = metric.execute();

        checkOperationEqual(op, 0.0f);
    }

    @Test
    public void executeTest_equal_onePattern() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 1));
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 1));

        MAEMetric metric = new MAEMetric(img1, img2, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op = metric.execute();

        checkOperationEqual(op, 0.0f);
    }

    @Test
    public void executeTest_notEqual_zeroPattern_onePattern() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 0));
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 1));

        MAEMetric metric1 = new MAEMetric(img1, img2, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op1 = metric1.execute();

        checkOperationEqual(op1, 1.0f);

        MAEMetric metric2 = new MAEMetric(img2, img1, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op2 = metric2.execute();

        checkOperationEqual(op2, 1.0f);
    }

    @Test
    public void executeTest_notEqual_zeroPattern_diagonalOnePattern() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 0));
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(), DIAGONAL_ONE_PATTERN1);

        MAEMetric metric1 = new MAEMetric(img1, img2, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op1 = metric1.execute();

        checkOperationEqual(op1, 0.2f);

        MAEMetric metric2 = new MAEMetric(img2, img1, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op2 = metric2.execute();

        checkOperationEqual(op2, 0.2f);
    }

    @Test
    public void executeTest_notEqual_zeroPattern_top2OnePattern() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 0));
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(), TOP2_ONE_PATTERN);

        MAEMetric metric1 = new MAEMetric(img1, img2, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);

        TransientOperation<Float, StaticColor> op1 = metric1.execute();

        checkOperationEqual(op1, 0.4f);

        MAEMetric metric2 = new MAEMetric(img2, img1, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op2 = metric2.execute();

        checkOperationEqual(op2, 0.4f);
    }

    @Test
    public void executeTest_notEqual_diagonalOnePattern1_diagonalOnePattern2() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(), DIAGONAL_ONE_PATTERN1);
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(), DIAGONAL_ONE_PATTERN2);

        MAEMetric metric1 = new MAEMetric(img1, img2, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op1 = metric1.execute();

        checkOperationEqual(op1, 0.32f);

        MAEMetric metric2 = new MAEMetric(img2, img1, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op2 = metric2.execute();

        checkOperationEqual(op2, 0.32f);
    }

    @Test
    public void executeTest_notEqual_zeroPattern_twoPattern() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 0));
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 2));

        MAEMetric metric1 = new MAEMetric(img1, img2, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op1 = metric1.execute();

        checkOperationEqual(op1, 0.5f, 0.0f, 0.5f, 1.0f);

        MAEMetric metric2 = new MAEMetric(img2, img1, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op2 = metric2.execute();

        checkOperationEqual(op2, 0.5f, 0.0f, 0.5f, 1.0f);
    }

    @Test
    public void executeTest_notEqual_fourPattern_fivePattern() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 4));
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 5));

        MAEMetric metric1 = new MAEMetric(img1, img2, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op1 = metric1.execute();

        checkOperationEqual(op1, 0.5333333f, 0.8f, 0.0f, 0.8f);

        MAEMetric metric2 = new MAEMetric(img2, img1, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op2 = metric2.execute();

        checkOperationEqual(op2, 0.5333333f, 0.8f, 0.0f, 0.8f);
    }
}
