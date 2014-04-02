/*******************************************************************************
 * Copyright 2010-2013 Vienna University of Technology
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
package at.ac.tuwien.photohawk.evaluation.operation.metric;

import at.ac.tuwien.photohawk.evaluation.colorconverter.ColorConverter;
import at.ac.tuwien.photohawk.evaluation.colorconverter.StaticColor;
import at.ac.tuwien.photohawk.evaluation.colorconverter.hsb.HSBColorConverter;
import at.ac.tuwien.photohawk.evaluation.operation.OperationException;
import at.ac.tuwien.photohawk.evaluation.operation.TransientOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.util.concurrent.*;

/**
 * This class implements a simple SSIM Metric. Based on: Z. Wang, A. C. Bovik,
 * H. R. Sheikh and E. P. Simoncelli,
 * "Image quality assessment: From error visibility to structural similarity,"
 * IEEE Transactions on Image Processing, vol. 13, no. 4, pp. 600-612, Apr.
 * 2004.
 *
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 */
public class SimpleSSIMMetric extends Metric {

    /**
     * Default target size of the image.
     */
    public static final int DEFAULT_TARGET_SIZE = 256;

    /**
     * Default thread pool size.
     */
    public static final int DEFAULT_THREADPOOL_SIZE = 0;
    private int threadPoolSize = DEFAULT_THREADPOOL_SIZE;
    private static final int BLOCK_SIZE = 11;
    private static final Logger LOGGER = LogManager.getLogger(SimpleSSIMMetric.class);
    private static final int[] SIGNIFICANT_BITS_PER_COMPONENT = new int[]{16, 16, 16};
    private static final double K1 = 0.01d;
    private static final double K2 = 0.03d;
    // Gaussian window 11x11 with std 1.5
    private static final double[] WINDOW = new double[]{1.05756559815326e-06, 7.81441153305360e-06, 3.70224770827489e-05, 0.000112464355116679, 0.000219050652866017, 0.000273561160085806, 0.000219050652866017, 0.000112464355116679, 3.70224770827489e-05, 7.81441153305360e-06, 1.05756559815326e-06, 7.81441153305360e-06, 5.77411251978637e-05, 0.000273561160085806, 0.000831005429087199, 0.00161857756253439, 0.00202135875836257, 0.00161857756253439, 0.000831005429087199, 0.000273561160085806, 5.77411251978637e-05, 7.81441153305360e-06, 3.70224770827489e-05, 0.000273561160085806, 0.00129605559384320, 0.00393706926284679, 0.00766836382523672, 0.00957662749024029, 0.00766836382523672, 0.00393706926284679, 0.00129605559384320, 0.000273561160085806, 3.70224770827489e-05, 0.000112464355116679, 0.000831005429087199, 0.00393706926284679, 0.0119597604100370, 0.0232944324734871, 0.0290912256485504, 0.0232944324734871, 0.0119597604100370, 0.00393706926284679, 0.000831005429087199, 0.000112464355116679, 0.000219050652866017, 0.00161857756253439, 0.00766836382523672, 0.0232944324734871, 0.0453713590956603, 0.0566619704916846, 0.0453713590956603, 0.0232944324734871, 0.00766836382523672, 0.00161857756253439, 0.000219050652866017, 0.000273561160085806, 0.00202135875836257, 0.00957662749024029, 0.0290912256485504, 0.0566619704916846, 0.0707622377639470, 0.0566619704916846, 0.0290912256485504, 0.00957662749024029, 0.00202135875836257, 0.000273561160085806, 0.000219050652866017, 0.00161857756253439, 0.00766836382523672, 0.0232944324734871, 0.0453713590956603, 0.0566619704916846, 0.0453713590956603, 0.0232944324734871, 0.00766836382523672, 0.00161857756253439, 0.000219050652866017, 0.000112464355116679, 0.000831005429087199, 0.00393706926284679, 0.0119597604100370, 0.0232944324734871, 0.0290912256485504, 0.0232944324734871, 0.0119597604100370, 0.00393706926284679, 0.000831005429087199, 0.000112464355116679, 3.70224770827489e-05, 0.000273561160085806, 0.00129605559384320, 0.00393706926284679, 0.00766836382523672, 0.00957662749024029, 0.00766836382523672, 0.00393706926284679, 0.00129605559384320, 0.000273561160085806, 3.70224770827489e-05, 7.81441153305360e-06, 5.77411251978637e-05, 0.000273561160085806, 0.000831005429087199, 0.00161857756253439, 0.00202135875836257, 0.00161857756253439, 0.000831005429087199, 0.000273561160085806, 5.77411251978637e-05, 7.81441153305360e-06, 1.05756559815326e-06, 7.81441153305360e-06, 3.70224770827489e-05, 0.000112464355116679, 0.000219050652866017, 0.000273561160085806, 0.000219050652866017, 0.000112464355116679, 3.70224770827489e-05, 7.81441153305360e-06, 1.05756559815326e-06};

