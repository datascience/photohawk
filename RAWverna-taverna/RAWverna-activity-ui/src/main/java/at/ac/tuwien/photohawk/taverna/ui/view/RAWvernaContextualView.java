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
package at.ac.tuwien.photohawk.taverna.ui.view;

import java.awt.Frame;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;

import at.ac.tuwien.photohawk.taverna.RAWvernaActivity;
import at.ac.tuwien.photohawk.taverna.RAWvernaActivityConfigurationBean;
import at.ac.tuwien.photohawk.taverna.ui.config.RAWvernaConfigureAction;

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
        RAWvernaActivityConfigurationBean configuration = activity.getConfiguration();
        return "RAWverna service " + configuration.getExampleString();
    }

    /**
     * Typically called when the activity configuration has changed.
     */
    @Override
    public void refreshView() {
        RAWvernaActivityConfigurationBean configuration = activity.getConfiguration();
        description.setText("RAWverna service " + configuration.getExampleUri() + " - "
            + configuration.getExampleString());
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
