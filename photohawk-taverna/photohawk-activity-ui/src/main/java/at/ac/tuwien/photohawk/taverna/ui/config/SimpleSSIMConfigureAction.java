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

import java.awt.Frame;
import java.awt.event.ActionEvent;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;

import at.ac.tuwien.photohawk.taverna.SimpleSSIMActivity;
import at.ac.tuwien.photohawk.taverna.SimpleSSIMActivityConfigurationBean;

/**
 * Configuration action for SSIM.
 */
@SuppressWarnings("serial")
public class SimpleSSIMConfigureAction extends
    ActivityConfigurationAction<SimpleSSIMActivity, SimpleSSIMActivityConfigurationBean> {

    /**
     * Creates a new instance for the provided activity.
     * 
     * @param activity
     *            the activity of this configuration
     * @param owner
     *            the owner frame
     */
    public SimpleSSIMConfigureAction(SimpleSSIMActivity activity, Frame owner) {
        super(activity);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void actionPerformed(ActionEvent e) {
        ActivityConfigurationDialog<SimpleSSIMActivity, SimpleSSIMActivityConfigurationBean> currentDialog = ActivityConfigurationAction
            .getDialog(getActivity());
        if (currentDialog != null) {
            currentDialog.toFront();
            return;
        }
        SimpleSSIMConfigurationPanel panel = new SimpleSSIMConfigurationPanel(getActivity());
        ActivityConfigurationDialog<SimpleSSIMActivity, SimpleSSIMActivityConfigurationBean> dialog = new ActivityConfigurationDialog<SimpleSSIMActivity, SimpleSSIMActivityConfigurationBean>(
            getActivity(), panel);

        ActivityConfigurationAction.setDialog(getActivity(), dialog);
    }
}
