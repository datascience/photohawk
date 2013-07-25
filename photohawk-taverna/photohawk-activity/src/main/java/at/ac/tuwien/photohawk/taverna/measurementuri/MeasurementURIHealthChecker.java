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
package at.ac.tuwien.photohawk.taverna.measurementuri;

import java.util.ArrayList;
import java.util.List;

import net.sf.taverna.t2.visit.VisitReport;
import net.sf.taverna.t2.visit.VisitReport.Status;
import net.sf.taverna.t2.workflowmodel.health.HealthCheck;
import net.sf.taverna.t2.workflowmodel.health.HealthChecker;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivity;

/**
 * Base class for health checkers that use MeasurementURIConfigurationBean for
 * configuration
 */
public abstract class MeasurementURIHealthChecker<A extends AsynchronousActivity<C>, C extends MeasurementURIConfigurationBean>
    implements HealthChecker<A> {

    @Override
    public boolean isTimeConsuming() {
        // Return true if the health checker does a network lookup
        // or similar time consuming checks, in which case
        // it would only be performed when using File->Validate workflow
        // or File->Run.
        return false;
    }

    @Override
    public VisitReport visit(A activity, List<Object> ancestry) {
        // Get configuration
        C config = activity.getConfiguration();

        // Create List of subreports
        List<VisitReport> subReports = new ArrayList<VisitReport>();

        // Warn if no measurements selected
        if (config.getMeasurementURIs().size() == 0) {
            subReports.add(new VisitReport(HealthCheck.getInstance(), activity, "No measurements specified",
                HealthCheck.NO_CONFIGURATION, Status.WARNING));
        }

        // The default explanation here will be used if the subreports list is
        // empty
        Status status = VisitReport.getWorstStatus(subReports);
        return new VisitReport(HealthCheck.getInstance(), activity, "MeasurementURI report", HealthCheck.NO_PROBLEM,
            status, subReports);
    }

}
