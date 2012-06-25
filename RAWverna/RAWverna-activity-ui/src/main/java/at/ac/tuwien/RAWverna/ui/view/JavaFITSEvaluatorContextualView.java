package at.ac.tuwien.RAWverna.ui.view;

import java.awt.Frame;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import at.ac.tuwien.RAWverna.JavaFITSEvaluatorActivity;
import at.ac.tuwien.RAWverna.measurementuri.MeasurementURIConfigurationBean;
import at.ac.tuwien.RAWverna.ui.config.JavaFITSEvaluatorConfigureAction;

@SuppressWarnings("serial")
public class JavaFITSEvaluatorContextualView extends ContextualView {
	private final JavaFITSEvaluatorActivity activity;
	private JLabel description = new JLabel("ads");

	public JavaFITSEvaluatorContextualView(JavaFITSEvaluatorActivity activity) {
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
		MeasurementURIConfigurationBean configuration = activity.getConfiguration();
		return "Java FITS Evaluator";
	}

	/**
	 * Typically called when the activity configuration has changed.
	 */
	@Override
	public void refreshView() {
		MeasurementURIConfigurationBean configuration = activity.getConfiguration();
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
		return new JavaFITSEvaluatorConfigureAction(activity, owner);
	}

}
