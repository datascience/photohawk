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
package at.ac.tuwien.photohawk.taverna.evaluation.evaluators;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import at.ac.tuwien.photohawk.evaluation.colorconverter.AutoColorConverter;
import at.ac.tuwien.photohawk.evaluation.colorconverter.StaticColor;
import at.ac.tuwien.photohawk.evaluation.colorconverter.hsb.HSBColorConverter;
import at.ac.tuwien.photohawk.evaluation.colorconverter.srgb.SRGBColorConverter;
import at.ac.tuwien.photohawk.evaluation.operation.TransientOperation;
import at.ac.tuwien.photohawk.evaluation.operation.metric.AEMetric;
import at.ac.tuwien.photohawk.evaluation.operation.metric.EqualMetric;
import at.ac.tuwien.photohawk.evaluation.operation.metric.MAEMetric;
import at.ac.tuwien.photohawk.evaluation.operation.metric.MSEMetric;
import at.ac.tuwien.photohawk.evaluation.operation.metric.Metric;
import at.ac.tuwien.photohawk.evaluation.operation.metric.PAEMetric;
import at.ac.tuwien.photohawk.evaluation.operation.metric.SimpleSSIMMetric;
import at.ac.tuwien.photohawk.evaluation.util.ConvenientBufferedImageWrapper;
import at.ac.tuwien.photohawk.taverna.JavaImageEvaluatorActivity;
import at.ac.tuwien.photohawk.taverna.evaluation.EvaluatorException;
import at.ac.tuwien.photohawk.taverna.evaluation.IObjectEvaluator;
import at.ac.tuwien.photohawk.taverna.model.model.scales.Scale;
import at.ac.tuwien.photohawk.taverna.model.model.util.MeasurementInfoUri;
import at.ac.tuwien.photohawk.taverna.model.model.values.BooleanValue;
import at.ac.tuwien.photohawk.taverna.model.model.values.FloatValue;
import at.ac.tuwien.photohawk.taverna.model.model.values.PositiveFloatValue;
import at.ac.tuwien.photohawk.taverna.model.model.values.Value;

/**
 * This class provides some image comparison metrics as evaluator.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 */
public class JavaImageComparisonEvaluator extends EvaluatorBase implements IObjectEvaluator {

    private static Logger logger = Logger.getLogger(JavaImageEvaluatorActivity.class);

    private static final String DESCRIPTOR_FILE = "data/evaluation/measurementsImageCompJava.xml";

    public JavaImageComparisonEvaluator() {
        // Load information about measurements
        loadMeasurementsDescription(JavaImageComparisonEvaluator.DESCRIPTOR_FILE);
    }

