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
import at.ac.tuwien.photohawk.evaluation.preprocessing.PreprocessingException;
import at.ac.tuwien.photohawk.evaluation.qa.Qa;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;
import org.apache.log4j.Logger;

import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * Abstract activity for XAE metrics.
 */
public abstract class MultichannelActivity<T> extends QaActivity<T> {

    private static Logger logger = Logger.getLogger(MultichannelActivity.class);

    /**
     * Port names.
     */
    private String outAggregated;
    private String outChannels;
    private String outChannelNames;

    public MultichannelActivity(final String outAggregated, final String outChannels, final String outChannelNames) {
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

    /**
     * Executes the provided QA on the provided images and registeres the results in the callback.
     *
     * @param qa       the QA to execute
     * @param image1   the left image to process
     * @param image2   the right image to process
     * @param callback the callback to use
     */
    protected void executeQa(final Qa<Float, StaticColor> qa, final BufferedImage image1, final BufferedImage image2, final AsynchronousActivityCallback callback) {
        try {
            // Evaluate
            TransientOperation<Float, StaticColor> op = qa.evaluate(image1, image2);
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
