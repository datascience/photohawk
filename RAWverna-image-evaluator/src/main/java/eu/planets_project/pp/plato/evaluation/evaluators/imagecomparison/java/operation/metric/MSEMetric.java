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
package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.operation.metric;

import java.awt.Point;

import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.ColorConverter;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.StaticColor;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.hsb.HSBColorConverter;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.operation.TransientOperation;

/**
 * This class implements a simple Mean Squared Error Metric.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 * @version 1.0
 */
public class MSEMetric extends Metric {

    public MSEMetric(ColorConverter<?> img1, ColorConverter<?> img2, Point start, Point end) {
        super(img1, img2, start, end);
    }

    public TransientOperation<Float, StaticColor> prepare() {
        return new MSEMetricTransientOperation();
    }

    public class MSEMetricTransientOperation extends MetricTransientOperation {

        private double realresult;

        private double[] channelResult;
        private StaticColor realChannelResult;

        @Override
        public void init() {
            realChannelResult = img1.getNullColor();
            channelResult = new double[img1.getNumberOfChannels()];
        }

        @Override
        public void execute(int x, int y) {
            StaticColor val1 = img1.getColorChannels(x, y);
            StaticColor val2 = img2.getColorChannels(x, y);

            for (int i = 0; i < val1.getNumberOfChannels(); i++) {
                // System.out.println(val1[i] + " " + val2[i]);
                double value = Math.abs(val1.getChannelValue(i) - val2.getChannelValue(i));
                if (img1 instanceof HSBColorConverter && i == 2) {
                    if (value > 0.5) {
                        value = 1 - value;
                    }
                }
                channelResult[i] += Math.pow(value, 2);
            }
        }

        @Override
        public void complete() {
            int size = ((end.x - start.x) * (end.y - start.y));
            for (int i = 0; i < channelResult.length; i++) {
                double value = channelResult[i] / (double) size;
                realChannelResult.setChannelValue(i, (float) value);
                realresult += value;
            }
            realresult /= (double) channelResult.length;
        }

        @Override
        public StaticColor getResult() {
            return realChannelResult;
        }

        @Override
        public Float getAggregatedResult() {
            return (float) realresult;
        }
    }
}
