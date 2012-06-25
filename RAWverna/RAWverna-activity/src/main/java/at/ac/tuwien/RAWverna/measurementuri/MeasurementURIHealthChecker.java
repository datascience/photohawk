package at.ac.tuwien.RAWverna.measurementuri;

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
			subReports.add(new VisitReport(HealthCheck.getInstance(), activity,
					"No measurements specified", HealthCheck.NO_CONFIGURATION,
					Status.WARNING));
		}

		// The default explanation here will be used if the subreports list is
		// empty
		Status status = VisitReport.getWorstStatus(subReports);
		return new VisitReport(HealthCheck.getInstance(), activity,
				"MeasurementURI report", HealthCheck.NO_PROBLEM, status,
				subReports);
	}

}