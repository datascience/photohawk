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
package at.ac.tuwien.photohawk.taverna.model.evaluation;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import at.ac.tuwien.photohawk.taverna.model.model.measurement.MeasurableProperty;
import at.ac.tuwien.photohawk.taverna.model.model.measurement.MeasurementInfo;
import at.ac.tuwien.photohawk.taverna.model.model.measurement.Metric;
import at.ac.tuwien.photohawk.taverna.model.model.scales.Scale;
import at.ac.tuwien.photohawk.taverna.model.model.util.MeasurementInfoUri;

public class MeasurementsDescriptor {
    private static final Logger log = Logger.getLogger(MeasurementsDescriptor.class);

    /**
     * a list of all known measurable properties, accessible by their propertyId
     */
    private Map<String, MeasurableProperty> properties = new HashMap<String, MeasurableProperty>();

    /**
     * a list of all known metrics, accessible by their metricId
     */
    // private Map<String, Metric> metricInfo= new HashMap<String, Metric> ();

    /**
     * a map of (measured and derived) measurements (their uri's) to their
     * corresponding scales.
     */
    private Map<String, Scale> measurementScales = new HashMap<String, Scale>();

    public MeasurementsDescriptor() {
    }

    public Collection<MeasurableProperty> getPossibleMeasurements() {
        return properties.values();
    }

    public void clearMeasurementInfos() {
        properties.clear();
        measurementScales.clear();
    }

    /**
     * loads one measurement descriptor file, parses it and adds the measurement
     * infos (property,metric,scale) to {@link #properties},
     * {@link #measurementScales}
     * 
     * @param descriptor
     *            reads the descriptor.
     */
    public void addMeasurementInfos(Reader descriptor) {

        MeasurementsDescriptorParser parser = new MeasurementsDescriptorParser();

        Map<String, MeasurableProperty> digestedProperties = new HashMap<String, MeasurableProperty>();
        Map<String, Metric> digestedMetrics = new HashMap<String, Metric>();

        parser.load(descriptor, digestedProperties, digestedMetrics);

        // merge digested properties with already existing
        for (MeasurableProperty p : digestedProperties.values()) {
            MeasurementInfo helperInfo = new MeasurementInfo();
            helperInfo.setMetric(null);
            helperInfo.setProperty(p);
            String uri = helperInfo.getUri();
            String propertyId = p.getPropertyId();
            if (uri == null) {
                log.error("found invalid property definition");
            } else {
                // We check if we have that property already. If not, we put
                // the property into the map.
                MeasurableProperty existingP = properties.get(propertyId);
                if (existingP == null) {
                    // this property could not be measured so far - add it to
                    // the map
                    properties.put(propertyId, p);
                    // and add the scale of this property too
                    measurementScales.put(uri, p.getScale());
                }
                // We look at all metrics and look
                // a. is the property+metric already mapped to a scale?
                // b. is the metric already listed in the property?
                for (Metric m : p.getPossibleMetrics()) {
                    helperInfo.setMetric(m);
                    uri = helperInfo.getUri();
                    // a. if property+metric isnt yet mapped, we put down the
                    // scale.
                    if (!measurementScales.containsKey(uri)) {
                        measurementScales.put(uri, m.getScale());
                    }
                    // If we have an existing property, we have to check if it
                    // contains this metric.
                    // If it does not, we add the metric to ist list.
                    if (existingP != null && (!containsMetric(m.getMetricId(), existingP.getPossibleMetrics()))) {
                        existingP.getPossibleMetrics().add(m);
                    }

                }
            }
        }
    }

    private boolean containsMetric(String id, List<Metric> metrics) {
        if ((id == null) || ("".equals(id))) {
            return false;
        }
        for (Metric m : metrics) {
            if (id.equals(m.getMetricId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * returns the scale of this measurement, or null if the measurement uri is
     * unknown
     * 
     * @param m
     * @return
     */
    public Scale getMeasurementScale(MeasurementInfoUri m) {
        return measurementScales.get(m.getAsURI());
    }

    /**
     * returns the scale for each measurement, as a list of classnames
     */
    public List<Scale> getMeasurementScales(List<MeasurementInfoUri> measurements) {
        List<Scale> scales = new ArrayList<Scale>();
        for (MeasurementInfoUri m : measurements) {
            scales.add(getMeasurementScale(m));
        }
        return scales;
    }

}
