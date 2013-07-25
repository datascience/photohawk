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

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import at.ac.tuwien.photohawk.taverna.evaluation.EvaluatorException;
import at.ac.tuwien.photohawk.taverna.evaluation.IObjectEvaluator;
import at.ac.tuwien.photohawk.taverna.evaluation.evaluators.javafitsevaluator.XmlExtractor;
import at.ac.tuwien.photohawk.taverna.model.model.scales.Scale;
import at.ac.tuwien.photohawk.taverna.model.model.util.FloatFormatter;
import at.ac.tuwien.photohawk.taverna.model.model.util.MeasurementInfoUri;
import at.ac.tuwien.photohawk.taverna.model.model.values.BooleanValue;
import at.ac.tuwien.photohawk.taverna.model.model.values.Value;

public class JavaFITSEvaluator extends EvaluatorBase implements IObjectEvaluator {
    private static final String FITS_COMPRESSIONSCHEME_UNCOMPRESSED = "Uncompressed";
    private static final String NAME = "FITS/Jhove/Exiftool";
    private static final String SOURCE = "\n- extracted by " + NAME;

    private static final String DESCRIPTOR_FILE = "data/evaluation/measurementsFITS.xml";

    public JavaFITSEvaluator() {
        // load information about measurements
        loadMeasurementsDescription(DESCRIPTOR_FILE);
    }

