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
import java.util.Map;

import org.apache.log4j.Logger;

import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;

import at.ac.tuwien.photohawk.evaluation.colorconverter.StaticColor;
import at.ac.tuwien.photohawk.evaluation.operation.TransientOperation;
import at.ac.tuwien.photohawk.evaluation.operation.metric.Metric;
import at.ac.tuwien.photohawk.evaluation.preprocessing.CheckEqualSizePreprocessor;
import at.ac.tuwien.photohawk.evaluation.preprocessing.PreprocessingException;

/**
 * Abstract activity for XAE metrics.
 */
public abstract class XAEErrorActivity<T> extends AbstractActivity<T> {

    private static Logger logger = Logger.getLogger(XAEErrorActivity.class);

    /**
     * Port names.
     */
    private String outAggregated;
    private String outChannels;
    private String outChannelNames;

    public XAEErrorActivity(String outAggregated, String outChannels, String outChannelNames) {
        super();
        this.outAggregated = outAggregated;
        this.outChannels = outChannels;
        this.outChannelNames = outChannelNames;
    }

    /**
     * Reconfigure ports of activity.
     */
    @Override
    protected void configurePorts() {
        super.configurePorts();

        // Output ports
        addOutput(outAggregated, 0);
        addOutput(outChannels, 1);
        addOutput(outChannelNames, 1);
    }

    protected void executeMetric(final Metric metric, final BufferedImage image1, final BufferedImage image2,
        final AsynchronousActivityCallback callback) {

        BufferedImage[] images = new BufferedImage[] {image1, image2};

        try {
            // Check size
            CheckEqualSizePreprocessor equalSize = new CheckEqualSizePreprocessor(images[0], images[1]);
            equalSize.process();
            images[0] = equalSize.getResult1();
            images[1] = equalSize.getResult2();
            equalSize = null;

            // Evaluate
            TransientOperation<Float, StaticColor> op = metric.execute();
            Map<String, T2Reference> outputs = registerOutputs(callback, op, outAggregated, outChannels,
                outChannelNames);

            // Return results
            callback.receiveResult(outputs, new int[0]);
        } catch (PreprocessingException e) {
            logger.info("Image size does not match");
            callback.fail("Image size does not match");
        }
    }
}
