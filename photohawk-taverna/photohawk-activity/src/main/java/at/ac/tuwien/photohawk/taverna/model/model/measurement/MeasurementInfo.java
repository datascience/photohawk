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
package at.ac.tuwien.photohawk.taverna.model.model.measurement;

import java.io.Serializable;

import at.ac.tuwien.photohawk.taverna.model.model.ChangeLog;
import at.ac.tuwien.photohawk.taverna.model.model.scales.Scale;
import at.ac.tuwien.photohawk.taverna.model.model.tree.CriterionCategory;
import at.ac.tuwien.photohawk.taverna.model.model.util.MeasurementInfoUri;

public class MeasurementInfo implements Serializable, Cloneable {
    private static final long serialVersionUID = -3942656115528678720L;

    private MeasurableProperty property;

    private Metric metric;

    private ChangeLog changeLog = new ChangeLog();

    public MeasurementInfoUri toMeasurementInfoUri() {
        MeasurementInfoUri uri = new MeasurementInfoUri(getUri());
        return uri;
    }

    public Scale getScale() {
        Scale s = null;
        if ((metric != null) && (metric.getScale() != null)) {
            s = metric.getScale();
        } else if ((property != null) && (property.getScale() != null)) {
            s = property.getScale();
        }
        return s;
    }

    public String getUnit() {
        Scale s = getScale();
        if (s != null) {
            return s.getUnit();
        } else {
            return null;
        }
    }

    /**
     * configures measurement info (its property and metric) with given uri
     * 
     * @param uri
     */
    public void fromUri(String uri) {
        MeasurementInfoUri info = new MeasurementInfoUri(uri);
        String scheme = info.getScheme();
        String path = info.getPath();
        String fragment = info.getFragment();

        if ((scheme == null) || (path == null)) {
            return;
        }

        int pathIdx = path.indexOf("/");
        if ((pathIdx == -1) || ((pathIdx + 1) >= path.length())) {
            throw new IllegalArgumentException("invalid measurment info uri - no path defined: " + uri);
        }

        String propertyId = path.substring(pathIdx + 1);
        path = path.substring(0, pathIdx);

        CriterionCategory cat = CriterionCategory.getType(scheme, path);
        if (cat == null) {
            throw new IllegalArgumentException(
                "invalid measurement info uri - scheme and path don't correspond to a criterion category: " + uri);
        }
        MeasurableProperty prop = getProperty();
        if (prop == null) {
            prop = new MeasurableProperty();
            setProperty(prop);
        } else {
            prop.setName(null);
            prop.setDescription(null);
            prop.getPossibleMetrics().clear();
            prop.setScale(null);
        }
        getProperty().setCategory(cat);
        getProperty().setPropertyId(propertyId);

        Metric metric = getMetric();
        if (metric == null) {
            metric = new Metric();
            setMetric(metric);
        } else {
            metric.setDescription(null);
            metric.setName(null);
            metric.setScale(null);
            metric.setType(null);
        }
        metric.setMetricId(fragment);

    }

    public String getUri() {
        String scheme = null;
        String path = null;

        if ((property == null) || (property.getCategory() == null)) {
            return null;
        }
        CriterionCategory cat = property.getCategory();

        scheme = cat.getCategory();
        path = cat.getSubCategory();

        String fragment;
        if ((metric != null) && (metric.getMetricId() != null)) {
            fragment = "#" + metric.getMetricId();
        } else {
            fragment = "";
        }
        return scheme + "://" + path + "/" + property.getPropertyId() + fragment;
    }

    /**
     * returns a clone of self. Implemented for storing and inserting fragments.
     * Subclasses obtain a shallow copy by invoking this method, then modifying
     * the fields required to obtain a deep copy of this object. the id is not
     * copied
     */
    public MeasurementInfo clone() {
        try {
            MeasurementInfo clone = (MeasurementInfo) super.clone();
            if (metric != null) {
                clone.setMetric(metric.clone());
            }
            if (property != null) {
                clone.setProperty(property.clone());
            }
            // created-timestamp is automatically set to now
            // clone.setChangeLog(new
            // ChangeLog(this.getChangeLog().getChangedBy()));
            return clone;
        } catch (CloneNotSupportedException e) {
            // never thrown
            return null;
        }
    }

    public MeasurableProperty getProperty() {
        return property;
    }

    public void setProperty(MeasurableProperty property) {
        this.property = property;
    }

    public Metric getMetric() {
        return metric;
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
    }

    public ChangeLog getChangeLog() {
        return changeLog;
    }

    public boolean isChanged() {
        return changeLog.isAltered();
    }

    public void touch() {
        changeLog.touch();

    }

    public void setChangeLog(ChangeLog changeLog) {
        this.changeLog = changeLog;
    }

}