    public HashMap<MeasurementInfoUri, Value> evaluate(String fitsXML1, String fitsXML2,
        List<MeasurementInfoUri> measurementInfoUris) throws EvaluatorException {

        FloatFormatter formatter = new FloatFormatter();

        HashMap<MeasurementInfoUri, Value> results = new HashMap<MeasurementInfoUri, Value>();

        XmlExtractor extractor = new XmlExtractor();
        if ((fitsXML1 != null) && (fitsXML2 != null)) {
            // so we have a fits xml, lets analyse it:
            try {
                StringReader reader = new StringReader(fitsXML1);
                Document fitsDocResult = extractor.getDocument(new InputSource(reader));
                reader = new StringReader(fitsXML2);
                Document fitsDocSample = extractor.getDocument(new InputSource(reader));

                String sampleImageCompressionScheme = extractor.extractText(fitsDocSample,
                    "//fits:compressionScheme/text()");
                String resultImageCompressionScheme = extractor.extractText(fitsDocResult,
                    "//fits:compressionScheme/text()");

                for (MeasurementInfoUri measurementInfoUri : measurementInfoUris) {
                    Value v = null;
                    String propertyURI = measurementInfoUri.getAsURI();
                    Scale scale = descriptor.getMeasurementScale(measurementInfoUri);
                    if (scale == null) {
                        // This means that I am not entitled to evaluate this
                        // measurementInfo and therefore supposed to skip it:
                        continue;
                    }
                    if (OBJECT_FORMAT_CORRECT_WELLFORMED.equals(propertyURI)) {
                        v = extractor.extractValue(fitsDocResult, scale,
                            "//fits:well-formed[@status='SINGLE_RESULT']/text()",
                            "//fits:filestatus/fits:message/text()");
                    } else if (OBJECT_FORMAT_CORRECT_VALID.equals(propertyURI)) {
                        v = extractor.extractValue(fitsDocResult, scale,
                            "//fits:filestatus/fits:valid[@status='SINGLE_RESULT']/text()",
                            "//fits:filestatus/fits:message/text()");
                    }
                    if (OBJECT_COMPRESSION_SCHEME.equals(propertyURI)) {
                        v = extractor.extractValue(fitsDocResult, scale, "//fits:compressionScheme/text()", null);
                    }

                    if ((v != null) && (v.getComment() == null || "".equals(v.getComment()))) {
                        v.setComment(SOURCE);
                        results.put(measurementInfoUri, v);
                        // this leaf has been processed
                        continue;
                    }

                    if (OBJECT_FORMAT_CORRECT_CONFORMS.equals(propertyURI)) {
                        // TODO: What do?
                        // if (alternative.getAction() != null) {
                        String puid = "UNDEFINED";
                        // FormatInfo info =
                        // alternative.getAction().getTargetFormatInfo();
                        // if (info != null) {
                        // puid = info.getPuid();
                        // }
                        String fitsText = extractor.extractText(fitsDocResult,
                            "//fits:externalIdentifier[@type='puid']/text()");
                        v = identicalValues(puid, fitsText, scale);
                        // }
                    } else if ((OBJECT_IMAGE_DIMENSION_WIDTH + "#equal").equals(propertyURI)) {
                        String sampleValue = extractor.extractText(fitsDocSample, "//fits:imageWidth/text()");
                        String resultValue = extractor.extractText(fitsDocResult, "//fits:imageWidth/text()");
                        v = identicalValues(sampleValue, resultValue, scale);
                    } else if ((OBJECT_IMAGE_DIMENSION_HEIGHT + "#equal").equals(propertyURI)) {
                        String sampleValue = extractor.extractText(fitsDocSample, "//fits:imageHeight/text()");
                        String resultValue = extractor.extractText(fitsDocResult, "//fits:imageHeight/text()");
                        v = identicalValues(sampleValue, resultValue, scale);
                    } else if ((OBJECT_IMAGE_DIMENSION_ASPECTRATIO + "#equal").equals(propertyURI)) {
                        try {
                            int sampleHeight = Integer.parseInt(extractor.extractText(fitsDocSample,
                                "//fits:imageHeight/text()"));
                            int resultHeight = Integer.parseInt(extractor.extractText(fitsDocResult,
                                "//fits:imageHeight/text()"));
                            int sampleWidth = Integer.parseInt(extractor.extractText(fitsDocSample,
                                "//fits:imageWidth/text()"));
                            int resultWidth = Integer.parseInt(extractor.extractText(fitsDocResult,
                                "//fits:imageWidth/text()"));

                            double sampleRatio = sampleHeight / sampleWidth;
                            double resultRatio = resultHeight / resultWidth;
                            v = scale.createValue();
                            ((BooleanValue) v).bool(0 == Double.compare(sampleRatio, resultRatio));
                            v.setComment(String.format("Reference value: %s\nActual value: %s",
                                formatter.formatFloat(sampleRatio), formatter.formatFloat(resultRatio)));
                        } catch (NumberFormatException e) {
                            // not all values are available - aspectRatio cannot
                            // be calculated
                        }
                    } else if ((OBJECT_COMPRESSION_SCHEME + "#equal").equals(propertyURI)) {
                        v = identicalValues(sampleImageCompressionScheme, resultImageCompressionScheme, scale);
                    } else if (OBJECT_COMPRESSION_LOSSLESS.equals(propertyURI)) {
                        // At the moment we only handle compression schemes of
                        // images
                        if ((resultImageCompressionScheme != null) && (!"".equals(resultImageCompressionScheme))) {
                            v = scale.createValue();
                            ((BooleanValue) v).bool(FITS_COMPRESSIONSCHEME_UNCOMPRESSED
                                .equals(resultImageCompressionScheme));
                        }
                    } else if (OBJECT_COMPRESSION_LOSSY.equals(propertyURI)) {
                        // At the moment we only handle compression schemes of
                        // images
                        if ((resultImageCompressionScheme != null) && (!"".equals(resultImageCompressionScheme))) {
                            v = scale.createValue();
                            ((BooleanValue) v).bool(!FITS_COMPRESSIONSCHEME_UNCOMPRESSED
                                .equals(resultImageCompressionScheme));
                        }
                    } else if ((OBJECT_IMAGE_COLORENCODING_BITSPERSAMPLE + "#equal").equals(propertyURI)) {
                        String sampleValue = extractor.extractText(fitsDocSample, "//fits:bitsPerSample/text()");
                        String resultValue = extractor.extractText(fitsDocResult, "//fits:bitsPerSample/text()");
                        v = identicalValues(sampleValue, resultValue, scale);
                    } else if ((OBJECT_IMAGE_COLORENCODING_SAMPLESPERPIXEL + "#equal").equals(propertyURI)) {
                        String sampleValue = extractor.extractText(fitsDocSample, "//fits:samplesPerPixel/text()");
                        String resultValue = extractor.extractText(fitsDocResult, "//fits:samplesPerPixel/text()");
                        v = identicalValues(sampleValue, resultValue, scale);
                    } else if ((OBJECT_IMAGE_PHOTOMETRICINTERPRETATION_COLORSPACE + "#equal").equals(propertyURI)) {
                        String sampleValue = extractor.extractText(fitsDocSample, "//fits:colorSpace/text()");
                        String resultValue = extractor.extractText(fitsDocResult, "//fits:colorSpace/text()");
                        v = identicalValues(sampleValue, resultValue, scale);
                    } else if ((OBJECT_IMAGE_PHOTOMETRICINTERPRETATION_COLORPROFILE_ICCPROFILE + "#equal")
                        .equals(propertyURI)) {
                        String sampleValue = extractor.extractText(fitsDocSample, "//fits:iccProfileName/text()");
                        String resultValue = extractor.extractText(fitsDocResult, "//fits:iccProfileName/text()");
                        v = identicalValues(sampleValue, resultValue, scale);
                    } else if ((OBJECT_IMAGE_SPATIALMETRICS_SAMPLINGFREQUENCYUNIT + "#equal").equals(propertyURI)) {
                        String sampleValue = extractor
                            .extractText(fitsDocSample, "//fits:samplingFrequencyUnit/text()");
                        String resultValue = extractor
                            .extractText(fitsDocResult, "//fits:samplingFrequencyUnit/text()");
                        v = identicalValues(sampleValue, resultValue, scale);
                    } else if ((OBJECT_IMAGE_SPATIALMETRICS_XSAMPLINGFREQUENCY + "#equal").equals(propertyURI)) {
                        String sampleValue = extractor.extractText(fitsDocSample, "//fits:xSamplingFrequency/text()");
                        String resultValue = extractor.extractText(fitsDocResult, "//fits:xSamplingFrequency/text()");
                        v = identicalValues(sampleValue, resultValue, scale);
                    } else if ((OBJECT_IMAGE_SPATIALMETRICS_YSAMPLINGFREQUENCY + "#equal").equals(propertyURI)) {
                        String sampleValue = extractor.extractText(fitsDocSample, "//fits:ySamplingFrequency/text()");
                        String resultValue = extractor.extractText(fitsDocResult, "//fits:ySamplingFrequency/text()");
                        v = identicalValues(sampleValue, resultValue, scale);

                    } else if ((OBJECT_IMAGE_METADATA + "#preserved").equals(propertyURI)) {
                        // FIXME is using metric "preserved" ok?!
                        HashMap<String, String> sampleMetadata = extractor.extractValues(fitsDocSample,
                            "//fits:exiftool/*[local-name() != 'rawdata']");
                        HashMap<String, String> resultMetadata = extractor.extractValues(fitsDocResult,
                            "//fits:exiftool/*[local-name() != 'rawdata']");
                        v = preservedValues(sampleMetadata, resultMetadata, scale);
                    } else if ((OBJECT_IMAGE_METADATA_PRODUCER + "#equal").equals(propertyURI)) {
                        String sampleValue = extractor.extractText(fitsDocSample,
                            "//fits:ImageCreation/ImageProducer/text()");
                        String resultValue = extractor.extractText(fitsDocResult,
                            "//fits:ImageCreation/ImageProducer/text()");
                        v = identicalValues(sampleValue, resultValue, scale);
                    } else if ((OBJECT_IMAGE_METADATA_SOFTWARE + "#equal").equals(propertyURI)) {
                        String sampleValue = extractor.extractText(fitsDocSample,
                            "//fits:creatingApplicationName/text()");
                        String resultValue = extractor.extractText(fitsDocResult,
                            "//fits:creatingApplicationName/text()");
                        v = identicalValues(sampleValue, resultValue, scale);
                    } else if ((OBJECT_IMAGE_METADATA_CREATIONDATE + "#equal").equals(propertyURI)) {
                        String sampleValue = extractor.extractText(fitsDocSample,
                            "//fits:ImageCreation/DateTimeCreated/text()");
                        String resultValue = extractor.extractText(fitsDocResult,
                            "//fits:ImageCreation/DateTimeCreated/text()");
                        v = identicalValues(sampleValue, resultValue, scale);
                    } else if ((OBJECT_IMAGE_METADATA_LASTMODIFIED + "#equal").equals(propertyURI)) {
                        String sampleValue = extractor
                            .extractText(fitsDocSample, "//fits:fileinfo/lastmodified/text()");
                        String resultValue = extractor
                            .extractText(fitsDocResult, "//fits:fileinfo/lastmodified/text()");
                        v = identicalValues(sampleValue, resultValue, scale);
                    } else if ((OBJECT_IMAGE_METADATA_DESCRIPTION + "#equal").equals(propertyURI)) {
                        String sampleValue = extractor.extractText(fitsDocSample,
                            "//fits:exiftool/ImageDescription/text()");
                        String resultValue = extractor.extractText(fitsDocResult,
                            "//fits:exiftool/ImageDescription/text()");
                        v = identicalValues(sampleValue, resultValue, scale);
                    } else if ((OBJECT_IMAGE_METADATA_ORIENTATION + "#equal").equals(propertyURI)) {
                        String sampleValue = extractor.extractText(fitsDocSample, "//fits:exiftool/Orientation/text()");
                        String resultValue = extractor.extractText(fitsDocResult, "//fits:exiftool/Orientation/text()");
                        v = identicalValues(sampleValue, resultValue, scale);
                    }

                    if (v != null) {
                        v.setComment(v.getComment() + SOURCE);
                        results.put(measurementInfoUri, v);
                        // listener.updateStatus(String.format("%s: evaluated measurement: %s = %s",
                        // NAME,
                        // measurementInfoUri.getAsURI(), v.toString()));
                    } else {
                        // listener.updateStatus(String.format("%s: no evaluator found for measurement: %s",
                        // NAME,
                        // measurementInfoUri.getAsURI()));
                    }
                }
            } catch (IOException e) {
                // listener.updateStatus(" - could not read FITS xml");
            } catch (SAXException e) {
                // listener.updateStatus(" - invalid FITS xml found");
            } catch (ParserConfigurationException e) {
                // listener.updateStatus(" - invalid FITS xml found");
            }
        } else {
            // listener.updateStatus(" - no FITS xml found");
        }
        return results;
    }

