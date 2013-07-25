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
 * This class represents an OperationExecution. It can be seen as state machine,
 * performing, calculating and storing result.
 * 
 * @param <AggregatedResult>
 *            the type of the aggregated result
 * @param <Result>
 *            the type of the result
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 */
public abstract class TransientOperation<AggregatedResult, Result> {

    /**
     * Initializes the operation.
     */
    public abstract void init();

    /**
     * Executes the operation on the specified image block.
     * 
     * @param x
     *            the x coordinates
     * @param y
     *            the y coordinates
     */
    public void execute(int[] x, int[] y) {
        for (int i = 0; i < x.length; i++) {
            execute(x[i], y[i]);
        }
    }

    /**
     * Executes the operation at the specified coordinates.
     * 
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     */
    public void execute(int x, int y) {
        throw new RuntimeException();
    }

    /**
     * Completes the operation.
     */
    public abstract void complete();

    /**
     * Returns the aggregated result of the operation if the operation was
     * completed.
     * 
     * @return the aggregated result
     */
    public abstract AggregatedResult getAggregatedResult();

    /**
     * Returns the result of the operation if the operation was completed.
     * 
     * @return the result
     */
    public abstract Result getResult();

    /**
     * Returns the x granularity of the operation.
     * 
     * @return the x granularity
     */
    public abstract int getGranularityX();

    /**
     * Returns the y granularity of the operation.
     * 
     * @return the y granularity
     */
    public abstract int getGranularityY();
}
