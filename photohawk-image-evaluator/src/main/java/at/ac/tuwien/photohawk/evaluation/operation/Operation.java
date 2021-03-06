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
package at.ac.tuwien.photohawk.evaluation.operation;

/**
 * This class represents a generic operation.
 *
 * @param <AggregatedResult> type of the aggregated results
 * @param <Result>           type of the result
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 */
public interface Operation<AggregatedResult, Result> {

    /**
     * Prepares a transient operation of this operation and returns it.
     *
     * @return the transient operation
     */
    TransientOperation<AggregatedResult, Result> prepare();

    /**
     * Executes an operation and returns the resulting transient operation.
     *
     * @return the executed transient operation
     */
    TransientOperation<AggregatedResult, Result> execute();
}