    private Value preservedValues(HashMap<String, String> sampleMetadata, HashMap<String, String> resultMetadata,
        Scale scale) {
        int numMissing = 0;
        int numChanged = 0;
        BooleanValue v = (BooleanValue) scale.createValue();
        StringBuilder comment = new StringBuilder();
        for (String key : sampleMetadata.keySet()) {
            String sampleValue = sampleMetadata.get(key);
            String resultValue = resultMetadata.get(key);
            if (resultValue == null) {
                numMissing++;
                comment.append(" - " + key + "\n");
            } else if (!resultValue.equals(sampleValue)) {
                numChanged++;
                comment.append(" ~ " + key + ": sample=" + sampleValue + ", result=" + resultValue + "\n");
            }
        }
        if ((numChanged == 0) && (numMissing == 0)) {
            v.bool(true);
            v.setComment("result contains complete metadata of sample");
        } else {
            v.bool(false);
            comment.insert(0, "following differences found: (- .. missing, ~ .. altered):\n");
            v.setComment(comment.toString());
        }
        return v;
    }

    private Value identicalValues(String v1, String v2, Scale s) {
        BooleanValue bv = (BooleanValue) s.createValue();
        String s1 = (v1 == null) ? "UNDEFINED" : v1;
        String s2 = (v2 == null) ? "UNDEFINED" : v2;

        if (!"UNDEFINED".equals(s1) && !"UNDEFINED".equals(s2)) {
            // both values are defined:
            if (s1.equals(s2)) {
                bv.bool(true);
                bv.setComment("Both have value " + s1);
            } else {
                bv.bool(false);
                bv.setComment("Reference value: " + s1 + "\nActual value: " + s2);
            }
        } else if (s1.equals(s2)) {
            // both are undefined :
            bv.setComment("Both values are UNDEFINED");
            // bv.setValue("");
        } else {
            // one value is undefined:
            bv.setComment("Reference value: " + s1 + "\nActual value: " + s2);
        }
        return bv;
    }
}
