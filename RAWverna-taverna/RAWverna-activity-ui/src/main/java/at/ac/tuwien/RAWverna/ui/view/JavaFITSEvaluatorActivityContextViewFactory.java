package at.ac.tuwien.RAWverna.ui.view;

import java.util.Arrays;
import java.util.List;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactory;
import at.ac.tuwien.RAWverna.JavaFITSEvaluatorActivity;

public class JavaFITSEvaluatorActivityContextViewFactory implements ContextualViewFactory<JavaFITSEvaluatorActivity> {

	public boolean canHandle(Object selection) {
		return selection instanceof JavaFITSEvaluatorActivity;
	}

	public List<ContextualView> getViews(JavaFITSEvaluatorActivity selection) {
		return Arrays.<ContextualView> asList(new JavaFITSEvaluatorContextualView(selection));
	}

}
