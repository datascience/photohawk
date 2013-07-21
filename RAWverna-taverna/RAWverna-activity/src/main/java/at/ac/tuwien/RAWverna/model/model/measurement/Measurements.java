package at.ac.tuwien.RAWverna.model.model.measurement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.RAWverna.model.model.scales.FloatScale;
import at.ac.tuwien.RAWverna.model.model.values.FloatValue;
import at.ac.tuwien.RAWverna.model.model.values.INumericValue;

public class Measurements implements Serializable {

	private static final long serialVersionUID = -6824569989115569984L;

	private List<Measurement> list = new ArrayList<Measurement>();

	public void addMeasurement(Measurement measurement) {
		list.add(measurement);
	}

	public Measurement getAverage() {
		if (list.size() == 0) {
			return null;
		}
		Measurement m = list.get(0);
		if (!m.getProperty().isNumeric()) {
			throw new IllegalArgumentException(
					"cannot calculate average of nun-numeric value: "
							+ m.getProperty().getName());
		}

		Measurement measurement = new Measurement();
		MeasurableProperty property = new MeasurableProperty();
		String propertyName = m.getProperty().getName();
		property.setName(propertyName + ":accumulated:average");
		FloatScale scale = new FloatScale();
		property.setScale(scale);
		measurement.setProperty(property);

		scale.setUnit("" + list.size());

		Double d = 0.0;
		for (Measurement entry : list) {
			INumericValue value = (INumericValue) entry.getValue();
			d += value.value();
		}
		FloatValue average = (FloatValue) new FloatScale().createValue();
		average.setValue(d / list.size());
		measurement.setValue(average);
		return measurement;
	}

	public int getSize() {
		return list.size();
	}

	public List<Measurement> getList() {
		return list;
	}

	public void setList(List<Measurement> list) {
		this.list = list;
	}
}
