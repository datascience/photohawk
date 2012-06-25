package at.ac.tuwien.RAWverna.ui.view;

import java.util.Arrays;
import java.util.List;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactory;
import at.ac.tuwien.RAWverna.JavaFITSEvaluatorActivity;
import at.ac.tuwien.RAWverna.JavaImageEvaluatorActivity;

public class JavaImageEvaluatorActivityContextViewFactory implements ContextualViewFactory<JavaImageEvaluatorActivity> {

	public boolean canHandle(Object selection) {
		return selection instanceof JavaImageEvaluatorActivity;
	}

	public List<ContextualView> getViews(JavaImageEvaluatorActivity selection) {
		return Arrays.<ContextualView> asList(new JavaImageEvaluatorContextualView(selection));
	}

}
