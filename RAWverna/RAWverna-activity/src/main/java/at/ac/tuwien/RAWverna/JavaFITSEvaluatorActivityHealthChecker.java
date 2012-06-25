package at.ac.tuwien.RAWverna;

import java.util.List;

import net.sf.taverna.t2.visit.VisitReport;
import at.ac.tuwien.RAWverna.measurementuri.MeasurementURIHealthChecker;

/**
 * Health checker for Java FITS evaluator
 */
public class JavaFITSEvaluatorActivityHealthChecker
		extends
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
	public VisitReport visit(JavaFITSEvaluatorActivity activity,
			List<Object> ancestry) {
		VisitReport report = super.visit(activity, ancestry);
		report.setMessage("JavaImageEvaluator report");
		return report;
	}

}
