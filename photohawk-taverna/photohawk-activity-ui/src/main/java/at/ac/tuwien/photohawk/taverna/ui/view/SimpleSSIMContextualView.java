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

import net.sf.taverna.t2.workbench.ui.actions.activity.HTMLBasedActivityContextualView;
import at.ac.tuwien.photohawk.taverna.SimpleSSIMActivity;
import at.ac.tuwien.photohawk.taverna.SimpleSSIMActivityConfigurationBean;
import at.ac.tuwien.photohawk.taverna.ui.config.SimpleSSIMConfigureAction;

public class SimpleSSIMContextualView extends HTMLBasedActivityContextualView<SimpleSSIMActivityConfigurationBean> {
    private static final long serialVersionUID = 2608878796411766535L;

    public SimpleSSIMContextualView(SimpleSSIMActivity activity) {
        super(activity);
    }

    @Override
    public String getViewTitle() {
        return "SSIM";
    }

    @Override
    protected String getRawTableRowsHtml() {
        SimpleSSIMActivityConfigurationBean configuration = getActivity().getConfiguration();
        StringBuilder sb = new StringBuilder();
        sb.append("<tr><td>Target size</td><td>");
        sb.append(configuration.getTargetSize());
        sb.append("</td></tr><tr><td>Threaded</td><td>");
        sb.append(configuration.isDoThreaded());
        sb.append("</td></tr>");
        if (configuration.isDoThreaded()) {
            sb.append("<tr><td>Number of threads</td><td>");
            sb.append(configuration.getThreadPoolSize());
            sb.append("</td></tr>");
        }
        return sb.toString();
    }

    @Override
    public int getPreferredPosition() {
        return 100;
    }

    @Override
    public Action getConfigureAction(final Frame owner) {
        return new SimpleSSIMConfigureAction(getActivity(), owner);
    }

    @Override
    public SimpleSSIMActivity getActivity() {
        return (SimpleSSIMActivity) super.getActivity();
    }
}
