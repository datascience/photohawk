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

import at.ac.tuwien.photohawk.evaluation.colorconverter.srgb.SRGBColorConverter;
import at.ac.tuwien.photohawk.evaluation.operation.metric.MAEMetric;
import at.ac.tuwien.photohawk.evaluation.util.ConvenientBufferedImageWrapper;

/**
 * Activity that runs MAE.
 */
public class MAEActivity extends XAEErrorActivity<CommonActivityConfigurationBean> implements
    AsynchronousActivity<CommonActivityConfigurationBean> {

    private static Logger logger = Logger.getLogger(MAEActivity.class);

    /**
     * Port names.
     */
    private static final String OUT_AGGREGATED = "MAE";
    private static final String OUT_CHANNELS = "MAE_Channels";
    private static final String OUT_CHANNEL_NAMES = "MAE_ChannelNames";

    public MAEActivity() {
        super(OUT_AGGREGATED, OUT_CHANNELS, OUT_CHANNEL_NAMES);
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

                MAEMetric metric = new MAEMetric(new SRGBColorConverter(new ConvenientBufferedImageWrapper(images[0])),
                    new SRGBColorConverter(new ConvenientBufferedImageWrapper(images[1])), new Point(0, 0), new Point(
                        images[0].getWidth(), images[0].getHeight()));

                executeMetric(metric, images[0], images[1], callback);
            }
        });
    }
}