    /**
     * Creates a new SimpleSSIMMetric with the provided parameters and a
     * blocksize of {@link #BLOCK_SIZE}.
     *
     * @param img1  color converter of image 1
     * @param img2  color converter of image 2
     * @param start start of comparison
     * @param end   end of comparison
     */
    public SimpleSSIMMetric(ColorConverter<?> img1, ColorConverter<?> img2, Point start, Point end) {
        super(img1, img2, start, end);
    }

    /**
     * Creates a new SimpleSSIMMetric with the provided parameters and a
     * blocksize of {@link #BLOCK_SIZE}.
     *
     * @param img1           color converter of image 1
     * @param img2           color converter of image 2
     * @param start          start of comparison
     * @param end            end of comparison
     * @param threadPoolSize number of threads to use
     */
    public SimpleSSIMMetric(final ColorConverter<?> img1, final ColorConverter<?> img2, final Point start,
                            final Point end, final int threadPoolSize) {
        this(img1, img2, start, end);
        this.threadPoolSize = threadPoolSize;
    }

    /**
     * Prepares the provided image for this metric.
     * <p/>
     * Uses
     * {@link SimpleSSIMMetric#prepare(BufferedImage, int, int, int, int, int)}
     * with the current image width and height and a target size of
     * {@link #DEFAULT_TARGET_SIZE}.
     *
     * @param img the image to prepare
     * @return the original image if no transformation was done or a new
     * transformed image otherwise
     */
    @Deprecated
    public static BufferedImage prepare(BufferedImage img) {
        return SimpleSSIMMetric.prepare(img, 0, 0, img.getWidth(), img.getHeight(), DEFAULT_TARGET_SIZE);
    }

