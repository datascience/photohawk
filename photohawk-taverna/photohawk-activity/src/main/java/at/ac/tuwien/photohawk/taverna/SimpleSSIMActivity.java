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
import java.util.Map;

import org.apache.log4j.Logger;

import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;

import at.ac.tuwien.photohawk.evaluation.colorconverter.StaticColor;
import at.ac.tuwien.photohawk.evaluation.colorconverter.hsb.HSBColorConverter;
import at.ac.tuwien.photohawk.evaluation.colorconverter.srgb.SRGBColorConverter;
import at.ac.tuwien.photohawk.evaluation.operation.TransientOperation;
import at.ac.tuwien.photohawk.evaluation.operation.metric.SimpleSSIMMetric;
import at.ac.tuwien.photohawk.evaluation.preprocessing.ScaleToNearestFactorPreprocessor;
import at.ac.tuwien.photohawk.evaluation.preprocessing.ShrinkResizePreprocessor;
import at.ac.tuwien.photohawk.evaluation.util.ConvenientBufferedImageWrapper;

/**
 * Activity that runs SSIM.
 */
public class SimpleSSIMActivity extends AbstractActivity<SimpleSSIMActivityConfigurationBean> implements
    AsynchronousActivity<SimpleSSIMActivityConfigurationBean> {

    private static Logger logger = Logger.getLogger(SimpleSSIMActivity.class);

    /**
     * Port names.
     */
    private static final String OUT_AGGREGATED = "SSIM";
    private static final String OUT_CHANNELS = "SSIM_Channels";
    private static final String OUT_CHANNEL_NAMES = "SSIM_ChannelNames";

    private static final int DEFAULT_SCALE_TARGET_SIZE = 256;

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

                // Read images
                BufferedImage[] images = readImages(inputs, callback);
                if (images == null) {
                    return;
                }

                // Convert to SRGB
                SRGBColorConverter srgbImage1 = new SRGBColorConverter(new ConvenientBufferedImageWrapper(images[0]));
                SRGBColorConverter srgbImage2 = new SRGBColorConverter(new ConvenientBufferedImageWrapper(images[1]));
                images[0] = srgbImage1.getImage().getBufferedImage();
                images[1] = srgbImage2.getImage().getBufferedImage();

                // Resize
                ShrinkResizePreprocessor shrink = new ShrinkResizePreprocessor(images[0], images[1]);
                shrink.process();
                images[0] = shrink.getResult1();
                images[1] = shrink.getResult2();
                shrink = null;

                // Scale
                ScaleToNearestFactorPreprocessor scale = new ScaleToNearestFactorPreprocessor(images[0], images[1],
                    DEFAULT_SCALE_TARGET_SIZE);
                scale.process();
                images[0] = scale.getResult1();
                images[1] = scale.getResult2();
                scale = null;

                // Evaluate
                ConvenientBufferedImageWrapper wrapped1 = new ConvenientBufferedImageWrapper(images[0]);
                HSBColorConverter c1 = new HSBColorConverter(new SRGBColorConverter(wrapped1));
                ConvenientBufferedImageWrapper wrapped2 = new ConvenientBufferedImageWrapper(images[1]);
                HSBColorConverter c2 = new HSBColorConverter(new SRGBColorConverter(wrapped2));

                // TODO: What happens if one image is smaller than the
                // SCALE_TARGET_SIZE?
                SimpleSSIMMetric ssim = new SimpleSSIMMetric(c1, c2, new Point(0, 0), new Point(images[0].getWidth(),
                    images[0].getHeight()));

                TransientOperation<Float, StaticColor> op = ssim.execute();
                Map<String, T2Reference> outputs = registerOutputs(callback, op, OUT_AGGREGATED, OUT_CHANNELS,
                    OUT_CHANNEL_NAMES);

                // Return map of output data, with empty index array as this is
                // the only and final result (this index parameter is used if
                // pipelining output)
                callback.receiveResult(outputs, new int[0]);
            }
        });
    }
}
