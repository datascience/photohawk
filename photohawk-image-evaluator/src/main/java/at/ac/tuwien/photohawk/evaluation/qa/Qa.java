/*******************************************************************************
 * Copyright 2010-2014 Vienna University of Technology
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

package at.ac.tuwien.photohawk.evaluation.qa;

import at.ac.tuwien.photohawk.evaluation.operation.TransientOperation;

import java.awt.image.BufferedImage;

/**
 * Quality assurance evaluation of two images.
 *
 * @param <AggregatedResult> aggregated result type
 * @param <Result>           channel result type
 */
public interface Qa<AggregatedResult, Result> {

    /**
     * Evaluate the provided images.
     *
     * @param left  left image to evalute
     * @param right right image to evaluate
     * @return the evaluation result
     */
    TransientOperation<AggregatedResult, Result> evaluate(BufferedImage left, BufferedImage right);
}
