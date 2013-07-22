package at.ac.tuwien.RAWverna.measurementuri;

import java.util.ArrayList;
import java.util.Collection;

public class MeasurementURIConfigurationBean {

    private Collection<String> measurementURIs = null;

    public Collection<String> getMeasurementURIs() {
        if (null == measurementURIs) {
            return new ArrayList<String>(0);
        } else {
            return measurementURIs;
        }
    }

    public void setMeasurementURIs(Collection<String> measurementURIs) {
        this.measurementURIs = measurementURIs;
    }

}