package at.ac.tuwien.RAWverna;

import java.util.List;

import net.sf.taverna.t2.visit.VisitReport;
import at.ac.tuwien.RAWverna.measurementuri.MeasurementURIHealthChecker;

/**
 * Health checker for Java image evaluator activity.
 */
public class JavaImageEvaluatorActivityHealthChecker extends
    MeasurementURIHealthChecker<JavaImageEvaluatorActivity, JavaImageEvaluatorActivityConfigurationBean> {

    @Override
    public boolean canVisit(Object o) {
        // Return True if we can visit the object. We could do
        // deeper (but not time consuming) checks here, for instance
        // if the health checker only deals with
        // JavaImageEvaluatorActivityHealthChecker where
        // a certain configuration option is enabled.
        return o instanceof JavaImageEvaluatorActivity;
    }

    @Override
    public VisitReport visit(JavaImageEvaluatorActivity activity, List<Object> ancestry) {
        VisitReport report = super.visit(activity, ancestry);
        report.setMessage("JavaImageEvaluator report");
        return report;
    }

}
