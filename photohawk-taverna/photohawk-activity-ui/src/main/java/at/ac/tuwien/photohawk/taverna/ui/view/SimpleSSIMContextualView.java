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
import javax.swing.JEditorPane;

import net.sf.taverna.t2.lang.ui.HtmlUtils;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;

import at.ac.tuwien.photohawk.taverna.SimpleSSIMActivity;
import at.ac.tuwien.photohawk.taverna.SimpleSSIMActivityConfigurationBean;
import at.ac.tuwien.photohawk.taverna.ui.config.SimpleSSIMConfigureAction;

/**
 * Contextual view for SSIM.
 */
public class SimpleSSIMContextualView extends ContextualView {
    private static final long serialVersionUID = 2608878796411766535L;

    private static final int PREFERRED_POSITION = 100;

    private SimpleSSIMActivity activity;

    private JEditorPane editorPane;

    /**
     * Creates a new instance for the provided activity.
     * 
     * @param activity
     *            the activity
     */
    public SimpleSSIMContextualView(SimpleSSIMActivity activity) {
        super();
        this.activity = activity;
        initView();
    }

    @Override
    public String getViewTitle() {
        return "SSIM";
    }

    @Override
    public JComponent getMainFrame() {
        editorPane = HtmlUtils.createEditorPane(buildHtml());
        return HtmlUtils.panelForHtml(editorPane);
    }

    /**
     * Creates the HTML view for the activity.
     * 
     * @return the HTML view as string
     */
    private String buildHtml() {
        SimpleSSIMActivityConfigurationBean configuration = activity.getConfiguration();
        StringBuilder sb = new StringBuilder();
        sb.append(HtmlUtils.getHtmlHead("white"));
        sb.append(HtmlUtils.buildTableOpeningTag());
        sb.append("<tr><th colspan=\"2\">");
        sb.append(getViewTitle());
        sb.append("</th></tr>");
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
        sb.append("</table></body></html>");
        return sb.toString();
    }

    @Override
    public int getPreferredPosition() {
        return PREFERRED_POSITION;
    }

    @Override
    public Action getConfigureAction(final Frame owner) {
        return new SimpleSSIMConfigureAction(activity, owner);
    }

    @Override
    public void refreshView() {
        editorPane.setText(buildHtml());
    }
}
