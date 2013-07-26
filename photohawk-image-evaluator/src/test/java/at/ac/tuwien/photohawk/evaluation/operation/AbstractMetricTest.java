package at.ac.tuwien.photohawk.evaluation.operation;

import java.awt.Point;
import java.util.Arrays;

import org.junit.Assert;

import at.ac.tuwien.photohawk.evaluation.colorconverter.StaticColor;

/**
 * Abstract class for metric tests.
 */
public abstract class AbstractMetricTest {

    protected static final float FLOAT_ASSERT_DELTA = 0.0001f;

    protected static final int DEFAULT_IMAGE_SIZE = 5;

    protected static final Point DEFAULT_STARTPOINT = new Point(0, 0);

    protected static final Point DEFAULT_ENDPOINT = new Point(DEFAULT_IMAGE_SIZE, DEFAULT_IMAGE_SIZE);

    /**
     * Asserts the operation results for the provided values using
     * {@link FLOAT_ASSERT_DELTA} as delta.
     * 
     * @param op
     *            operation to check
     * @param expectedAggregate
     *            expected aggregate value
     * @param expectedChannels
     *            expected channel values
     */
    protected void checkOperation(final TransientOperation<Float, StaticColor> op, final float expectedAggregate,
        final float... expectedChannels) {
        StaticColor result = op.getResult();

        Assert.assertEquals(expectedAggregate, op.getAggregatedResult(), FLOAT_ASSERT_DELTA);
        for (int i = 0; i < expectedChannels.length; i++) {
            Assert.assertEquals(expectedChannels[i], result.getChannelValue(i), FLOAT_ASSERT_DELTA);
        }
    }

    /**
     * Asserts the operation results for the provided values using
     * {@link FLOAT_ASSERT_DELTA} as delta.
     * 
     * @param op
     *            operation to check
     * @param expectedValue
     *            expected aggregate value
     */
    protected void checkOperation(final TransientOperation<Float, StaticColor> op, final float expectedValue) {
        float[] expectedChannels = new float[op.getResult().getNumberOfChannels()];
        Arrays.fill(expectedChannels, expectedValue);
        checkOperation(op, expectedValue, expectedChannels);
    }

}