/*******************************************************************************
 * Copyright 2013 Vienna University of Technology
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
package at.ac.tuwien.photohawk.taverna;

import at.ac.tuwien.photohawk.evaluation.operation.metric.SimpleSSIMMetric;

import java.io.Serializable;

/**
 * Activity configuration bean for SimpleSSIMActivity.
 */
public class SimpleSSIMActivityConfigurationBean implements Serializable {
    private static final long serialVersionUID = -7424460713469977670L;

    private int targetSize = SimpleSSIMMetric.DEFAULT_TARGET_SIZE;

    private boolean doThreaded = false;

    private int threadPoolSize = 4;

    public int getTargetSize() {
        return targetSize;
    }

    public void setTargetSize(int targetSize) {
        this.targetSize = targetSize;
    }

    public boolean isDoThreaded() {
        return doThreaded;
    }

    public void setDoThreaded(boolean doThreaded) {
        this.doThreaded = doThreaded;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }
}
