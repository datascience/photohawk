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
package at.ac.tuwien.photohawk.taverna.ui.config.evaluation;

import java.awt.Dimension;
import java.awt.Panel;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import at.ac.tuwien.photohawk.taverna.evaluation.IEvaluator;
import at.ac.tuwien.photohawk.taverna.model.evaluation.MeasurementsDescriptor;
import at.ac.tuwien.photohawk.taverna.model.model.measurement.MeasurableProperty;
import at.ac.tuwien.photohawk.taverna.model.model.measurement.MeasurementInfo;
import at.ac.tuwien.photohawk.taverna.model.model.measurement.Metric;

public class MeasurementSelectorPanel extends Panel {

    private JList list = null;

    /**
     * Generated serial version UID
     */
    private static final long serialVersionUID = -6785606479823259035L;

    private MeasurementsDescriptor descriptor;

    public MeasurementSelectorPanel(IEvaluator evaluator) {

        String descriptorStr = evaluator.getPossibleMeasurements();
        descriptor = new MeasurementsDescriptor();
        descriptor.addMeasurementInfos(new StringReader(descriptorStr));

        createPanel();
    }

    private void createPanel() {

        Collection<MeasurableProperty> properties = descriptor.getPossibleMeasurements();
        ArrayList<String> uriStrings = new ArrayList<String>();

        MeasurementInfo helperInfo = new MeasurementInfo();
        for (MeasurableProperty p : properties) {
            helperInfo.setProperty(p);

            if (p.getPossibleMetrics() == null || p.getPossibleMetrics().size() <= 0) {
                helperInfo.setMetric(null);
                uriStrings.add(helperInfo.getUri());
            } else {
                for (Metric m : p.getPossibleMetrics()) {
                    helperInfo.setMetric(m);
                    uriStrings.add(helperInfo.getUri());
                }
            }
        }

        list = new JList(uriStrings.toArray());
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);

        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(400, 600));
        listScroller.setAlignmentX(LEFT_ALIGNMENT);

        add(listScroller);
    }

    public Collection<String> getSelectedValues() {
        return Arrays.asList(Arrays.copyOf(list.getSelectedValues(), list.getSelectedValues().length, String[].class));
    }

    public void setSelectedValues(Collection<String> values) {
        list.clearSelection();
        // for (String v : values) {
        // int index = ((DefaultListModel) list.getModel()).indexOf(v);
        // if (index >= 0) {
        // list.addSelectionInterval(index,
        // }
        // }

        for (int i = 0; i < list.getModel().getSize(); i++) {
            if (values.contains(list.getModel().getElementAt(i))) {
                list.addSelectionInterval(i, i);
            }

        }
    }
}
