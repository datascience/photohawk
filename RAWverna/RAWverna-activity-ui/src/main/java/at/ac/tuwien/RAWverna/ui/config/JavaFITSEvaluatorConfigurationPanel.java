package at.ac.tuwien.RAWverna.ui.config;

import java.net.URI;
import java.util.Collection;

import javax.swing.JOptionPane;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationPanel;
import at.ac.tuwien.RAWverna.JavaFITSEvaluatorActivity;
import at.ac.tuwien.RAWverna.JavaFITSEvaluatorActivityConfigurationBean;
import at.ac.tuwien.RAWverna.evaluation.evaluators.JavaFITSEvaluator;
import at.ac.tuwien.RAWverna.measurementuri.MeasurementURIActivity;
import at.ac.tuwien.RAWverna.ui.config.evaluation.MeasurementSelectorPanel;

@SuppressWarnings("serial")
public class JavaFITSEvaluatorConfigurationPanel
		extends
		ActivityConfigurationPanel<JavaFITSEvaluatorActivity, JavaFITSEvaluatorActivityConfigurationBean> {

	private JavaFITSEvaluatorActivity activity;
	private JavaFITSEvaluatorActivityConfigurationBean configBean;

	private MeasurementSelectorPanel selector;

	public JavaFITSEvaluatorConfigurationPanel(
			JavaFITSEvaluatorActivity activity) {
		this.activity = activity;
		initGui();
		refreshConfiguration();
	}

	protected void initGui() {
		removeAll();

		// Add measurement URI selector
		selector = new MeasurementSelectorPanel(new JavaFITSEvaluator());
		add(selector);
	}

	/**
	 * Check that user values in UI are valid
	 */
	@Override
	public boolean checkValues() {
		try {
			for (String uri : selector.getSelectedValues()) {
				URI.create(uri);
			}
		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(this, ex.getCause().getMessage(),
					"Invalid URI", JOptionPane.ERROR_MESSAGE);
			// Not valid, return false
			return false;
		}
		// All valid, return true
		return true;
	}

	/**
	 * Return configuration bean generated from user interface last time
	 * noteConfiguration() was called.
	 */
	@Override
	public JavaFITSEvaluatorActivityConfigurationBean getConfiguration() {
		// Should already have been made by noteConfiguration()
		return configBean;
	}

	/**
	 * Check if the user has changed the configuration from the original
	 */
	@Override
	public boolean isConfigurationChanged() {
		Collection<String> originalMeasurementURIs = configBean
				.getMeasurementURIs();

		// true (changed) unless all fields match the originals
		return !(originalMeasurementURIs.equals(selector.getSelectedValues()));
	}

	/**
	 * Prepare a new configuration bean from the UI, to be returned with
	 * getConfiguration()
	 */
	@Override
	public void noteConfiguration() {
		configBean = new JavaFITSEvaluatorActivityConfigurationBean();

		configBean.setMeasurementURIs(selector.getSelectedValues());
	}

	/**
	 * Update GUI from a changed configuration bean (perhaps by undo/redo).
	 * 
	 */
	@Override
	public void refreshConfiguration() {
		configBean = activity.getConfiguration();

		selector.setSelectedValues(configBean.getMeasurementURIs());
	}
}
