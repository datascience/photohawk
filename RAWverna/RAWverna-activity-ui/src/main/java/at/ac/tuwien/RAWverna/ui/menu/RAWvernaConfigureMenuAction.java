package at.ac.tuwien.RAWverna.ui.menu;

import javax.swing.Action;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;
import at.ac.tuwien.RAWverna.RAWvernaActivity;
import at.ac.tuwien.RAWverna.ui.config.RAWvernaConfigureAction;

public class RAWvernaConfigureMenuAction extends
		AbstractConfigureActivityMenuAction<RAWvernaActivity> {

	public RAWvernaConfigureMenuAction() {
		super(RAWvernaActivity.class);
	}

	@Override
	protected Action createAction() {
		RAWvernaActivity a = findActivity();
		Action result = null;
		result = new RAWvernaConfigureAction(findActivity(),
				getParentFrame());
		result.putValue(Action.NAME, "Configure example service");
		addMenuDots(result);
		return result;
	}

}
