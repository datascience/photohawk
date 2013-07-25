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

import at.ac.tuwien.photohawk.evaluation.colorconverter.StaticColor;
import at.ac.tuwien.photohawk.evaluation.colorconverter.srgb.SRGBColorConverter;
import at.ac.tuwien.photohawk.evaluation.operation.TransientOperation;
import at.ac.tuwien.photohawk.evaluation.operation.metric.AEMetric;
import at.ac.tuwien.photohawk.evaluation.util.ConvenientBufferedImageWrapper;

/**
 * Activity that runs SSIM.
 */
public class AEErrorActivity extends AbstractActivity<CommonActivityConfigurationBean> implements
    AsynchronousActivity<CommonActivityConfigurationBean> {

    private static Logger logger = Logger.getLogger(AEErrorActivity.class);

    /**
     * Port names.
     */
    private static final String OUT_AGGREGATED = "AE";
    private static final String OUT_CHANNELS = "AE_Channels";
    private static final String OUT_CHANNEL_NAMES = "SSIM_ChannelNames";

    /**
     * Reconfigure ports of activity.
     */
    @Override
    protected void configurePorts() {
        super.configurePorts();

        // Output ports
        addOutput(OUT_AGGREGATED, 0);
        addOutput(OUT_CHANNELS, 1);
        addOutput(OUT_CHANNEL_NAMES, 1);
    }

    @Override
    public void executeAsynch(final Map<String, T2Reference> inputs, final AsynchronousActivityCallback callback) {
        callback.requestRun(new Runnable() {
            public void run() {
                logger.info("Activity started");

                // Wrap image 1
                logger.info("Loading image on port " + IN_IMAGE_1);
                BufferedImage image1 = wrapInputImage(callback, inputs.get(IN_IMAGE_1));
                if (null == image1) {
                    callback.fail("Equals: Could not read image on port " + IN_IMAGE_1);
                    logger.error("Could not read image on port " + IN_IMAGE_1);
                    return;
                }

                // Wrap image 2
                logger.info("Loading image on port " + IN_IMAGE_2);
                BufferedImage image2 = wrapInputImage(callback, inputs.get(IN_IMAGE_2));
                if (null == image2) {
                    callback.fail("Equals: Could not read image on port " + IN_IMAGE_2);
                    logger.error("Could not read image on port " + IN_IMAGE_2);
                    return;
                }

                Map<String, T2Reference> outputs = null;
                if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight()) {
                    logger.debug("Images have different size");
                    InvocationContext context = callback.getContext();
                    ReferenceService referenceService = context.getReferenceService();
                    outputs = new HashMap<String, T2Reference>();

                    T2Reference aggregatedRef = referenceService.register(new Boolean(false), 0, true, context);
                    outputs.put(OUT_AGGREGATED, aggregatedRef);
                } else {
                    SRGBColorConverter c1 = new SRGBColorConverter(new ConvenientBufferedImageWrapper(image1));
                    SRGBColorConverter c2 = new SRGBColorConverter(new ConvenientBufferedImageWrapper(image2));

                    // Evaluate
                    AEMetric ae = new AEMetric(c1, c2, new Point(0, 0),
                        new Point(image1.getWidth(), image1.getHeight()));

                    TransientOperation<Float, StaticColor> op = ae.execute();
                    outputs = registerOutputs(callback, op, OUT_AGGREGATED, OUT_CHANNELS, OUT_CHANNEL_NAMES);
                }

                // Return map of output data, with empty index array as this is
                // the only and final result (this index parameter is used if
                // pipelining output)
                callback.receiveResult(outputs, new int[0]);
            }
        });
    }
}
