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
package at.ac.tuwien.RAWverna;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import net.sf.taverna.t2.invocation.InvocationContext;
import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;

import org.apache.log4j.Logger;

import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.StaticColor;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.hsb.HSBColorConverter;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.srgb.SRGBColorConverter;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.operation.TransientOperation;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.operation.metric.SimpleSSIMMetric;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.preprocessing.ScaleToNearestFactorPreprocessor;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.preprocessing.ShrinkResizePreprocessor;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.util.ConvenientBufferedImageWrapper;

/**
 * Activity that runs SSIM.
 */
public class SimpleSSIMActivity extends AbstractActivity<SimpleSSIMActivityConfigurationBean> implements
    AsynchronousActivity<SimpleSSIMActivityConfigurationBean> {

    private static Logger logger = Logger.getLogger(SimpleSSIMActivity.class);

    /**
     * Port names.
     */
    private static final String OUT_AGGREGATED = "ssim";
    private static final String OUT_CHANNELS = "ssimChannels";
    private static final String OUT_CHANNEL_NAMES = "ssimChannelNames";

    private static final int DEFAULT_SCALE_TARGET_SIZE = 512;

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
                    callback.fail("JavaImageEvalutatorActivity: 2 Could not read image on port " + IN_IMAGE_1);
                    logger.error("Could not read image on port " + IN_IMAGE_1);
                    return;
                }

                // Wrap image 2
                logger.info("Loading image on port " + IN_IMAGE_2);
                BufferedImage image2 = wrapInputImage(callback, inputs.get(IN_IMAGE_2));
                if (null == image2) {
                    callback.fail("JavaImageEvalutatorActivity: 2 Could not read image on port " + IN_IMAGE_2);
                    logger.error("Could not read image on port " + IN_IMAGE_2);
                    return;
                }

                // Convert to SRGB
                SRGBColorConverter srgbImage1 = new SRGBColorConverter(new ConvenientBufferedImageWrapper(image1));
                SRGBColorConverter srgbImage2 = new SRGBColorConverter(new ConvenientBufferedImageWrapper(image2));
                image1 = srgbImage1.getImage().getBufferedImage();
                image2 = srgbImage2.getImage().getBufferedImage();

                // Resize
                ShrinkResizePreprocessor shrink = new ShrinkResizePreprocessor(image1, image2);
                shrink.process();
                image1 = shrink.getResult1();
                image2 = shrink.getResult2();
                shrink = null;

                // Scale
                ScaleToNearestFactorPreprocessor scale = new ScaleToNearestFactorPreprocessor(image1, image1,
                    DEFAULT_SCALE_TARGET_SIZE);
                scale.process();
                image1 = scale.getResult1();
                image2 = scale.getResult2();
                scale = null;

                // Evaluate
                ConvenientBufferedImageWrapper wrapped1 = new ConvenientBufferedImageWrapper(image1);
                HSBColorConverter c1 = new HSBColorConverter(new SRGBColorConverter(wrapped1));
                ConvenientBufferedImageWrapper wrapped2 = new ConvenientBufferedImageWrapper(image2);
                HSBColorConverter c2 = new HSBColorConverter(new SRGBColorConverter(wrapped2));

                // TODO: What happens if one image is smaller than the
                // SCALE_TARGET_SIZE?
                SimpleSSIMMetric ssim = new SimpleSSIMMetric(c1, c2, new Point(0, 0), new Point(image1.getWidth(),
                    image1.getHeight()));

                TransientOperation<Float, StaticColor> op = ssim.execute();
                Map<String, T2Reference> outputs = registerOutputs(callback, op);

                // Return map of output data, with empty index array as this is
                // the only and final result (this index parameter is used if
                // pipelining output)
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
        TransientOperation<Float, StaticColor> op) {
        logger.debug("Registering outputs");

        InvocationContext context = callback.getContext();
        ReferenceService referenceService = context.getReferenceService();
        Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();

        // Aggregated result
        Float aggregatedResult = op.getAggregatedResult();
        T2Reference aggregatedRef = referenceService.register(aggregatedResult.toString(), 0, true, context);
        outputs.put(OUT_AGGREGATED, aggregatedRef);

        // Channels
        StaticColor result = op.getResult();
        float[] resultValues = result.getChannelValues();
        T2Reference channelsRef = referenceService.register(resultValues, 1, true, context);
        outputs.put(OUT_CHANNELS, channelsRef);

        String[] resultDescriptions = result.getChannelDescription();
        T2Reference channelDescriptionRef = referenceService.register(resultDescriptions, 1, true, context);
        outputs.put(OUT_CHANNEL_NAMES, channelDescriptionRef);

        // // Register ErrorDocument with comment from evaluator
        // if (logger.isDebugEnabled()) {
        // logger.debug("Measurement " + convertUriToPortname(entry.getKey()) +
        // " returned null: "
        // + entry.getValue().getComment());
        // }
        // ErrorDocument errorDoc =
        // referenceService.getErrorDocumentService().registerError(
        // "Measurement returned null: " + entry.getValue().getComment(), 0,
        // context);
        // reference = referenceService.register(errorDoc, 0, true, context);

        return outputs;
    }
}
