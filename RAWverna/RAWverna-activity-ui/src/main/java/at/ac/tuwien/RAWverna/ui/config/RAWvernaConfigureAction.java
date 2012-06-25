package at.ac.tuwien.RAWverna.ui.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;

import at.ac.tuwien.RAWverna.RAWvernaActivity;
import at.ac.tuwien.RAWverna.RAWvernaActivityConfigurationBean;

@SuppressWarnings("serial")
public class RAWvernaConfigureAction
		extends
		ActivityConfigurationAction<RAWvernaActivity,
        RAWvernaActivityConfigurationBean> {

	public RAWvernaConfigureAction(RAWvernaActivity activity, Frame owner) {
		super(activity);
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		ActivityConfigurationDialog<RAWvernaActivity, RAWvernaActivityConfigurationBean> currentDialog = ActivityConfigurationAction
				.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}
		RAWvernaConfigurationPanel panel = new RAWvernaConfigurationPanel(
				getActivity());
		ActivityConfigurationDialog<RAWvernaActivity,
        RAWvernaActivityConfigurationBean> dialog = new ActivityConfigurationDialog<RAWvernaActivity, RAWvernaActivityConfigurationBean>(
				getActivity(), panel);

		ActivityConfigurationAction.setDialog(getActivity(), dialog);

	}

}
