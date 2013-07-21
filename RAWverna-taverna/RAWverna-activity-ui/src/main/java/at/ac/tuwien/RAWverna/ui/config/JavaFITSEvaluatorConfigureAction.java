package at.ac.tuwien.RAWverna.ui.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;
import at.ac.tuwien.RAWverna.JavaFITSEvaluatorActivity;
import at.ac.tuwien.RAWverna.JavaFITSEvaluatorActivityConfigurationBean;
import at.ac.tuwien.RAWverna.JavaImageEvaluatorActivity;
import at.ac.tuwien.RAWverna.JavaImageEvaluatorActivityConfigurationBean;

@SuppressWarnings("serial")
public class JavaFITSEvaluatorConfigureAction extends
		ActivityConfigurationAction<JavaFITSEvaluatorActivity, JavaFITSEvaluatorActivityConfigurationBean> {

	public JavaFITSEvaluatorConfigureAction(JavaFITSEvaluatorActivity activity, Frame owner) {
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
		JavaFITSEvaluatorConfigurationPanel panel = new JavaFITSEvaluatorConfigurationPanel(getActivity());
		ActivityConfigurationDialog<JavaFITSEvaluatorActivity, JavaFITSEvaluatorActivityConfigurationBean> dialog = new ActivityConfigurationDialog<JavaFITSEvaluatorActivity, JavaFITSEvaluatorActivityConfigurationBean>(
				getActivity(), panel);

		ActivityConfigurationAction.setDialog(getActivity(), dialog);

	}

}
