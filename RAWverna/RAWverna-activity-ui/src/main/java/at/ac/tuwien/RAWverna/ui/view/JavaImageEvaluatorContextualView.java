package at.ac.tuwien.RAWverna.ui.view;

import java.awt.Frame;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import at.ac.tuwien.RAWverna.JavaImageEvaluatorActivity;
import at.ac.tuwien.RAWverna.JavaImageEvaluatorActivityConfigurationBean;
import at.ac.tuwien.RAWverna.ui.config.JavaImageEvaluatorConfigureAction;

@SuppressWarnings("serial")
public class JavaImageEvaluatorContextualView extends ContextualView {
	private final JavaImageEvaluatorActivity activity;
	private JLabel description = new JLabel("ads");

	public JavaImageEvaluatorContextualView(JavaImageEvaluatorActivity activity) {
		this.activity = activity;
		initView();
	}

	@Override
	public JComponent getMainFrame() {
		JPanel jPanel = new JPanel();
		jPanel.add(description);
		refreshView();
		return jPanel;
	}

	@Override
	public String getViewTitle() {
		JavaImageEvaluatorActivityConfigurationBean configuration = activity.getConfiguration();
		return "Java Image Evaluator";
	}

	/**
	 * Typically called when the activity configuration has changed.
	 */
	@Override
	public void refreshView() {
		JavaImageEvaluatorActivityConfigurationBean configuration = activity.getConfiguration();
		description.setText("RAWverna service");
		// TODO: Might also show extra service information looked
		// up dynamically from endpoint/registry
	}

	/**
	 * View position hint
	 */
	@Override
	public int getPreferredPosition() {
		// We want to be on top
		return 100;
	}

	@Override
	public Action getConfigureAction(final Frame owner) {
		return new JavaImageEvaluatorConfigureAction(activity, owner);
	}

}
