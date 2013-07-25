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
package at.ac.tuwien.photohawk.taverna;

import java.util.HashMap;
import java.util.Map;

import net.sf.taverna.t2.invocation.InvocationContext;
import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;
import at.ac.tuwien.photohawk.taverna.evaluation.EvaluatorException;
import at.ac.tuwien.photohawk.taverna.evaluation.evaluators.JavaFITSEvaluator;
import at.ac.tuwien.photohawk.taverna.measurementuri.MeasurementURIActivity;
import at.ac.tuwien.photohawk.taverna.model.model.util.MeasurementInfoUri;
import at.ac.tuwien.photohawk.taverna.model.model.values.Value;

public class JavaFITSEvaluatorActivity extends MeasurementURIActivity<JavaFITSEvaluatorActivityConfigurationBean>
    implements AsynchronousActivity<JavaFITSEvaluatorActivityConfigurationBean> {

    /**
     * Port names
     */
    static final String IN_FITS_1 = "fitsXML1";
    static final String IN_FITS_2 = "fitsXML2";

    /**
     * Configuration of this instance
     */
    private JavaFITSEvaluatorActivityConfigurationBean configBean;

    @Override
    public void configure(JavaFITSEvaluatorActivityConfigurationBean configBean) throws ActivityConfigurationException {
        // Store for getConfiguration(), but you could also make
        // getConfiguration() return a new bean from other sources
        this.configBean = configBean;

        // Get measurements from config
        setMeasurementUris(configBean);

        // (Re)create input/output ports depending on configuration
        configurePorts();
    }

    @Override
    protected void configurePorts() {
        // Measurement URI ports
        super.configurePorts();

        // Input ports
        addInput(IN_FITS_1, 0, true, null, String.class);
        addInput(IN_FITS_2, 0, true, null, String.class);
    }

    @Override
    public void executeAsynch(final Map<String, T2Reference> inputs, final AsynchronousActivityCallback callback) {
        // Don't execute service directly now, request to be run ask to be run
        // from thread pool and return asynchronously
        callback.requestRun(new Runnable() {

            public void run() {
                logger.info("Activity started");

                InvocationContext context = callback.getContext();
                ReferenceService referenceService = context.getReferenceService();

                // Resolve inputs
                String fits1 = (String) referenceService.renderIdentifier(inputs.get(IN_FITS_1), String.class, context);
                String fits2 = (String) referenceService.renderIdentifier(inputs.get(IN_FITS_2), String.class, context);

                if (logger.isInfoEnabled()) {
                    logger.info("Image paths: " + fits1 + ", " + fits2);
                }

                // Evaluate FITS
                JavaFITSEvaluator evaluator = new JavaFITSEvaluator();
                HashMap<MeasurementInfoUri, Value> results = new HashMap<MeasurementInfoUri, Value>();

                try {
                    results = evaluator.evaluate(fits1, fits2, measurementInfoUris);
                } catch (EvaluatorException e) {
                    logger.error("Error evaluating " + IN_FITS_2 + " and " + IN_FITS_2, e);
                    callback
                        .fail("JavaImageEvalutatorActivity: Error evaluating " + IN_FITS_2 + " and " + IN_FITS_2, e);
                    return;
                }

                // Register outputs
                Map<String, T2Reference> outputs = registerOutputs(callback, results);

                // Return map of output data, with empty index array as this is
                // the only and final result (this index parameter is used if
                // pipelining output)
                callback.receiveResult(outputs, new int[0]);
            }
        });
    }

    @Override
    public JavaFITSEvaluatorActivityConfigurationBean getConfiguration() {
        return this.configBean;
    }

}
