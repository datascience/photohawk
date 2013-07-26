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

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import net.sf.taverna.t2.invocation.InvocationContext;
import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;

import at.ac.tuwien.photohawk.evaluation.colorconverter.AutoColorConverter;
import at.ac.tuwien.photohawk.evaluation.operation.TransientOperation;
import at.ac.tuwien.photohawk.evaluation.operation.metric.EqualMetric;
import at.ac.tuwien.photohawk.evaluation.util.ConvenientBufferedImageWrapper;

/**
 * Activity that runs SSIM.
 */
public class EqualActivity extends AbstractActivity<CommonActivityConfigurationBean> implements
    AsynchronousActivity<CommonActivityConfigurationBean> {

    private static Logger logger = Logger.getLogger(EqualActivity.class);

    /**
     * Port names.
     */
    private static final String OUT = "equals";

    /**
     * Reconfigure ports of activity.
     */
    @Override
    protected void configurePorts() {
        super.configurePorts();

        // Output ports
        addOutput(OUT, 0);
    }

    @Override
    public void executeAsynch(final Map<String, T2Reference> inputs, final AsynchronousActivityCallback callback) {
        callback.requestRun(new Runnable() {
            public void run() {
                logger.info("Activity started");

                // Read images
                BufferedImage[] images = readImages(inputs, callback);
                if (images == null) {
                    return;
                }

                // Process images
                Map<String, T2Reference> outputs = null;
                if (images[0].getWidth() != images[1].getWidth() || images[0].getHeight() != images[1].getHeight()) {
                    logger.debug("Images have different size");
                    InvocationContext context = callback.getContext();
                    ReferenceService referenceService = context.getReferenceService();
                    outputs = new HashMap<String, T2Reference>();

                    T2Reference aggregatedRef = referenceService.register(new Boolean(false), 0, true, context);
                    outputs.put(OUT, aggregatedRef);
                } else {
                    ConvenientBufferedImageWrapper wrapped1 = new ConvenientBufferedImageWrapper(images[0]);
                    ConvenientBufferedImageWrapper wrapped2 = new ConvenientBufferedImageWrapper(images[1]);
                    AutoColorConverter c1 = new AutoColorConverter(wrapped1, wrapped2,
                        AutoColorConverter.AlternativeColorConverter.CIEXYZ);
                    AutoColorConverter c2 = new AutoColorConverter(wrapped2, wrapped1,
                        AutoColorConverter.AlternativeColorConverter.CIEXYZ);

                    // Evaluate
                    EqualMetric equal = new EqualMetric(c1, c2, new Point(0, 0), new Point(images[0].getWidth(),
                        images[0].getHeight()));

                    TransientOperation<Boolean, Boolean> op = equal.execute();
                    outputs = registerOutputs(callback, op);
                }

                // Return results
                callback.receiveResult(outputs, new int[0]);
            }
        });
    }

    /**
     * Registers the outputs.
     * 
     * @param callback
     *            callback of the activity
     * @param op
     *            results
     * @return a map of output port names and output references
     */
    private Map<String, T2Reference> registerOutputs(final AsynchronousActivityCallback callback,
        TransientOperation<Boolean, Boolean> op) {
        logger.debug("Registering outputs");

        InvocationContext context = callback.getContext();
        ReferenceService referenceService = context.getReferenceService();
        Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();

        // Result
        Boolean result = op.getAggregatedResult();
        T2Reference ref = referenceService.register(result.toString(), 0, true, context);
        outputs.put(OUT, ref);

        return outputs;
    }
}
