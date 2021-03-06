/*******************************************************************************
 * Copyright 2013 Vienna University of Technology
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package at.ac.tuwien.photohawk.taverna.ui.config;

import java.net.URI;
import java.util.Collection;

import javax.swing.JOptionPane;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationPanel;
import at.ac.tuwien.photohawk.taverna.JavaImageEvaluatorActivity;
import at.ac.tuwien.photohawk.taverna.JavaImageEvaluatorActivityConfigurationBean;
import at.ac.tuwien.photohawk.taverna.evaluation.evaluators.JavaImageComparisonEvaluator;
import at.ac.tuwien.photohawk.taverna.ui.config.evaluation.MeasurementSelectorPanel;

@SuppressWarnings("serial")
public class JavaImageEvaluatorConfigurationPanel extends
    ActivityConfigurationPanel<JavaImageEvaluatorActivity, JavaImageEvaluatorActivityConfigurationBean> {

    private JavaImageEvaluatorActivity activity;
    private JavaImageEvaluatorActivityConfigurationBean configBean;

    private MeasurementSelectorPanel selector;

    public JavaImageEvaluatorConfigurationPanel(JavaImageEvaluatorActivity activity) {
        this.activity = activity;
        initGui();
        refreshConfiguration();
    }

    protected void initGui() {
        removeAll();

        selector = new MeasurementSelectorPanel(new JavaImageComparisonEvaluator());
        add(selector);

        // FIXME: Create GUI depending on activity configuration bean
        // JLabel labelString = new JLabel("Mesaurement URI:");
        // add(labelString);
        // fieldmeasurementURI = new JTextField(60);
        // add(fieldmeasurementURI);
        // labelString.setLabelFor(fieldmeasurementURI);
        //
        // // Populate fields from activity configuration bean
        // refreshConfiguration();
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
            JOptionPane.showMessageDialog(this, ex.getCause().getMessage(), "Invalid URI", JOptionPane.ERROR_MESSAGE);
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
    public JavaImageEvaluatorActivityConfigurationBean getConfiguration() {
        // Should already have been made by noteConfiguration()
        return configBean;
    }

    /**
     * Check if the user has changed the configuration from the original
     */
    @Override
    public boolean isConfigurationChanged() {
        Collection<String> originalMeasurementURIs = configBean.getMeasurementURIs();

        // true (changed) unless all fields match the originals
        return !(originalMeasurementURIs.equals(selector.getSelectedValues()));
    }

    /**
     * Prepare a new configuration bean from the UI, to be returned with
     * getConfiguration()
     */
    @Override
    public void noteConfiguration() {
        configBean = new JavaImageEvaluatorActivityConfigurationBean();

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
