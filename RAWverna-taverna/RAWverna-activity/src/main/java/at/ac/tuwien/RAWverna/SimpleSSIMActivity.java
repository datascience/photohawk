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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;

import net.sf.taverna.t2.invocation.InvocationContext;
import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.ReferenceServiceException;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.processor.activity.AbstractAsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;

import org.apache.log4j.Logger;

import at.ac.tuwien.RAWverna.evaluation.EvaluatorException;
import at.ac.tuwien.RAWverna.evaluation.evaluators.JavaImageComparisonEvaluator;
import at.ac.tuwien.RAWverna.model.model.util.MeasurementInfoUri;
import at.ac.tuwien.RAWverna.model.model.values.Value;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.StaticColor;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.srgb.SRGBColorConverter;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.operation.TransientOperation;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.operation.metric.SimpleSSIMMetric;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.preprocessing.ScaleToNearestFactorPreprocessor;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.preprocessing.ShrinkResizePreprocessor;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.util.ConvenientBufferedImageWrapper;

public class SimpleSSIMActivity extends AbstractAsynchronousActivity<JavaImageEvaluatorActivityConfigurationBean>
    implements AsynchronousActivity<JavaImageEvaluatorActivityConfigurationBean> {

    private static Logger logger = Logger.getLogger(SimpleSSIMActivity.class);

    /**
     * Port names.
     */
    private static final String IN_IMAGE_1 = "image1";
    private static final String IN_IMAGE_2 = "image2";
    private static final String OUT_MEASURE = "ssim";

    private static final int DEFAULT_SCALE_TARGET_SIZE = 512;

    /**
     * Configuration of this instance.
     */
    private JavaImageEvaluatorActivityConfigurationBean configBean;

    private static Object lock = new Object();
    private static boolean pluginsScanned = false;

    @Override
    public void configure(JavaImageEvaluatorActivityConfigurationBean configBean) throws ActivityConfigurationException {
        // Store the current configuration
        this.configBean = configBean;

        // (Re)create input/output ports depending on configuration
        configurePorts();
    }

    /**
     * Reconfigure ports of activity.
     */
    protected void configurePorts() {
        // In case we are being reconfigured - remove existing ports first
        // to avoid duplicates
        removeInputs();
        removeOutputs();

        // Input ports
        addInput(IN_IMAGE_1, 0, true, null, byte[].class);
        addInput(IN_IMAGE_2, 0, true, null, byte[].class);

        // Output ports
        addOutput(OUT_MEASURE, 0);
    }

    @Override
    public void executeAsynch(final Map<String, T2Reference> inputs, final AsynchronousActivityCallback callback) {
        // Don't execute service directly now, request to be run
        // from thread pool and return asynchronously
        callback.requestRun(new Runnable() {

            public void run() {
                logger.info("Activity started");

                // Get context and reference service
                InvocationContext context = callback.getContext();
                ReferenceService referenceService = context.getReferenceService();

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

                // Evaluate Images
                ShrinkResizePreprocessor shrink = new ShrinkResizePreprocessor(image1, image2);
                shrink.process();
                image1 = shrink.getResult1();
                image2 = shrink.getResult2();

                ScaleToNearestFactorPreprocessor scale = new ScaleToNearestFactorPreprocessor(image1, image1,
                    DEFAULT_SCALE_TARGET_SIZE);
                scale.process();
                image1 = shrink.getResult1();
                image2 = shrink.getResult2();

                ConvenientBufferedImageWrapper wrapped1 = new ConvenientBufferedImageWrapper(image1);
                SRGBColorConverter c1 = new SRGBColorConverter(wrapped1);
                ConvenientBufferedImageWrapper wrapped2 = new ConvenientBufferedImageWrapper(image2);
                SRGBColorConverter c2 = new SRGBColorConverter(wrapped2);

                // TODO: What happens if one image is smaller than the
                // SCALE_TARGET_SIZE?
                SimpleSSIMMetric ssim = new SimpleSSIMMetric(c1, c2, new Point(0, 0), new Point(image1.getWidth(),
                    image1.getWidth()));

                TransientOperation<Float, StaticColor> op = ssim.execute();
                Map<String, T2Reference> outputs = registerOutputs(callback, op);

                // Return map of output data, with empty index array as this is
                // the only and final result (this index parameter is used if
                // pipelining output)
                callback.receiveResult(outputs, new int[0]);
            }
        });
    }

    @Override
    public JavaImageEvaluatorActivityConfigurationBean getConfiguration() {
        return this.configBean;
    }

    /**
     * Reads the data from input port specified by the provided reference from
     * the callback context and wraps it into a BufferedImage object.
     * 
     * @param callback
     *            the callback environment
     * @param reference
     *            the reference of the input port
     * @return the imagedata wrapped in a BufferedImage
     */
    private BufferedImage wrapInputImage(AsynchronousActivityCallback callback, T2Reference reference) {

        InvocationContext context = callback.getContext();
        ReferenceService referenceService = context.getReferenceService();

        BufferedImage image = null;
        try {
            logger.info("Wrapping image data in BufferedImage");

            // Get image data bytearray
            byte[] imageData = (byte[]) referenceService.renderIdentifier(reference, byte[].class, context);

            if (null == imageData) {
                logger.error("Image data is null");
                callback.fail("Image data is null");
                return null;
            }

            if (0 == imageData.length) {
                logger.error("Image data length is 0");
                callback.fail("Image data length is 0");
                return null;
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Image data has size " + imageData.length + " bytes");
            }

            // Create inputstream from bytearray
            ByteArrayInputStream is = new ByteArrayInputStream(imageData);

            // Scan for plugins
            synchronized (lock) {
                if (!pluginsScanned) {
                    pluginsScanned = true;
                    // Scan for ImageIO plugins
                    logger.debug("Scanning for ImageIO plugins");
                    ImageIO.scanForPlugins();
                }
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Finding ImageReaders capable of decoding the image");
                ImageInputStream iis = new MemoryCacheImageInputStream(is);
                Iterator<ImageReader> decoders = ImageIO.getImageReaders(iis);
                while (decoders.hasNext()) {
                    ImageReader ir = decoders.next();
                    logger.debug(ir.getClass());
                }
                is.reset();
            }

            // Wrap image
            image = ImageIO.read(is);

            if (null == image) {
                logger.error("BufferdImage is null, probably could not find an ImageReader able to decode the data");
                callback.fail("BufferdImage is null, probably could not find an ImageReader able to decode the data");
                return null;
            }

            logger.info("Image read with size " + image.getWidth() + "x" + image.getHeight());
        } catch (ReferenceServiceException e) {
            logger.error("Could not get image data from reference service", e);
            callback.fail("JavaImageEvalutatorActivity: Could not get image data from reference service", e);
            return null;
        } catch (IOException e) {
            logger.error("Could not wrap input data into BufferdImage", e);
            callback.fail("JavaImageEvalutatorActivity: Could not wrap input data into BufferdImage", e);
            return null;
        }

        return image;

    }

    private Map<String, T2Reference> registerOutputs(final AsynchronousActivityCallback callback,
        TransientOperation<Float, StaticColor> op) {
        logger.debug("Registering outputs");

        InvocationContext context = callback.getContext();
        ReferenceService referenceService = context.getReferenceService();

        Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();

        T2Reference reference = null;

        Float aggregatedResult = op.getAggregatedResult();

        reference = referenceService.register(aggregatedResult.toString(), 0, true, context);
        outputs.put(OUT_MEASURE, reference);

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
