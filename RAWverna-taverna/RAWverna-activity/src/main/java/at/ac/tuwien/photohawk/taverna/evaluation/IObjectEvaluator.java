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
package at.ac.tuwien.photohawk.taverna.evaluation;

public interface IObjectEvaluator extends IEvaluator {
    /**
     * relative filesize (measure, positive number)
     */
    String OBJECT_FORMAT_RELATIVEFILESIZE = "outcome://object/relativeFileSize";
    /**
     * is the format wellformed, valid, conforms ? (measure, boolean)
     */
    String OBJECT_FORMAT_CORRECT_WELLFORMED = "outcome://object/format/correct/wellformed";
    String OBJECT_FORMAT_CORRECT_VALID = "outcome://object/format/correct/valid";
    String OBJECT_FORMAT_CORRECT_CONFORMS = "outcome://object/format/correct/conforms";

    /**
     * compression scheme (measure, free text) compression scheme#equal (derived
     * measure, boolean) more derived measures with XCL
     */
    String OBJECT_COMPRESSION_SCHEME = "outcome://object/compression/scheme";
    /**
     * is this sample stored lossless, or with a lossy compression? (measure,
     * boolean)
     */
    String OBJECT_COMPRESSION_LOSSLESS = "outcome://object/compression/lossless";
    String OBJECT_COMPRESSION_LOSSY = "outcome://object/compression/lossy";

    /**
     * width and height of the image (derived measure: #equal, boolean) - more
     * derived with XCL
     */
    String OBJECT_IMAGE_DIMENSION_WIDTH = "outcome://object/image/dimension/width";
    String OBJECT_IMAGE_DIMENSION_HEIGHT = "outcome://object/image/dimension/height";
    /**
     * aspect ratio of image height/ width (derived measure: #equal, boolean)
     */
    String OBJECT_IMAGE_DIMENSION_ASPECTRATIO = "outcome://object/image/dimension/aspectRatio";

    /**
     * bits per sample (derived measure: #equal, boolean) - more derived with
     * XCL
     */
    String OBJECT_IMAGE_COLORENCODING_BITSPERSAMPLE = "outcome://object/image/colorEncoding/bitsPerSample";
    /**
     * samplesPerPixel (derived measure: #equal, boolean) - more derived with
     * XCL
     */
    String OBJECT_IMAGE_COLORENCODING_SAMPLESPERPIXEL = "outcome://object/image/colorEncoding/samplesPerPixel";
    String OBJECT_IMAGE_PHOTOMETRICINTERPRETATION_COLORSPACE = "outcome://object/image/photometricInterpretation/colorSpace";
    String OBJECT_IMAGE_PHOTOMETRICINTERPRETATION_COLORPROFILE_ICCPROFILE = "outcome://object/image/photometricInterpretation/colorProfile/iccProfile";

    String OBJECT_IMAGE_SPATIALMETRICS_SAMPLINGFREQUENCYUNIT = "outcome://object/image/spatialMetrics/samplingFrequencyUnit";
    String OBJECT_IMAGE_SPATIALMETRICS_XSAMPLINGFREQUENCY = "outcome://object/image/spatialMetrics/xSamplingFrequency";
    String OBJECT_IMAGE_SPATIALMETRICS_YSAMPLINGFREQUENCY = "outcome://object/image/spatialMetrics/ySamplingFrequency";

    String OBJECT_IMAGE_METADATA = "outcome://object/image/metadata";
    // exif: artist
    String OBJECT_IMAGE_METADATA_PRODUCER = "outcome://object/image/metadata/producer";
    String OBJECT_IMAGE_METADATA_SOFTWARE = "outcome://object/image/metadata/software";
    // exif: DateTime
    String OBJECT_IMAGE_METADATA_CREATIONDATE = "outcome://object/image/metadata/creationDate";
    // exif: FileModifyDate/ModifyDate
    String OBJECT_IMAGE_METADATA_LASTMODIFIED = "outcome://object/image/metadata/lastModified";
    String OBJECT_IMAGE_METADATA_DESCRIPTION = "outcome://object/image/metadata/description";
    String OBJECT_IMAGE_METADATA_ORIENTATION = "outcome://object/image/metadata/orientation";

    String OBJECT_IMAGE_SIMILARITY = "outcome://object/image/similarity";

    /**
     * 
     */
    String OBJECT_ACTION_RUNTIME_PERFORMANCE_TIME_PERSAMPLE = "action://runtime/performance/time/perSample";

    /**
     * time per MB is defined msec/MB (measure, positive number)
     */
    String OBJECT_ACTION_RUNTIME_PERFORMANCE_TIME_PERMB = "action://runtime/performance/time/perMB";

    /**
     * throughput is defined in MB per second (measure, positive number)
     */
    String OBJECT_ACTION_RUNTIME_PERFORMANCE_THROUGHPUT = "action://runtime/performance/throughput";

    /**
     * Memory per MB (of the sample object's size) is defined (measure, positive
     * number)
     */
    String OBJECT_ACTION_RUNTIME_PERFORMANCE_MEMORY_PERMB = "action://runtime/performance/memory/perMB";

    /**
     * Memory per Sample (measure, positive number)
     */
    String OBJECT_ACTION_RUNTIME_PERFORMANCE_MEMORY_PERSAMPLE = "action://runtime/performance/memory/perSample";

    /**
     * Memory peak (measure, positive number)
     */
    // TODO: not evaluated yet
    String OBJECT_ACTION_RUNTIME_PERFORMANCE_MEMORY_PEAK = "action://runtime/performance/memory/peak";

    String OBJECT_ACTION_ACTIVITYLOGGING_FORMAT = "action://runtime/activityLogging/format";
    String OBJECT_ACTION_ACTIVITYLOGGING_AMOUNT = "action://runtime/activityLogging/amount";

    /**
     * evaluates result and sample object with regard to the given critera
     * defined in leaves returns a list of values, one per leaf
     * 
     * It is not nice that leaves are passed to the evaluator, and a map of
     * leaves to values is returned
     * 
     * This information is really needed: - how this criterion is measured
     * (MeasurementInfo) - what is type of the evaluated value (Scale)
     * 
     * @param alternative
     * @param sample
     * @param result
     * @param measurementInfoUris
     * @param listener
     * @return
     * @throws EvaluatorException
     */
    // public HashMap<MeasurementInfoUri, Value> evaluate(BufferedImage image1,
    // BufferedImage image2,
    // List<MeasurementInfoUri> measurementInfoUris) throws EvaluatorException;

}
