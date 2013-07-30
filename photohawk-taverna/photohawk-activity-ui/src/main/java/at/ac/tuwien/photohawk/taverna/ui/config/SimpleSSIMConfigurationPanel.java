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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationPanel;
import at.ac.tuwien.photohawk.taverna.SimpleSSIMActivity;
import at.ac.tuwien.photohawk.taverna.SimpleSSIMActivityConfigurationBean;

@SuppressWarnings("serial")
public class SimpleSSIMConfigurationPanel extends
    ActivityConfigurationPanel<SimpleSSIMActivity, SimpleSSIMActivityConfigurationBean> {

    private SimpleSSIMActivity activity;
    private SimpleSSIMActivityConfigurationBean configBean;

    private JTextField targetSizeText;
    private JCheckBox doThreadedBox;
    private JTextField threadPoolSizeText;

    public SimpleSSIMConfigurationPanel(SimpleSSIMActivity activity) {
        this.activity = activity;
        initGui();
    }

    protected void initGui() {
        removeAll();
        setLayout(new GridLayout(0, 2));

        JLabel targetSizeLabel = new JLabel("Target Size:");
        add(targetSizeLabel);
        targetSizeText = new JTextField(20);
        add(targetSizeText);
        targetSizeLabel.setLabelFor(targetSizeText);

        doThreadedBox = new JCheckBox("Run threaded");
        doThreadedBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox source = (JCheckBox) e.getItemSelectable();
                threadPoolSizeText.setEnabled(source.isSelected());
            }
        });
        add(doThreadedBox);
        add(new JLabel());

        JLabel numberOfThreadsLabel = new JLabel("Number of threads:");
        add(numberOfThreadsLabel);
        threadPoolSizeText = new JTextField(20);
        add(threadPoolSizeText);
        numberOfThreadsLabel.setLabelFor(threadPoolSizeText);

        // Populate fields from activity configuration bean
        refreshConfiguration();
    }

    /**
     * Check that user values in UI are valid
     */
    @Override
    public boolean checkValues() {
        try {
            int targetsize = Integer.parseInt(targetSizeText.getText());
            if (targetsize <= 0) {
                JOptionPane.showMessageDialog(this, "Target size must be larger than 0", "Target size",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Target size must be an integer value", "Target size",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (doThreadedBox.isSelected()) {
            try {
                int threadPoolSize = Integer.parseInt(threadPoolSizeText.getText());
                if (threadPoolSize <= 0) {
                    JOptionPane.showMessageDialog(this, "Number of threads must be larger than 0", "Number of threads",
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Number of threads must be an integer value", "Number of threads",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }

    /**
     * Return configuration bean generated from user interface last time
     * noteConfiguration() was called.
     */
    @Override
    public SimpleSSIMActivityConfigurationBean getConfiguration() {
        // Should already have been made by noteConfiguration()
        return configBean;
    }

    /**
     * Check if the user has changed the configuration from the original
     */
    @Override
    public boolean isConfigurationChanged() {
        // true (changed) unless all fields match the originals
        return !(Integer.toString(configBean.getTargetSize()).equals(targetSizeText.getText())
            && (configBean.isDoThreaded() == doThreadedBox.isSelected()) && Integer.toString(
            configBean.getThreadPoolSize()).equals(threadPoolSizeText.getText()));
    }

    /**
     * Prepare a new configuration bean from the UI, to be returned with
     * getConfiguration()
     */
    @Override
    public void noteConfiguration() {
        configBean = new SimpleSSIMActivityConfigurationBean();
        configBean.setTargetSize(Integer.parseInt(targetSizeText.getText()));
        configBean.setDoThreaded(doThreadedBox.isSelected());
        configBean.setThreadPoolSize(Integer.parseInt(threadPoolSizeText.getText()));
    }

    /**
     * Update GUI from a changed configuration bean (perhaps by undo/redo).
     * 
     */
    @Override
    public void refreshConfiguration() {
        configBean = activity.getConfiguration();
        targetSizeText.setText(Integer.toString(configBean.getTargetSize()));
        doThreadedBox.setSelected(configBean.isDoThreaded());
        threadPoolSizeText.setText(Integer.toString(configBean.getThreadPoolSize()));
        threadPoolSizeText.setEnabled(configBean.isDoThreaded());
    }
}
