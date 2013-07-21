package at.ac.tuwien.RAWverna.ui.config.evaluation;

import java.awt.Dimension;
import java.awt.Panel;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import at.ac.tuwien.RAWverna.evaluation.IEvaluator;
import at.ac.tuwien.RAWverna.model.evaluation.MeasurementsDescriptor;
import at.ac.tuwien.RAWverna.model.model.measurement.MeasurableProperty;
import at.ac.tuwien.RAWverna.model.model.measurement.MeasurementInfo;
import at.ac.tuwien.RAWverna.model.model.measurement.Metric;

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

		Collection<MeasurableProperty> properties = descriptor
				.getPossibleMeasurements();
		ArrayList<String> uriStrings = new ArrayList<String>();

		MeasurementInfo helperInfo = new MeasurementInfo();
		for (MeasurableProperty p : properties) {
			helperInfo.setProperty(p);

			if (p.getPossibleMetrics() == null
					|| p.getPossibleMetrics().size() <= 0) {
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
		return Arrays.asList(Arrays.copyOf(list.getSelectedValues(),
				list.getSelectedValues().length, String[].class));
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
