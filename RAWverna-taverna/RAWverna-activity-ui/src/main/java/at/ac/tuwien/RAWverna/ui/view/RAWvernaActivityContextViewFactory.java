package at.ac.tuwien.RAWverna.ui.view;

import java.util.Arrays;
import java.util.List;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactory;

import at.ac.tuwien.RAWverna.RAWvernaActivity;

public class RAWvernaActivityContextViewFactory implements
		ContextualViewFactory<RAWvernaActivity> {

	public boolean canHandle(Object selection) {
		return selection instanceof RAWvernaActivity;
	}

	public List<ContextualView> getViews(RAWvernaActivity selection) {
		return Arrays.<ContextualView>asList(new RAWvernaContextualView(selection));
	}
	
}
