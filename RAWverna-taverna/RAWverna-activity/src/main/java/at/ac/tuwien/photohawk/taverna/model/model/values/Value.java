/*******************************************************************************
 * Copyright 2006 - 2012 Vienna University of Technology,
 * Department of Software Technology and Interactive Systems, IFS
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
package at.ac.tuwien.photohawk.taverna.model.model.values;

import java.io.Serializable;

import at.ac.tuwien.photohawk.taverna.model.model.ChangeLog;
import at.ac.tuwien.photohawk.taverna.model.model.scales.Scale;

public abstract class Value implements Serializable, Cloneable {

    private static final long serialVersionUID = -5691552398563804873L;

    private ChangeLog changeLog = new ChangeLog();

    public ChangeLog getChangeLog() {
        return changeLog;
    }

    private Scale scale;

    private String comment = "";

    // private String comment;

    public Scale getScale() {
        return scale;
    }

    public void setScale(Scale scale) {
        this.scale = scale;
    }

    public void setChangeLog(ChangeLog value) {
        changeLog = value;
    }

    /**
     * Evaluates this Value Object and returns if everything is ok. Used in
     * proceed of EvaluateAction so that you can only proceed if every value in
     * a Leaf is completely evaluated.
     * 
     * @return evaluation status of this Value Object (default = false)
     */
    public boolean isChanged() {
        return this.changeLog.isAltered();
    }

    public void touch() {
        this.changeLog.touch();
    }

    public boolean isEvaluated() {
        return scale.isEvaluated(this);
    }

    /**
     * Returns an object with the same values. Important: Does not copy id and
     * link to scale!
     * 
     * Derived classes have to override this.
     */
    public Value clone() {
        try {
            Value clone = (Value) super.clone();
            clone.scale = null;
            // created-timestamp is automatically set to now
            clone.setChangeLog(new ChangeLog(this.getChangeLog().getChangedBy()));
            return clone;
        } catch (CloneNotSupportedException e) {
            // never thrown
            return null;
        }
    }

    public String getFormattedValue() {
        return "abstract value";
    }

    /**
     * parses a String value and sets the value appropriately. At the moment
     * there are no guarantees about error checking or behaviour, this needs to
     * be used carefully or experimentally ;)
     * 
     * @param text
     *            String to parse
     */
    public abstract void parse(String text);

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
