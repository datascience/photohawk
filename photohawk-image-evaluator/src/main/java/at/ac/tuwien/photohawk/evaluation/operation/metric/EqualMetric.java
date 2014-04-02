/*******************************************************************************
 * Copyright 2010-2013 Vienna University of Technology
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
package at.ac.tuwien.photohawk.evaluation.operation.metric;

import at.ac.tuwien.photohawk.evaluation.colorconverter.ColorConverter;
import at.ac.tuwien.photohawk.evaluation.colorconverter.StaticColor;
import at.ac.tuwien.photohawk.evaluation.operation.Operation;
import at.ac.tuwien.photohawk.evaluation.operation.TransientOperation;

import java.awt.Point;


/**
 * This class implements a simple Equals Metric. It may be more efficient than
 * checking AEMetric == 0.
 *
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 */
public class EqualMetric implements Operation<Boolean, Boolean> {

    private ColorConverter<?> img1;

    private ColorConverter<?> img2;

    private StaticColor threshold;

    private Point start;
    private Point end;

    /**
     * Creates a new EqualMetric with the provided parameters.
     *
     * @param img1      color converter of image 1
     * @param img2      color converter of image 2
     * @param threshold threshold of comparison
     * @param start     start of comparison
     * @param end       end of comparison
     */
    public EqualMetric(ColorConverter<?> img1, ColorConverter<?> img2, StaticColor threshold, Point start, Point end) {
        this.img1 = img1;
        this.img2 = img2;
        this.threshold = threshold;
        this.start = start;
        this.end = end;
    }

    /**
     * Creates a new EqualMetric with the provided parameters. The threshold is
     * set to the {@link ColorConverter#getNullColor() null color} of img1.
     *
     * @param img1  color converter of image 1
     * @param img2  color converter of image 2
     * @param start start of comparison
     * @param end   end of comparison
     */
    public EqualMetric(ColorConverter<?> img1, ColorConverter<?> img2, Point start, Point end) {
        this(img1, img2, img1.getNullColor(), start, end);
    }

    @Override
    public TransientOperation<Boolean, Boolean> prepare() {
        return new EqualMetricTransientOperation();
    }

    @Override
    public TransientOperation<Boolean, Boolean> execute() {
        TransientOperation<Boolean, Boolean> op = prepare();
        op.init();
        for (int x = start.x; x < end.x; x++) {
            for (int y = start.y; y < end.y; y++) {
                op.execute(x, y);
                if (!op.getResult()) {
                    break;
                }
            }
        }
        op.complete();
        return op;
    }

    /**
     * Transient operation that implements a simple Equal Metric.
     */
    public class EqualMetricTransientOperation extends TransientOperation<Boolean, Boolean> {

        private boolean result;

        @Override
        public void init() {
            result = true;
        }

        @Override
        public void complete() {

        }

        @Override
        public void execute(int[] x, int[] y) {
            for (int i = 0; i < x.length; i++) {
                execute(x[i], y[i]);
                if (!result) {
                    break;
                }
            }
        }

        @Override
        public void execute(int x, int y) {
            StaticColor val1 = img1.getColorChannels(x, y);
            StaticColor val2 = img2.getColorChannels(x, y);

            for (int i = 0; i < val1.getNumberOfChannels(); i++) {
                if (Math.abs(val1.getChannelValue(i) - val2.getChannelValue(i)) > threshold.getChannelValue(i)) {
                    result = false;
                }
            }
        }

        @Override
        public Boolean getAggregatedResult() {
            return result;
        }

        @Override
        public Boolean getResult() {
            return result;
        }

        @Override
        public int getGranularityX() {
            return 1;
        }

        @Override
        public int getGranularityY() {
            return 1;
        }
    }
}
