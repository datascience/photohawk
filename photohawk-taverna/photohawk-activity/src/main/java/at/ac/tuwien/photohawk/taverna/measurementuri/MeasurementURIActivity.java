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
package at.ac.tuwien.photohawk.taverna.measurementuri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.taverna.t2.invocation.InvocationContext;
import net.sf.taverna.t2.reference.ErrorDocument;
import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.processor.activity.AbstractAsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;

import org.apache.log4j.Logger;

import at.ac.tuwien.photohawk.taverna.model.model.util.MeasurementInfoUri;
import at.ac.tuwien.photohawk.taverna.model.model.values.Value;

public abstract class MeasurementURIActivity<C> extends AbstractAsynchronousActivity<C> {

    /**
     * Logger
     */
    protected Logger logger = Logger.getLogger(MeasurementURIActivity.class);

    /**
     * List of measurmentUris for this evaluator
     */
    protected List<MeasurementInfoUri> measurementInfoUris = new ArrayList<MeasurementInfoUri>(20);

    /**
     * Sets the measurement URIs for this evaluator
     * 
     * @param configBean
     *            - Configuration bean of this evaluator
     */
    protected void setMeasurementUris(MeasurementURIConfigurationBean configBean) {
        measurementInfoUris.clear();
        for (String uri : configBean.getMeasurementURIs()) {
            MeasurementInfoUri measurementInfoURI = new MeasurementInfoUri(uri);
            measurementInfoUris.add(measurementInfoURI);
        }
    }

    /**
     * Remove all set input and ouput ports and add ports related to the
     * measurement URIs.
     */
    protected void configurePorts() {

        if (logger.isDebugEnabled()) {
            logger.debug("Configuring ports");
        }

        // In case we are being reconfigured - remove existing ports first
        // to avoid duplicates
        removeInputs();
        removeOutputs();

        // Output ports
        for (MeasurementInfoUri uri : measurementInfoUris) {
            if (logger.isDebugEnabled()) {
                logger.debug("Adding port " + convertUriToPortname(uri));
            }
            addOutput(convertUriToPortname(uri), 0);
        }
    }

    /**
     * Registers the provided results using the callback interface and returns a
     * map of registered references to the results.
     * 
     * @param callback
     *            - Taverna callback interface
     * @param results
     *            - Map of results to register
     * @return A map of results
     */
    protected Map<String, T2Reference> registerOutputs(final AsynchronousActivityCallback callback,
        HashMap<MeasurementInfoUri, Value> results) {

        if (logger.isDebugEnabled()) {
            logger.debug("Registering outputs");
        }

        InvocationContext context = callback.getContext();
        ReferenceService referenceService = context.getReferenceService();

        Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();

        for (Entry<MeasurementInfoUri, Value> entry : results.entrySet()) {
            T2Reference reference = null;
            if (null == entry.getValue()) {
                // Register an ErrorDocument instead of the value if value is
                // null
                if (logger.isDebugEnabled()) {
                    logger.debug("Measurement " + convertUriToPortname(entry.getKey()) + " was not evaluated");
                }
                ErrorDocument errorDoc = referenceService.getErrorDocumentService().registerError(
                    "Measurement was not evaluated", 0, context);
                reference = referenceService.register(errorDoc, 0, true, context);
            } else if (null == entry.getValue().getFormattedValue()) {
                // Register ErrorDocument with comment from evaluator
                if (logger.isDebugEnabled()) {
                    logger.debug("Measurement " + convertUriToPortname(entry.getKey()) + " returned null: "
                        + entry.getValue().getComment());
                }
                ErrorDocument errorDoc = referenceService.getErrorDocumentService().registerError(
                    "Measurement returned null: " + entry.getValue().getComment(), 0, context);
                reference = referenceService.register(errorDoc, 0, true, context);
            } else {
                // Register value
                reference = referenceService.register(entry.getValue().toString(), 0, true, context);
            }
            outputs.put(convertUriToPortname(entry.getKey()), reference);
        }

        return outputs;

    }

    /**
     * Converts a URI to a valid portname
     * 
     * @param uri
     *            - The URI to convert
     * @return a portname
     */
    protected String convertUriToPortname(MeasurementInfoUri uri) {
        String portname = uri.getPath().replace('/', '.');
        if (!uri.getFragment().equals("")) {
            portname += "." + uri.getFragment();
        }
        return portname;
    }

}
