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

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

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

public abstract class AbstractActivity<T> extends AbstractAsynchronousActivity<T> implements AsynchronousActivity<T> {

    private static Logger logger = Logger.getLogger(AbstractActivity.class);

    /**
     * Port names.
     */
    protected static final String IN_IMAGE_1 = "image1";
    protected static final String IN_IMAGE_2 = "image2";

    /**
     * Configuration of this instance.
     */
    private T configBean;

    private static Object lock = new Object();
    private static boolean pluginsScanned = false;

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
     * Reads the data from input port specified by the provided reference from
     * the callback context and wraps it into a BufferedImage object.
     * 
     * @param callback
     *            the callback environment
     * @param reference
     *            the reference of the input port
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
}
