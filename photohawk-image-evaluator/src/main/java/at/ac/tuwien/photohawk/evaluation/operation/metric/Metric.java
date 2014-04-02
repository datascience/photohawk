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
 * This class represents a generic Metric.
 *
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 */
public abstract class Metric implements Operation<Float, StaticColor> {

    protected ColorConverter<?> img1;

    protected ColorConverter<?> img2;

    protected StaticColor threshold;

    protected Point end;
    protected Point start;

    /**
     * Creates a new metric with the specified parameters. The threshold is set
     * to the {@link ColorConverter#getNullColor() null color} of img1.
     *
     * @param img1  color converter of image 1
     * @param img2  color converter of image 2
     * @param start start of comparison
     * @param end   end of comparison
     */
    public Metric(ColorConverter<?> img1, ColorConverter<?> img2, Point start, Point end) {
        this(img1, img2, img1.getNullColor(), start, end);
    }

    /**
     * Creates a new metric with the specified parameters.
     * <p/>
     * Precondition: Images have equal size.
     *
     * @param img1      color converter of image 1
     * @param img2      color converter of image 2
     * @param threshold threshold of comparison
     * @param start     start of comparison
     * @param end       end of comparison
     */
    public Metric(ColorConverter<?> img1, ColorConverter<?> img2, StaticColor threshold, Point start, Point end) {
        this.img1 = img1;
        this.img2 = img2;
        this.threshold = threshold;
        this.start = start;
        this.end = end;
    }

    @Override
    public TransientOperation<Float, StaticColor> execute() {
        TransientOperation<Float, StaticColor> op = prepare();
        op.init();
        for (int x = start.x; x < end.x; x++) {
            for (int y = start.y; y < end.y; y++) {
                op.execute(x, y);
            }
        }
        op.complete();
        return op;
    }

    /**
     * Generic transient operation.
     */
    public abstract class MetricTransientOperation extends TransientOperation<Float, StaticColor> {
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
