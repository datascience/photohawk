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

import java.util.List;

import net.sf.taverna.t2.visit.VisitReport;
import at.ac.tuwien.photohawk.taverna.measurementuri.MeasurementURIHealthChecker;

/**
 * Health checker for Java FITS evaluator
 */
public class JavaFITSEvaluatorActivityHealthChecker extends
    MeasurementURIHealthChecker<JavaFITSEvaluatorActivity, JavaFITSEvaluatorActivityConfigurationBean> {

    @Override
    public boolean canVisit(Object o) {
        // Return True if we can visit the object. We could do
        // deeper (but not time consuming) checks here, for instance
        // if the health checker only deals with RAWvernaActivity where
        // a certain configuration option is enabled.
        return o instanceof JavaFITSEvaluatorActivity;
    }

    @Override
    public VisitReport visit(JavaFITSEvaluatorActivity activity, List<Object> ancestry) {
        VisitReport report = super.visit(activity, ancestry);
        report.setMessage("JavaImageEvaluator report");
        return report;
    }

}
