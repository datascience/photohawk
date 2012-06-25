package at.ac.tuwien.RAWverna.ui.view;

import java.awt.Frame;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;

import at.ac.tuwien.RAWverna.RAWvernaActivity;
import at.ac.tuwien.RAWverna.RAWvernaActivityConfigurationBean;
import at.ac.tuwien.RAWverna.ui.config.RAWvernaConfigureAction;

@SuppressWarnings("serial")
public class RAWvernaContextualView extends ContextualView {
	private final RAWvernaActivity activity;
	private JLabel description = new JLabel("ads");

	public RAWvernaContextualView(RAWvernaActivity activity) {
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
		RAWvernaActivityConfigurationBean configuration = activity
				.getConfiguration();
		return "RAWverna service " + configuration.getExampleString();
	}

	/**
	 * Typically called when the activity configuration has changed.
	 */
	@Override
	public void refreshView() {
		RAWvernaActivityConfigurationBean configuration = activity
				.getConfiguration();
		description.setText("RAWverna service " + configuration.getExampleUri()
				+ " - " + configuration.getExampleString());
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
		return new RAWvernaConfigureAction(activity, owner);
	}

}
