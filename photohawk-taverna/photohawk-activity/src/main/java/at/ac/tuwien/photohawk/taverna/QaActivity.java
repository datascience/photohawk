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

import at.ac.tuwien.photohawk.evaluation.colorconverter.StaticColor;
import at.ac.tuwien.photohawk.evaluation.operation.TransientOperation;
import net.sf.taverna.t2.invocation.InvocationContext;
import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.ReferenceServiceException;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.processor.activity.AbstractAsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class QaActivity<T> extends AbstractAsynchronousActivity<T> implements AsynchronousActivity<T> {

    /**
     * Port names.
     */
    protected static final String IN_IMAGE_1 = "image1";
    protected static final String IN_IMAGE_2 = "image2";
    private static Logger logger = Logger.getLogger(QaActivity.class);
    private static Object lock = new Object();
    private static boolean pluginsScanned = false;
    /**
     * Configuration of this instance.
     */
    private T configBean;

    @Override
    public void configure(T configBean) throws ActivityConfigurationException {
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
    }

    @Override
    public T getConfiguration() {
        return this.configBean;
    }

    /**
     * Reads the input images from the provided inputs.
     *
     * @param inputs   the inputs of the activity
     * @param callback callback
     * @return an array with IN_IMAGE_1 and IN_IMAGE_2 in this order or null if
     * one of the images could not be read
     */
    protected BufferedImage[] readImages(final Map<String, T2Reference> inputs,
                                         final AsynchronousActivityCallback callback) {

        BufferedImage[] images = new BufferedImage[2];

        // Wrap image 1
        logger.info("Loading image on port " + IN_IMAGE_1);
        images[0] = wrapInputImage(callback, inputs.get(IN_IMAGE_1));
        if (null == images[0]) {
            logger.warn("Could not read image on port " + IN_IMAGE_1);
            callback.fail("Equals: Could not read image on port " + IN_IMAGE_1);
            return null;
        }

        // Wrap image 2
        logger.info("Loading image on port " + IN_IMAGE_2);
        images[1] = wrapInputImage(callback, inputs.get(IN_IMAGE_2));
        if (null == images[1]) {
            logger.warn("Could not read image on port " + IN_IMAGE_2);
            callback.fail("Equals: Could not read image on port " + IN_IMAGE_2);
            return null;
        }

        return images;
    }

    /**
     * Reads the data from input port specified by the provided reference from
     * the callback context and wraps it into a BufferedImage object.
     * <p/>
     * If one of the images could not be read, fails the activity.
     *
     * @param callback  the callback environment
     * @param reference the reference of the input port
     * @return the image data wrapped in a BufferedImage
     */
    protected BufferedImage wrapInputImage(AsynchronousActivityCallback callback, T2Reference reference) {

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

    /**
     * Registers the outputs.
     *
     * @param callback callback of the activity
     * @param op       results
     * @return a map of output port names and output references
     */
    protected Map<String, T2Reference> registerOutputs(final AsynchronousActivityCallback callback,
                                                       final TransientOperation<Float, StaticColor> op, final String aggregatedName, final String channelsName,
                                                       final String channelNamesName) {
        logger.debug("Registering outputs");

        InvocationContext context = callback.getContext();
        ReferenceService referenceService = context.getReferenceService();
        Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();

        // Aggregated result
        Float aggregatedResult = op.getAggregatedResult();
        T2Reference aggregatedRef = referenceService.register(aggregatedResult.toString(), 0, true, context);
        outputs.put(aggregatedName, aggregatedRef);

        // Channels
        StaticColor result = op.getResult();
        float[] resultValues = result.getChannelValues();
        T2Reference channelsRef = referenceService.register(resultValues, 1, true, context);
        outputs.put(channelsName, channelsRef);

        String[] resultDescriptions = result.getChannelDescription();
        T2Reference channelDescriptionRef = referenceService.register(resultDescriptions, 1, true, context);
        outputs.put(channelNamesName, channelDescriptionRef);

        return outputs;
    }
}
