package at.ac.tuwien.RAWverna.model.model.measurement;

import java.io.Serializable;

import at.ac.tuwien.RAWverna.model.model.ChangeLog;
import at.ac.tuwien.RAWverna.model.model.scales.Scale;

public class Metric implements Serializable, Cloneable {

    private static final long serialVersionUID = 569177099755236670L;

    private String metricId;

    private String name;

    /**
     * Hibernate note: standard length for a string column is 255 validation is
     * broken because we use facelet templates (issue resolved in Seam 2.0)
     * therefore allow "long" entries
     */
    private String description;

    private String type;

    private Scale scale;

    private ChangeLog changeLog = new ChangeLog();

    public String getMetricId() {
        return metricId;
    }

    public void setMetricId(String metricId) {
        this.metricId = metricId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ChangeLog getChangeLog() {
        return changeLog;
    }

    public void setChangeLog(ChangeLog value) {
        changeLog = value;
    }

    public boolean isChanged() {
        return changeLog.isAltered();
    }

    public void touch() {
        changeLog.touch();
    }

    /**
     * returns a clone of self. Implemented for storing and inserting fragments.
     * Subclasses obtain a shallow copy by invoking this method, then modifying
     * the fields required to obtain a deep copy of this object. the id is not
     * copied
     */
    public Metric clone() {
        try {
            Metric clone = (Metric) super.clone();
            // created-timestamp is automatically set to now
            // clone.setChangeLog(new
            // ChangeLog(this.getChangeLog().getChangedBy()));
            if (this.scale != null) {
                clone.setScale(this.scale.clone());
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            // never thrown
            return null;
        }
    }

    public void assign(Metric m) {
        metricId = m.metricId;
        name = m.name;
        description = m.description;
        type = m.type;
        if (m.scale != null) {
            scale = m.scale.clone();
        } else {
            scale = null;
        }
    }

    public Scale getScale() {
        return scale;
    }

    public void setScale(Scale scale) {
        this.scale = scale;
    }
}
