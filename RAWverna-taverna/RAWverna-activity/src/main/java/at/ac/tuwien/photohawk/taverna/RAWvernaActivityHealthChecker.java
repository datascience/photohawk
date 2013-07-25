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

import java.util.ArrayList;
import java.util.List;

import net.sf.taverna.t2.visit.VisitReport;
import net.sf.taverna.t2.visit.VisitReport.Status;
import net.sf.taverna.t2.workflowmodel.health.HealthCheck;
import net.sf.taverna.t2.workflowmodel.health.HealthChecker;

/**
 * RAWverna health checker
 * 
 */
public class RAWvernaActivityHealthChecker implements HealthChecker<RAWvernaActivity> {

    public boolean canVisit(Object o) {
        // Return True if we can visit the object. We could do
        // deeper (but not time consuming) checks here, for instance
        // if the health checker only deals with RAWvernaActivity where
        // a certain configuration option is enabled.
        return o instanceof RAWvernaActivity;
    }

    public boolean isTimeConsuming() {
        // Return true if the health checker does a network lookup
        // or similar time consuming checks, in which case
        // it would only be performed when using File->Validate workflow
        // or File->Run.
        return false;
    }

    public VisitReport visit(RAWvernaActivity activity, List<Object> ancestry) {
        RAWvernaActivityConfigurationBean config = activity.getConfiguration();

        // We'll build a list of subreports
        List<VisitReport> subReports = new ArrayList<VisitReport>();

        if (!config.getExampleUri().isAbsolute()) {
            // Report Severe problems we know won't work
            VisitReport report = new VisitReport(HealthCheck.getInstance(), activity, "Example URI must be absolute",
                HealthCheck.INVALID_URL, Status.SEVERE);
            subReports.add(report);
        }

        if (config.getExampleString().equals("")) {
            // Warning on possible problems
            subReports.add(new VisitReport(HealthCheck.getInstance(), activity, "Example string empty",
                HealthCheck.NO_CONFIGURATION, Status.WARNING));
        }

        // The default explanation here will be used if the subreports list is
        // empty
        return new VisitReport(HealthCheck.getInstance(), activity, "RAWverna service OK", HealthCheck.NO_PROBLEM,
            subReports);
    }

}
