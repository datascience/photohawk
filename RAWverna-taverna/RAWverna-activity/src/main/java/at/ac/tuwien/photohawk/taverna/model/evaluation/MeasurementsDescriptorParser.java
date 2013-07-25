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

import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;

import at.ac.tuwien.photohawk.taverna.model.model.measurement.MeasurableProperty;
import at.ac.tuwien.photohawk.taverna.model.model.measurement.Metric;
import at.ac.tuwien.photohawk.taverna.model.model.scales.BooleanScale;
import at.ac.tuwien.photohawk.taverna.model.model.scales.FloatRangeScale;
import at.ac.tuwien.photohawk.taverna.model.model.scales.FloatScale;
import at.ac.tuwien.photohawk.taverna.model.model.scales.FreeStringScale;
import at.ac.tuwien.photohawk.taverna.model.model.scales.IntRangeScale;
import at.ac.tuwien.photohawk.taverna.model.model.scales.IntegerScale;
import at.ac.tuwien.photohawk.taverna.model.model.scales.OrdinalScale;
import at.ac.tuwien.photohawk.taverna.model.model.scales.PositiveFloatScale;
import at.ac.tuwien.photohawk.taverna.model.model.scales.PositiveIntegerScale;
import at.ac.tuwien.photohawk.taverna.model.model.scales.YanScale;

public class MeasurementsDescriptorParser {

    /**
     * a list of all known measurable properties, accessible by their propertyId
     * used by the digester
     */
    private Map<String, MeasurableProperty> propertyInfo;

    /**
     * a list of all known metrics, accessible by their metricId used by the
     * digester
     */
    private Map<String, Metric> metricInfo;

    public void load(InputStream in, Map<String, MeasurableProperty> propertyInfo, Map<String, Metric> metricInfo) {
        this.propertyInfo = propertyInfo;
        this.metricInfo = metricInfo;

        Digester d = setupDigester();
        try {
            d.parse(in);
            resolveMetrics();
        } catch (Exception e) {
            Logger.getLogger(getClass()).error("could not parse measurement infos", e);
        }
    }

    public void load(Reader in, Map<String, MeasurableProperty> propertyInfo, Map<String, Metric> metricInfo) {
        this.propertyInfo = propertyInfo;
        this.metricInfo = metricInfo;

        Digester d = setupDigester();
        try {
            d.parse(in);
            resolveMetrics();
        } catch (Exception e) {
            Logger.getLogger(getClass()).error("could not parse measurement infos", e);
        }
    }

    private void resolveMetrics() {
        // resolve references to metrics:
        for (MeasurableProperty p : propertyInfo.values()) {
            for (Metric m : p.getPossibleMetrics()) {
                Metric metric = metricInfo.get(m.getMetricId());
                if (metric != null) {
                    m.assign(metric);
                }
            }
        }
    }

    private Digester setupDigester() {
        Digester d = new Digester();
        d.setClassLoader(getClass().getClassLoader());
        // d.setUseContextClassLoader(true);
        d.push(this);

        /*
         * <measurableProperties> <property>
         * <propertyId>object://image/dimension/width</propertyId> <name>image
         * width</name> <description>the width of an image in
         * pixel</description> <scale type="positiveInteger"> <unit>pixel</unit>
         * </scale> <possibleMetrics> <metric metricId="equal"/> <metric
         * metricId="indDiff"/> </possibleMetrics> </property>
         */
        d.addObjectCreate("*/property", MeasurableProperty.class);
        d.addSetNext("*/property", "addProperty");
        d.addBeanPropertySetter("*/property/category", "categoryAsString");
        d.addBeanPropertySetter("*/property/propertyId");
        d.addBeanPropertySetter("*/property/name");
        d.addBeanPropertySetter("*/property/description");
        d.addObjectCreate("*/property/possibleMetrics", ArrayList.class);
        d.addSetNext("*/property/possibleMetrics", "setPossibleMetrics");
        // d.addObjectCreate("*/property/possibleMetrics/metric", Metric.class);
        d.addObjectCreate("*/possibleMetrics/metric", Metric.class);
        d.addSetProperties("*/possibleMetrics/metric");
        d.addSetNext("*/possibleMetrics/metric", "add");

        /*
         * <metric> <name>equal</name> <description></description> <scale
         * type="boolean" /> </metric>
         */
        d.addObjectCreate("*/metrics/metric", Metric.class);
        d.addSetProperties("*/metrics/metric");
        d.addBeanPropertySetter("*/metrics/metric/metricId");
        d.addBeanPropertySetter("*/metrics/metric/name");
        d.addBeanPropertySetter("*/metrics/metric/description");
        d.addSetNext("*/metrics/metric", "addMetric");

        addCreateScale(d, BooleanScale.class);
        addCreateScale(d, FloatRangeScale.class);
        addCreateScale(d, FloatScale.class);
        addCreateScale(d, IntegerScale.class);
        addCreateScale(d, IntRangeScale.class);
        addCreateScale(d, OrdinalScale.class);
        addCreateScale(d, PositiveFloatScale.class);
        addCreateScale(d, PositiveIntegerScale.class);
        addCreateScale(d, YanScale.class);
        addCreateScale(d, FreeStringScale.class);
        return d;
    }

    /**
     * used by digester
     * 
     * @param p
     */
    public void addProperty(MeasurableProperty p) {
        propertyInfo.put(p.getPropertyId(), p);
    }

    /**
     * used by digester
     * 
     * @param m
     */
    public void addMetric(Metric m) {
        metricInfo.put(m.getMetricId(), m);
    }

    private static void addCreateScale(Digester digester, Class c) {
        String name = c.getName();
        name = name.substring(name.lastIndexOf(".") + 1);
        name = name.substring(0, 1).toLowerCase() + name.substring(1);

        String pattern = "*/" + name;
        digester.addObjectCreate(pattern, c);
        digester.addSetProperties(pattern);
        digester.addBeanPropertySetter(pattern + "/unit");
        digester.addSetNext(pattern, "setScale");
    }
}
