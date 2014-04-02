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
import at.ac.tuwien.photohawk.evaluation.colorconverter.hsb.HSBColorConverter;
import at.ac.tuwien.photohawk.evaluation.operation.TransientOperation;

import java.awt.Point;

/**
 * This class implements a simple Peak Absolute Error Metric.
 *
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 */
public class PAEMetric extends Metric {

    /**
     * Creates a new PAEMetric with the provided parameters. The threshold is
     * set to the {@link ColorConverter#getNullColor() null color} of img1.
     *
     * @param img1  color converter of image 1
     * @param img2  color converter of image 2
     * @param start start of comparison
     * @param end   end of comparison
     */
    public PAEMetric(ColorConverter<?> img1, ColorConverter<?> img2, Point start, Point end) {
        super(img1, img2, start, end);
    }

    @Override
    public TransientOperation<Float, StaticColor> prepare() {
        return new PAEMetricTransientOperation();
    }

    /**
     * Transient operation that implements a simple Peak Absolute Error Metric.
     */
    public class PAEMetricTransientOperation extends MetricTransientOperation {

        protected float realresult;

        protected float[] channelResult;
        protected StaticColor realChannelResult;

        @Override
        public void init() {
            realresult = 0;
            realChannelResult = img1.getNullColor();
            channelResult = new float[img1.getNumberOfChannels()];
        }

        @Override
        public void execute(int x, int y) {
            StaticColor val1 = img1.getColorChannels(x, y);
            StaticColor val2 = img2.getColorChannels(x, y);

            for (int i = 0; i < val1.getNumberOfChannels(); i++) {
                // System.out.println(val1[i] + " " + val2[i]);
                double value = Math.abs(val1.getChannelValue(i) - val2.getChannelValue(i));
                if (img1 instanceof HSBColorConverter && i == 2) {
                    value = HSBColorConverter.normalizeHueDifference(value);
                }
                if (value > channelResult[i]) {
                    channelResult[i] = (float) value;
                    if (value > realresult) {
                        realresult = (float) value;
                    }
                }
            }
        }

        @Override
        public void complete() {
            realChannelResult.setChannelValues(channelResult);
        }

        @Override
        public StaticColor getResult() {
            return realChannelResult;
        }

        @Override
        public Float getAggregatedResult() {
            return realresult;
        }

    }
}