    /**
     * Prepares the provided image for structured similarity metric.
     * <p/>
     * If the image width or height is larger than the provided width or height,
     * creates a new image with the specified width and height and positions the
     * image in the center.
     * <p/>
     * Then scales the image's smaller side to the provided targetSize keeping
     * the aspect ratio using
     * {@link java.awt.image.AffineTransformOp#TYPE_BICUBIC bicubic
     * interpolation}.
     *
     * @param img        the image to prepare
     * @param x
     * @param y
     * @param width      the maximum image width
     * @param height     the maximum image height
     * @param targetSize the maximum size of the smaller image side.
     * @return the original image if no transformation was done or a new
     * transformed image otherwise
     */
    @Deprecated
    public static BufferedImage prepare(BufferedImage img, int x, int y, int width, int height, int targetSize) {
        BufferedImage result = img;

        if (img.getWidth() > width || img.getHeight() > height) {
            LOGGER.info("Scaling image to width {}, height {}.", width, height);
            AffineTransformOp temp = new AffineTransformOp(
                    AffineTransform.getScaleInstance(width / (double) img.getWidth(),
                                                     height / (double) img.getHeight()), AffineTransformOp.TYPE_BICUBIC
            );
            BufferedImage result1 = temp.createCompatibleDestImage(img, new ComponentColorModel(
                                                                           img.getColorModel().getColorSpace(),
                                                                           SIGNIFICANT_BITS_PER_COMPONENT,
                                                                           img.getColorModel().hasAlpha(),
                                                                           img.getColorModel().isAlphaPremultiplied(),
                                                                           img.getColorModel().getTransparency(),
                                                                           DataBuffer.TYPE_INT)
            );

            Graphics2D graphics1 = result1.createGraphics();
            graphics1.drawImage(img, -(img.getWidth() - width) / 2, -(img.getHeight() - height) / 2, null);

            result = result1;
        }
        img = null;

        // Scale image if necessary
        int invfactor = Math.max(1, Math.round(Math.min(result.getWidth(), result.getHeight()) / (float) targetSize));
        float factor = 1.0f / invfactor;
        if (factor < 0.95) {
            LOGGER.info("Scaling image by factor {}.", factor);
            // Blur might be applied in future versions
            /*-
            System.out.println("Blur by " + invfactor);

            float[] data = new float[invfactor * invfactor];
            Arrays.fill(data, factor * factor);
            ConvolveOp blur = new ConvolveOp(new Kernel(invfactor, invfactor, data), ConvolveOp.EDGE_NO_OP, null);
            BufferedImage result1 = blur.createCompatibleDestImage(img, new ComponentColorModel(img.getColorModel().getColorSpace(), new int[] { 16, 16, 16 }, img.getColorModel().hasAlpha(), img
            .getColorModel().isAlphaPremultiplied(), img.getColorModel().getTransparency(), DataBuffer.TYPE_INT));
            BufferedImage result1 = blur.filter(img, result1);
             */

            AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(factor, factor),
                                                         AffineTransformOp.TYPE_BICUBIC);
            BufferedImage result2 = op.createCompatibleDestImage(result, new ComponentColorModel(
                                                                         result.getColorModel().getColorSpace(),
                                                                         SIGNIFICANT_BITS_PER_COMPONENT,
                                                                         result.getColorModel().hasAlpha(),
                                                                         result.getColorModel().isAlphaPremultiplied(),
                                                                         result.getColorModel().getTransparency(),
                                                                         DataBuffer.TYPE_INT)
            );

            // result = op.filter(img, result);

            AffineTransform scale = AffineTransform.getScaleInstance(factor, factor);
            Graphics2D graphics = result2.createGraphics();
            graphics.setTransform(scale);
            graphics.drawImage(result, 0, 0, null);

            result = result2;
        }
        return result;
    }

    @Override
    public SSIMTransientOperation prepare() {
        return new SSIMTransientOperation();
    }

    @Override
    public TransientOperation<Float, StaticColor> execute() {
        SSIMTransientOperation op = this.prepare();

        op.init();

        if (end.x - start.x < BLOCK_SIZE || end.y - start.y < BLOCK_SIZE) {
            throw new IllegalArgumentException("Metric comparison size is smaller than block size.");
        }

        for (int x = start.x + BLOCK_SIZE / 2; x < end.x - BLOCK_SIZE / 2; x += 2) {
            for (int y = start.y + BLOCK_SIZE / 2; y < end.y - BLOCK_SIZE / 2; y += 2) {
                int[] xValues = new int[BLOCK_SIZE * BLOCK_SIZE];
                int[] yValues = new int[BLOCK_SIZE * BLOCK_SIZE];
                int counter = 0;
                for (int i = -BLOCK_SIZE / 2; i < (BLOCK_SIZE + 1) / 2; i++) {
                    for (int j = -BLOCK_SIZE / 2; j < (BLOCK_SIZE + 1) / 2; j++) {
                        xValues[counter] = x + i;
                        yValues[counter] = y + j;
                        counter++;
                    }
                }
                op.execute(xValues, yValues);
            }
        }

        op.complete();
        return op;
    }

    /**
     * Checks if the execution should be threaded.
     *
     * @return true if threaded, false otherwise
     */
    private boolean doThreaded() {
        return threadPoolSize > 0;
    }

    /**
     * Transient operation that implements a simple Structured Similarity
     * Metric.
     */
    public class SSIMTransientOperation extends MetricTransientOperation {
        private double[] ssimTotal;
        private StaticColor realssimTotal;
        private int blocks;
        private ExecutorService pool;
        private ExecutorCompletionService<Double[]> service;

        @Override
        public void init() {
            if (doThreaded()) {
                LOGGER.debug("Threaded execution enabled, creating pool");
                this.pool = Executors.newFixedThreadPool(threadPoolSize);
                this.service = new ExecutorCompletionService<Double[]>(pool);
            }

            this.ssimTotal = new double[img1.getNumberOfChannels()];

            this.realssimTotal = img1.getNullColor();
            this.blocks = 0;
        }

        @Override
        public void complete() {
            if (doThreaded()) {
                LOGGER.debug("Shutting down thread pool");
                pool.shutdown();

                LOGGER.debug("Retrieving results from threads");
                for (int i = 0; i < blocks; i++) {
                    try {
                        Double[] data = service.take().get();
                        for (int counter = 0; counter < img1.getNumberOfChannels(); counter++) {
                            ssimTotal[counter] += data[counter];
                        }
                    } catch (InterruptedException e) {
                        LOGGER.error("Retrieving result of block [{}] interrupted", i, e);
                        Thread.currentThread().interrupt();
                        throw new OperationException("Retrieving result of block " + i + " interrupted");
                    } catch (ExecutionException e) {
                        LOGGER.error("Execution of block [{}] had an exception", i, e);
                        throw new OperationException("Execution of block " + i + " had an exception");
                    }
                }
            }

            float[] realssimTotalFloat = new float[img1.getNumberOfChannels()];
            for (int counter = 0; counter < img1.getNumberOfChannels(); counter++) {
                realssimTotalFloat[counter] = (float) (ssimTotal[counter] / blocks);
            }
            this.realssimTotal.setChannelValues(realssimTotalFloat);
        }

        @Override
        public void execute(int[] x, int[] y) {
            if (x.length != BLOCK_SIZE * BLOCK_SIZE || y.length != BLOCK_SIZE * BLOCK_SIZE) {
                throw new IllegalArgumentException("Data size doesn't match blocksize");
            }

            blocks++;

            if (doThreaded()) {
                LOGGER.debug("Threaded execution enabled, submitting callable.");
                service.submit(new ThreadedSSIM(x, y));
            } else {
                Double[] data;
                try {
                    data = new ThreadedSSIM(x, y).call();
                    for (int counter = 0; counter < img1.getNumberOfChannels(); counter++) {
                        ssimTotal[counter] += data[counter];
                    }
                } catch (Exception e) {
                    LOGGER.error("Error running calculating block [{}]", blocks);
                    throw new OperationException("Error running calculating block " + blocks, e);
                }
            }

        }

        public Float getAggregatedResult() {
            return realssimTotal.getChannelValue(0);
        }

        public StaticColor getResult() {
            return realssimTotal;
        }

        public int getGranularityX() {
            return BLOCK_SIZE;
        }

        public int getGranularityY() {
            return BLOCK_SIZE;
        }

        /**
         * Callable implementing the SSIM operation.
         */
        private final class ThreadedSSIM implements Callable<Double[]> {

            private int[] x;
            private int[] y;

            /**
             * Creates a ThreadedSSIM with the provided block coordinates.
             *
             * @param x x coordinates of the block to check
             * @param y x coordinates of the block to check
             */
            private ThreadedSSIM(int[] x, int[] y) {
                this.x = x;
                this.y = y;
            }

            /**
             * Converts polar coordinates to Cartesian coordinates for a hue
             * channel.
             *
             * @param hue        the hue
             * @param saturation the saturation
             * @return an array representing the Cartesian coordinates
             */
            private double[] convertPolToCart(double hue, double saturation) {
                float cartX, cartY;
                cartX = (float) (saturation * Math.cos(2 * Math.PI * hue));
                cartY = (float) (saturation * Math.sin(2 * Math.PI * hue));
                return new double[]{cartX, cartY};
            }

            /**
             * Converts Cartesian coordinates to polar coordinates for a hue
             * channel.
             *
             * @param x x value
             * @param y y value
             * @return an array representing the polar coordinates
             */
            private double[] convertCartToPol(double x, double y) {
                float r, phi;
                r = (float) Math.hypot(x, y);
                if (r == 0) {
                    phi = 0;
                } else {
                    phi = (float) Math.atan2(y, x);
                    if (phi < 0) {
                        phi += 2 * Math.PI;
                    }
                    phi /= 2 * Math.PI;
                }
                double[] val = new double[2];
                val[0] = (double) r;
                val[1] = (double) phi;
                return val;
            }

            @Override
            public Double[] call() throws Exception {
                double[] avgX = new double[img1.getNumberOfChannels()];
                double[] avgY = new double[img1.getNumberOfChannels()];
                double[] varX = new double[img1.getNumberOfChannels()];
                double[] varY = new double[img1.getNumberOfChannels()];
                double[] covXY = new double[img1.getNumberOfChannels()];
                double hueXCartX = 0;
                double hueXCartY = 0;
                double hueYCartX = 0;
                double hueYCartY = 0;

                for (int counter = 0; counter < img1.getNumberOfChannels(); counter++) {
                    avgX[counter] = 0;
                    avgY[counter] = 0;
                    varX[counter] = 0;
                    varY[counter] = 0;
                    covXY[counter] = 0;
                }

                for (int i = 0; i < x.length; i++) {
                    StaticColor val1 = img1.getColorChannels(x[i], y[i]);
                    StaticColor val2 = img2.getColorChannels(x[i], y[i]);

                    // System.out.println(val1 + " <-> " + val2);
                    for (int counter = 0; counter < img1.getNumberOfChannels(); counter++) {
                        double value1 = val1.getChannelValue(counter);
                        double value2 = val2.getChannelValue(counter);

                        // Special handling for Hue channel
                        if (img1 instanceof HSBColorConverter && counter == 2) {
                            double[] cartX = convertPolToCart(val1.getChannelValue(2), 1);
                            hueXCartX += WINDOW[i] * cartX[0];
                            hueXCartY += WINDOW[i] * cartX[1];
                            double[] cartY = convertPolToCart(val2.getChannelValue(2), 1);
                            hueYCartX += WINDOW[i] * cartY[0];
                            hueYCartY += WINDOW[i] * cartY[1];
                            continue;
                        }

                        avgX[counter] += WINDOW[i] * value1;
                        avgY[counter] += WINDOW[i] * value2;
                    }
                }

                // Special handling for Hue channel
                if (img1 instanceof HSBColorConverter) {
                    // System.out.println(hueXCartX + ";" + hueXCartY);
                    double[] polX = convertCartToPol(hueXCartX, hueXCartY);
                    // System.out.println(polX[0] + ";" + polX[1]);
                    avgX[2] = polX[1];
                    // System.out.println("ux=" + avgX[2]);

                    // System.out.println(hueYCartX + ";" + hueYCartY);
                    double[] polY = convertCartToPol(hueYCartX, hueYCartY);
                    // System.out.println(polY[0] + ";" + polY[1]);
                    avgY[2] = polY[1];
                    // System.out.println("uy=" + avgY[2]);
                }

                for (int i = 0; i < x.length; i++) {
                    StaticColor val1 = img1.getColorChannels(x[i], y[i]);
                    StaticColor val2 = img2.getColorChannels(x[i], y[i]);

                    for (int counter = 0; counter < img1.getNumberOfChannels(); counter++) {
                        double value1 = avgX[counter] - val1.getChannelValue(counter);
                        double value2 = avgY[counter] - val2.getChannelValue(counter);

                        // Special handling for Hue channel
                        if (img1 instanceof HSBColorConverter && counter == 2) {
                            value1 = HSBColorConverter.normalizeHueDifference(Math.abs(value1));
                            value2 = HSBColorConverter.normalizeHueDifference(Math.abs(value2));
                        }
                        varX[counter] += WINDOW[i] * Math.pow(value1, 2);
                        varY[counter] += WINDOW[i] * Math.pow(value2, 2);
                        covXY[counter] += WINDOW[i] * (value1 * value2);
                        // System.out.println(varX[counter] + "," +
                        // varY[counter] + ";" +
                        // covXY[counter]);
                    }
                }

                /* c1, c2 calculation */
                double[] c1 = new double[img1.getNumberOfChannels()];
                double[] c2 = new double[img1.getNumberOfChannels()];
                for (int counter = 0; counter < img1.getNumberOfChannels(); counter++) {
                    c1[counter] = Math.pow(K1, 2);
                    c2[counter] = Math.pow(K2, 2);
                }

                double uf1 = 0;
                double uf2 = 0;
                double uf3 = 0;
                double lf1 = 0;
                double lf2 = 0;
                double lf3 = 0;
                Double[] ssim = new Double[img1.getNumberOfChannels()];

                for (int counter = 0; counter < img1.getNumberOfChannels(); counter++) {

                    if (img1 instanceof HSBColorConverter && counter == 2) {
                        double varProd = Math.sqrt(varX[counter]) * Math.sqrt(varY[counter]);

                        uf1 = 1 - (Math.hypot(hueXCartX - hueYCartX, hueXCartY - hueYCartY) *
                                HSBColorConverter.normalizeHueDifference(Math.abs(avgX[counter] - avgY[counter])));
                        // uf1 = 1 - (2 * HSBColorConverter.normalizeHueDifference(Math.abs(avgX[counter] - avgY[counter])));
                        lf1 = 1;

                        uf2 = 2 * varProd + c2[counter];
                        lf2 = varX[counter] + varY[counter] + c2[counter];

                        uf3 = covXY[counter] + c2[counter] / 2;
                        lf3 = varProd + c2[counter] / 2;

                    } else {
                        uf1 = 2 * avgX[counter] * avgY[counter] + c1[counter];
                        uf2 = 2 * covXY[counter] + c2[counter];

                        lf1 = Math.pow(avgX[counter], 2) + Math.pow(avgY[counter], 2) + c1[counter];
                        lf2 = varX[counter] + varY[counter] + c2[counter];

                        uf3 = 1;
                        lf3 = 1;
                    }

                    ssim[counter] = (uf1 * uf2 * uf3) / (lf1 * lf2 * lf3);

                    /*-
                    System.out.println("\n(" + counter + ")");
                    System.out.println(uf1 + "," + uf2 + "," + uf3);
                    System.out.println(lf1 + "," + lf2 + "," + lf3);
                    System.out.println((uf1 / lf1) + "," + (uf2 / lf2) + "," + (uf3 / lf3));
                    System.out.println(ssim[counter]);
                     */

                }

                return ssim;
            }

        }

    }
}
