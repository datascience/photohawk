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
package at.ac.tuwien.photohawk.taverna.model.model.scales;

import java.io.Serializable;
import java.util.List;

import at.ac.tuwien.photohawk.taverna.model.model.values.BooleanValue;
import at.ac.tuwien.photohawk.taverna.model.model.values.Value;
import at.ac.tuwien.photohawk.taverna.model.validation.ValidationError;

/**
 * Base class for all scales that are used for measuring the degree of
 * fulfillment of requirements, i.e. {@link Leaf} nodes in the
 * {@link ObjectiveTree}. These scales have units, restrictions that define
 * possible values, and they are capable of constructing instances of
 * {@link Value} objects that correspond to their scale. I.e., a
 * {@link BooleanScale} will be able to create a {@link BooleanValue}.
 * Furthermore, they are also responsible of checking these Values for
 * correctness ({@link #isEvaluated(Value)}
 * 
 * @author Christoph Becker
 * @author Kevin Stadler
 */
public abstract class Scale implements Serializable, Cloneable {

    /**
     * We have to set the max and min limits here. Reason: Derby's limits are
     * different from the java.lang.Double limits. An exception is thrown when
     * any double value is calculated or entered that is outside of these value
     * ranges. Arithmetic operations do not round their resulting values to
     * zero. If the values are too small, you will receive an exception.
     */
    public static final double MAX_VALUE = 1.79769E+308;

    /**
     * see comment to {@link #MAX_VALUE}
     */
    public static final double MIN_VALUE = 2.225E-307;

    /**
     * This function has to check whether the provided {@link Value} object has
     * been evaluated properly, i.e. has an associated value that is valid and
     * has been changed at some time by the user (because default values might
     * be valid, too).
     * 
     * @param v
     *            the {@link Value} that shall be checked
     * @return true if the Value has been changed by the user.
     * @see Value#isChanged()
     * @see Value#isEvaluated()
     */
    public abstract boolean isEvaluated(Value v);

    /**
     * This field is needed only for the hibernate annotation transient -
     * because annotations need to be consistently on either fields, getters, or
     * setters.
     */
    protected String displayName = "Undefined scale";

    public abstract String getDisplayName();

    /**
     * Defines the separator to be used in string-encoding of possible
     * restriction settings, such as Yes/Acceptable/No or 0/5
     */
    public static final String SEPARATOR = "/";

    /**
     * the measurement unit of the Scale. E.g. "number of tools" supporting a
     * file format, "Euro" for the costs of a preservation action, etc.
     */
    private String unit = new String();

    public boolean isInteger() {
        return false;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public Scale clone() {
        try {
            Scale clone = (Scale) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            // never thrown
            return null;
        }
    }

    /**
     * This property is only needed for checking in the faces pages if we need
     * to display the fields for setting restrictions
     */
    protected boolean restricted = false;

    protected List<String> list;

    public abstract boolean isRestricted();

    /**
     * Must be overridden by subclasses to return the type of the scale -
     * ordinal or not ordinal, that's the question.
     * 
     * @return {@link ScaleType}
     */
    public abstract ScaleType getType();

    /**
     * Returns true if the basic properties of this value-object are correctly
     * specified, e.g. a restricted value has a restriction that is correctly
     * formatted etc. If this method returns false, it has to add a
     * corresponding error-message to the given list.
     * 
     * @param leafName
     *            name of the leaf this scale belongs to, need to be provided to
     *            be able to create sensible and traceable error-messages
     * @param errors
     *            If errors occur, error messages are added to this List.
     */
    public abstract boolean isCorrectlySpecified(String leafName, List<ValidationError> errors);

    /**
     * Creates a {@link Value} object corresponding to this scale.
     * 
     * @return subclass of {@link Value} corresponding to myself.
     */
    public abstract Value createValue();

    public String toString() {
        return getClass().getName();
    }

    public List<String> getList() {
        return list;
    }
}