    public HashMap<MeasurementInfoUri, Value> evaluate(BufferedImage image1, BufferedImage image2,
        List<MeasurementInfoUri> measurementInfoUris) throws EvaluatorException {

        HashMap<MeasurementInfoUri, Value> results = new HashMap<MeasurementInfoUri, Value>();

        ConvenientBufferedImageWrapper sampleImg = new ConvenientBufferedImageWrapper(image1);
        SRGBColorConverter sampleImgColor = null;
        ConvenientBufferedImageWrapper resultImg = new ConvenientBufferedImageWrapper(image2);
        SRGBColorConverter resultImgColor = null;

        // Reorder measurements to gain additional performance
        // "equal" will be the last metric if existing
        // some unsuited operations are removed
        List<MeasurementInfoUri> orderedMeasurements = new ArrayList<MeasurementInfoUri>();
        for (MeasurementInfoUri measurementInfoUri : measurementInfoUris) {
            String propertyURI = measurementInfoUri.getAsURI();
            String fragment = measurementInfoUri.getFragment();
            if ((propertyURI != null) && propertyURI.startsWith(OBJECT_IMAGE_SIMILARITY + "#")) {
                if (fragment.equals("equal")) {
                    orderedMeasurements.add(measurementInfoUri);
                } else {
                    orderedMeasurements.add(0, measurementInfoUri);
                }
            }
        }

        // Loop over mesurementInfoUris
        for (MeasurementInfoUri measurementInfoUri : orderedMeasurements) {
            String fragment = measurementInfoUri.getFragment();
            Scale scale = descriptor.getMeasurementScale(measurementInfoUri);

            if (scale == null) {
                // This means that I am not entitled to evaluate this
                // measurementInfo and therefore supposed to skip it.
                continue;
            }

            // Equal
            if (fragment.equals("equal")) {
                Point p0 = new Point(0, 0);
                Point p1 = getSize(sampleImg, resultImg);

                if (null == p1) {
                    // Image Size does not match
                    logger.error("Image Size does not match");

                    Value v = scale.createValue();
                    ((BooleanValue) v).bool(false);
                    v.setComment("Images are not the same as image size differs (" + sampleImg.getWidth() + "x"
                        + sampleImg.getHeight() + " vs. " + resultImg.getWidth() + "x" + resultImg.getHeight() + ")");

                    results.put(measurementInfoUri, v);

                    continue;
                }

                EqualMetric metric = null;
                metric = new EqualMetric(new AutoColorConverter(sampleImg, resultImg,
                    AutoColorConverter.AlternativeColorConverter.CIEXYZ), new AutoColorConverter(resultImg, sampleImg,
                    AutoColorConverter.AlternativeColorConverter.CIEXYZ), p0, p1);

                TransientOperation<Boolean, Boolean> op = metric.execute();
                Boolean b = op.getAggregatedResult();

                Value v = scale.createValue();
                ((BooleanValue) v).bool(b);
                v.setComment("Java ImageCompare returned images are equal: " + b);

                results.put(measurementInfoUri, v);

            } else {

                Metric metric = null;
                Value v = null;

                if (null == resultImgColor) {
                    resultImgColor = new SRGBColorConverter(resultImg);
                }
                if (null == sampleImgColor) {
                    sampleImgColor = new SRGBColorConverter(sampleImg);
                }

                if (fragment.startsWith("ssimSimple")) {

                    int selectChannel = -1;
                    if (fragment.equals("ssimSimpleHue")) {
                        selectChannel = 2;
                    } else if (fragment.equals("ssimSimpleSaturation")) {
                        selectChannel = 1;
                    } else if (fragment.equals("ssimSimple")) {
                    } else {
                        continue;
                    }

                    Point p0 = new Point(0, 0);
                    Point p1 = getTolerantSize(sampleImg, resultImg);

                    int dx = sampleImg.getWidth() - resultImg.getWidth();
                    int dy = sampleImg.getHeight() - resultImg.getHeight();

                    if (null == p1) {
                        // Image Size does not match
                        logger.error("Image Size does not match");
                        continue;
                    }

                    TransientOperation<Float, StaticColor> op;

                    ConvenientBufferedImageWrapper scaledSample = new ConvenientBufferedImageWrapper(
                        SimpleSSIMMetric.prepare(sampleImgColor.getImage().getBufferedImage(), Math.max(0, dx),
                            Math.max(0, dy), p1.x, p1.y, 512));
                    ConvenientBufferedImageWrapper scaledResult = new ConvenientBufferedImageWrapper(
                        SimpleSSIMMetric.prepare(resultImgColor.getImage().getBufferedImage(), Math.max(0, -dx),
                            Math.max(0, -dy), p1.x, p1.y, 512));

                    metric = new SimpleSSIMMetric(new HSBColorConverter(new SRGBColorConverter(scaledSample)),
                        new HSBColorConverter(new SRGBColorConverter(scaledResult)), p0, new Point(
                            scaledSample.getWidth(), scaledSample.getHeight()));

                    op = metric.execute();

                    Double d;
                    if (-1 == selectChannel) {
                        d = Double.valueOf(op.getAggregatedResult());
                    } else {
                        d = Double.valueOf(op.getResult().getChannelValue(selectChannel));
                    }

                    // System.out.println(op.getResult());

                    v = scale.createValue();
                    ((FloatValue) v).setValue(d);
                    v.setComment("Java ImageCompare returned " + Double.toString(d) + " for " + fragment);

                    results.put(measurementInfoUri, v);

                } else {

                    Point p0 = new Point(0, 0);
                    Point p1 = getSize(sampleImg, resultImg);

                    if (null == p1) {
                        // Image Size does not match
                        logger.error("Image Size does not match");
                        continue;
                    }

                    int selectChannel = -1;

                    if (fragment.equals("aeRelative")) {
                        metric = new AEMetric(sampleImgColor, resultImgColor, p0, p1);
                    } else if (fragment.equals("paeRelative")) {
                        metric = new PAEMetric(sampleImgColor, resultImgColor, p0, p1);
                    } else if (fragment.equals("maeRelative")) {
                        metric = new MAEMetric(sampleImgColor, resultImgColor, p0, p1);
                    } else if (fragment.equals("mseRelative")) {
                        metric = new MSEMetric(sampleImgColor, resultImgColor, p0, p1);
                    }
                    /*-
                    else if (fragment.equals("mseRelativeHue")) {
                    	metric = new MSEMetric(new HSBColorConverter(sampleImgColor), new HSBColorConverter(resultImgColor), p0, p1);
                    	selectChannel = 2;
                    }else if (fragment.equals("maeRelativeHue")) {
                    	metric = new MAEMetric(new HSBColorConverter(sampleImgColor), new HSBColorConverter(resultImgColor), p0, p1);
                    	selectChannel = 2;
                    }
                     */

                    if (null != metric) {
                        TransientOperation<Float, StaticColor> op = metric.execute();
                        Double d;

                        if (-1 == selectChannel) {
                            d = Double.valueOf(op.getAggregatedResult());
                        } else {
                            d = Double.valueOf(op.getResult().getChannelValue(selectChannel));
                        }

                        // System.out.println(op.getResult());

                        v = scale.createValue();
                        ((PositiveFloatValue) v).setValue(d);
                        v.setComment("Java ImageCompare returned " + Double.toString(d) + " for " + fragment);

                        results.put(measurementInfoUri, v);
                    }

                }
            }

            // App.endMeasureTime(id, "End Measurement");

        }

        return results;

    }

    private Point getSize(ConvenientBufferedImageWrapper img1, ConvenientBufferedImageWrapper img2) {
        if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
            return null;
        } else {
            return new Point(img1.getWidth(), img1.getHeight());
        }
    }

    private Point getTolerantSize(ConvenientBufferedImageWrapper img1, ConvenientBufferedImageWrapper img2) {
        return new Point(Math.min(img1.getWidth(), img2.getWidth()), Math.min(img1.getHeight(), img2.getHeight()));
    }
}
