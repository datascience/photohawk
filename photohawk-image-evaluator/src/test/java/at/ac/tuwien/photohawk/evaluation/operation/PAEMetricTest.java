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
import at.ac.tuwien.photohawk.evaluation.operation.metric.PAEMetric;

public class PAEMetricTest extends AbstractMetricTest {

    @Test
    public void executeTest_equal_zeroPattern() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 0));
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 0));

        PAEMetric metric = new PAEMetric(img1, img2, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op = metric.execute();

        checkOperation(op, 0.0f);
    }

    @Test
    public void executeTest_equal_onePattern() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 1));
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 1));

        PAEMetric metric = new PAEMetric(img1, img2, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op = metric.execute();

        checkOperation(op, 0.0f);
    }

    @Test
    public void executeTest_notEqual_zeroPattern_onePattern() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 0));
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 1));

        PAEMetric metric1 = new PAEMetric(img1, img2, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op1 = metric1.execute();

        checkOperation(op1, 1.0f);

        PAEMetric metric2 = new PAEMetric(img2, img1, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op2 = metric2.execute();

        checkOperation(op2, 1.0f);
    }

    @Test
    public void executeTest_notEqual_zeroPattern_diagonalOnePattern() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 0));
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(), DIAGONAL_ONE_PATTERN1);

        PAEMetric metric1 = new PAEMetric(img1, img2, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op1 = metric1.execute();

        checkOperation(op1, 1.0f);

        PAEMetric metric2 = new PAEMetric(img2, img1, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op2 = metric2.execute();

        checkOperation(op2, 1.0f);
    }

    @Test
    public void executeTest_notEqual_zeroPattern_top2OnePattern() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 0));
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(), TOP2_ONE_PATTERN);

        PAEMetric metric1 = new PAEMetric(img1, img2, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op1 = metric1.execute();

        checkOperation(op1, 1.0f);

        PAEMetric metric2 = new PAEMetric(img2, img1, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op2 = metric2.execute();

        checkOperation(op2, 1.0f);
    }

    @Test
    public void executeTest_notEqual_diagonalOnePattern1_diagonalOnePattern2() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(), DIAGONAL_ONE_PATTERN1);
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(), DIAGONAL_ONE_PATTERN2);

        PAEMetric metric1 = new PAEMetric(img1, img2, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op1 = metric1.execute();

        checkOperation(op1, 1.0f);

        PAEMetric metric2 = new PAEMetric(img2, img1, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op2 = metric2.execute();

        checkOperation(op2, 1.0f);
    }

    @Test
    public void executeTest_notEqual_zeroPattern_twoPattern() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 0));
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 2));

        PAEMetric metric1 = new PAEMetric(img1, img2, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op1 = metric1.execute();

        checkOperation(op1, 1.0f, 0.0f, 0.5f, 1.0f);

        PAEMetric metric2 = new PAEMetric(img2, img1, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op2 = metric2.execute();

        checkOperation(op2, 1.0f, 0.0f, 0.5f, 1.0f);
    }

    @Test
    public void executeTest_notEqual_fourPattern_fivePattern() {
        ColorConverter<SRGBStaticColor> img1 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 4));
        ColorConverter<SRGBStaticColor> img2 = mockColorConverter(getColors(), getUniformPattern(DEFAULT_IMAGE_SIZE, 5));

        PAEMetric metric1 = new PAEMetric(img1, img2, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op1 = metric1.execute();

        checkOperation(op1, 0.8f, 0.8f, 0.0f, 0.8f);

        PAEMetric metric2 = new PAEMetric(img2, img1, DEFAULT_STARTPOINT, DEFAULT_ENDPOINT);
        TransientOperation<Float, StaticColor> op2 = metric2.execute();

        checkOperation(op2, 0.8f, 0.8f, 0.0f, 0.8f);
    }
}
