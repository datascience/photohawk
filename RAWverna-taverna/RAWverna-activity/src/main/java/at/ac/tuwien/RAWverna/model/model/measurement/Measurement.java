package at.ac.tuwien.RAWverna.model.model.measurement;

import java.io.Serializable;

import at.ac.tuwien.RAWverna.model.model.scales.FreeStringScale;
import at.ac.tuwien.RAWverna.model.model.scales.PositiveFloatScale;
import at.ac.tuwien.RAWverna.model.model.values.FreeStringValue;
import at.ac.tuwien.RAWverna.model.model.values.PositiveFloatValue;
import at.ac.tuwien.RAWverna.model.model.values.Value;

public class Measurement implements Serializable {
    private static final long serialVersionUID = 1189511961248081431L;

    private MeasurableProperty property;

    private Value value;

    public Measurement() {

    }

    public Measurement(String propertyName, String value) {
        MeasurableProperty p = new MeasurableProperty();
        p.setName(propertyName);
        p.setScale(new FreeStringScale());
        FreeStringValue s = (FreeStringValue) p.getScale().createValue();
        s.setValue(value);
        this.setProperty(p);
        this.setValue(s);
    }

    public Measurement(String propertyName, double value) {
        MeasurableProperty p = new MeasurableProperty();
        p.setName(propertyName);
        p.setScale(new PositiveFloatScale());
        PositiveFloatValue v = (PositiveFloatValue) p.getScale().createValue();
        v.setValue(value);
        this.setProperty(p);
        this.setValue(v);
    }

    public MeasurableProperty getProperty() {
        return property;
    }

    public void setProperty(MeasurableProperty property) {
        this.property = property;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }
}
