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

import java.awt.GridLayout;
import java.net.URI;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationPanel;

import at.ac.tuwien.photohawk.taverna.RAWvernaActivity;
import at.ac.tuwien.photohawk.taverna.RAWvernaActivityConfigurationBean;

@SuppressWarnings("serial")
public class RAWvernaConfigurationPanel extends
    ActivityConfigurationPanel<RAWvernaActivity, RAWvernaActivityConfigurationBean> {

    private RAWvernaActivity activity;
    private RAWvernaActivityConfigurationBean configBean;

    private JTextField fieldString;
    private JTextField fieldURI;

    public RAWvernaConfigurationPanel(RAWvernaActivity activity) {
        this.activity = activity;
        initGui();
    }

    protected void initGui() {
        removeAll();
        setLayout(new GridLayout(0, 2));

        // FIXME: Create GUI depending on activity configuration bean
        JLabel labelString = new JLabel("Example string:");
        add(labelString);
        fieldString = new JTextField(20);
        add(fieldString);
        labelString.setLabelFor(fieldString);

        JLabel labelURI = new JLabel("Example URI:");
        add(labelURI);
        fieldURI = new JTextField(25);
        add(fieldURI);
        labelURI.setLabelFor(fieldURI);

        // Populate fields from activity configuration bean
        refreshConfiguration();
    }

    /**
     * Check that user values in UI are valid
     */
    @Override
    public boolean checkValues() {
        try {
            URI.create(fieldURI.getText());
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
    public RAWvernaActivityConfigurationBean getConfiguration() {
        // Should already have been made by noteConfiguration()
        return configBean;
    }

    /**
     * Check if the user has changed the configuration from the original
     */
    @Override
    public boolean isConfigurationChanged() {
        String originalString = configBean.getExampleString();
        String originalUri = configBean.getExampleUri().toASCIIString();
        // true (changed) unless all fields match the originals
        return !(originalString.equals(fieldString.getText()) && originalUri.equals(fieldURI.getText()));
    }

    /**
     * Prepare a new configuration bean from the UI, to be returned with
     * getConfiguration()
     */
    @Override
    public void noteConfiguration() {
        configBean = new RAWvernaActivityConfigurationBean();

        // FIXME: Update bean fields from your UI elements
        configBean.setExampleString(fieldString.getText());
        configBean.setExampleUri(URI.create(fieldURI.getText()));
    }

    /**
     * Update GUI from a changed configuration bean (perhaps by undo/redo).
     * 
     */
    @Override
    public void refreshConfiguration() {
        configBean = activity.getConfiguration();

        // FIXME: Update UI elements from your bean fields
        fieldString.setText(configBean.getExampleString());
        fieldURI.setText(configBean.getExampleUri().toASCIIString());
    }
}
