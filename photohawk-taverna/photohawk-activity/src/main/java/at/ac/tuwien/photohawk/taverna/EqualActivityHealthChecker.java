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
 * Health checker for EqualActivity.
 */
public class EqualActivityHealthChecker implements HealthChecker<EqualActivity> {

    @Override
    public boolean isTimeConsuming() {
        return false;
    }

    @Override
    public boolean canVisit(Object o) {
        return o instanceof SimpleSSIMActivity;
    }

    @Override
    public VisitReport visit(EqualActivity activity, List<Object> ancestry) {
        List<VisitReport> subReports = new ArrayList<VisitReport>();
        Status status = VisitReport.getWorstStatus(subReports);
        return new VisitReport(HealthCheck.getInstance(), activity, "Equal report", HealthCheck.NO_PROBLEM, status,
            subReports);
    }
}
