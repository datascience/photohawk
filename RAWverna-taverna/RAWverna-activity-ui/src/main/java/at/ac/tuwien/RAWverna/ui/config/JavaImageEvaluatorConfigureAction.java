package at.ac.tuwien.RAWverna.ui.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;
import at.ac.tuwien.RAWverna.JavaImageEvaluatorActivity;
import at.ac.tuwien.RAWverna.JavaImageEvaluatorActivityConfigurationBean;

@SuppressWarnings("serial")
public class JavaImageEvaluatorConfigureAction extends
		ActivityConfigurationAction<JavaImageEvaluatorActivity, JavaImageEvaluatorActivityConfigurationBean> {

	public JavaImageEvaluatorConfigureAction(JavaImageEvaluatorActivity activity, Frame owner) {
		super(activity);
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		ActivityConfigurationDialog<JavaImageEvaluatorActivity, JavaImageEvaluatorActivityConfigurationBean> currentDialog = ActivityConfigurationAction
				.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}
		JavaImageEvaluatorConfigurationPanel panel = new JavaImageEvaluatorConfigurationPanel(getActivity());
		ActivityConfigurationDialog<JavaImageEvaluatorActivity, JavaImageEvaluatorActivityConfigurationBean> dialog = new ActivityConfigurationDialog<JavaImageEvaluatorActivity, JavaImageEvaluatorActivityConfigurationBean>(
				getActivity(), panel);

		ActivityConfigurationAction.setDialog(getActivity(), dialog);

	}

}
