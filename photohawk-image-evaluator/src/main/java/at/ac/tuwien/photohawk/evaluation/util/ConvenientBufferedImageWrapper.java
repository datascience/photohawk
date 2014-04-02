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
package at.ac.tuwien.photohawk.evaluation.util;

import java.awt.*;
import java.awt.image.*;
import java.util.Vector;

/**
 * This class wraps a BufferedImage and provides some convenience as well as
 * performance tweaks.
 *
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 * @version 1.0
 */
public class ConvenientBufferedImageWrapper implements RenderedImage {

    private BufferedImage bufferedImage;
    private Raster data;

    /**
     * Creates a new ConvenientBufferedImageWrapper for the provided image.
     *
     * @param img the image
     */
    public ConvenientBufferedImageWrapper(BufferedImage img) {
        setBufferedImage(img);
    }

    /**
     * Returns a sample at the provided coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return an array representing the sample
     */
    public float[] getSample(int x, int y) {
        float[] result = new float[getSampleModel().getNumBands()];
        int[] pixel = new int[getSampleModel().getNumBands()];

        pixel = getData().getPixel(x, y, pixel);
        result = getColorModel().getNormalizedComponents(pixel, 0, result, 0);

        return result;
    }

    /**
     * Returns a sample at the provided coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the sample as color
     */
    public Color getSampleAsColor(int x, int y) {
        return new Color(getColorModel().getColorSpace(), getSample(x, y), 1.0f);
    }

    /**
     * Returns the image of this wrapper.
     *
     * @return the image.
     */
    public BufferedImage getBufferedImage() {
        return this.bufferedImage;
    }

    /**
     * Sets the image of this object.
     *
     * @param img the image
     */
    private void setBufferedImage(BufferedImage img) {
        if (null == img) {
            throw new IllegalArgumentException("Image not readable");
        }

        if (img.getWidth() <= 0 || img.getHeight() <= 0) {
            throw new IllegalArgumentException("Image size invalid");
        }

        this.bufferedImage = img;
        data = this.bufferedImage.getData();
    }

    /* ********* */
    /* DELEGATES */
    /* ********* */
    public synchronized Raster getData() {
        // Due to performance reasons
        return data;
    }

    public void setData(Raster r) {
        this.bufferedImage.setData(r);
    }

    public void addTileObserver(TileObserver to) {
        this.bufferedImage.addTileObserver(to);
    }

    public void coerceData(boolean isAlphaPremultiplied) {
        this.bufferedImage.coerceData(isAlphaPremultiplied);
    }

    public WritableRaster copyData(WritableRaster outRaster) {
        return this.bufferedImage.copyData(outRaster);
    }

    public Graphics2D createGraphics() {
        return this.bufferedImage.createGraphics();
    }

    public boolean equals(Object obj) {
        return this.bufferedImage.equals(obj);
    }

    public void flush() {
        this.bufferedImage.flush();
    }

    public float getAccelerationPriority() {
        return this.bufferedImage.getAccelerationPriority();
    }

    public void setAccelerationPriority(float priority) {
        this.bufferedImage.setAccelerationPriority(priority);
    }

    public WritableRaster getAlphaRaster() {
        return this.bufferedImage.getAlphaRaster();
    }

    public ImageCapabilities getCapabilities(GraphicsConfiguration gc) {
        return this.bufferedImage.getCapabilities(gc);
    }

    public ColorModel getColorModel() {
        return this.bufferedImage.getColorModel();
    }

    public Raster getData(Rectangle rect) {
        return this.bufferedImage.getData(rect);
    }

    public Graphics getGraphics() {
        return this.bufferedImage.getGraphics();
    }

    public int getHeight() {
        return this.bufferedImage.getHeight();
    }

    public int getHeight(ImageObserver observer) {
        return this.bufferedImage.getHeight(observer);
    }

    public int getMinTileX() {
        return this.bufferedImage.getMinTileX();
    }

    public int getMinTileY() {
        return this.bufferedImage.getMinTileY();
    }

    public int getMinX() {
        return this.bufferedImage.getMinX();
    }

    public int getMinY() {
        return this.bufferedImage.getMinY();
    }

    public int getNumXTiles() {
        return this.bufferedImage.getNumXTiles();
    }

    public int getNumYTiles() {
        return this.bufferedImage.getNumYTiles();
    }

    public Object getProperty(String name, ImageObserver observer) {
        return this.bufferedImage.getProperty(name, observer);
    }

    public Object getProperty(String name) {
        return this.bufferedImage.getProperty(name);
    }

    public String[] getPropertyNames() {
        return this.bufferedImage.getPropertyNames();
    }

    public WritableRaster getRaster() {
        return this.bufferedImage.getRaster();
    }

    public int[] getRGB(int startX, int startY, int w, int h, int[] rgbArray, int offset, int scansize) {
        return this.bufferedImage.getRGB(startX, startY, w, h, rgbArray, offset, scansize);
    }

    public int getRGB(int x, int y) {
        return this.bufferedImage.getRGB(x, y);
    }

    public SampleModel getSampleModel() {
        return this.bufferedImage.getSampleModel();
    }

    public Image getScaledInstance(int width, int height, int hints) {
        return this.bufferedImage.getScaledInstance(width, height, hints);
    }

    public ImageProducer getSource() {
        return this.bufferedImage.getSource();
    }

    public Vector<RenderedImage> getSources() {
        return this.bufferedImage.getSources();
    }

    public BufferedImage getSubimage(int x, int y, int w, int h) {
        return this.bufferedImage.getSubimage(x, y, w, h);
    }

    public Raster getTile(int tileX, int tileY) {
        return this.bufferedImage.getTile(tileX, tileY);
    }

    public int getTileGridXOffset() {
        return this.bufferedImage.getTileGridXOffset();
    }

    public int getTileGridYOffset() {
        return this.bufferedImage.getTileGridYOffset();
    }

    public int getTileHeight() {
        return this.bufferedImage.getTileHeight();
    }

    public int getTileWidth() {
        return this.bufferedImage.getTileWidth();
    }

    public int getTransparency() {
        return this.bufferedImage.getTransparency();
    }

    public int getType() {
        return this.bufferedImage.getType();
    }

    public int getWidth() {
        return this.bufferedImage.getWidth();
    }

    public int getWidth(ImageObserver observer) {
        return this.bufferedImage.getWidth(observer);
    }

    public WritableRaster getWritableTile(int tileX, int tileY) {
        return this.bufferedImage.getWritableTile(tileX, tileY);
    }

    public Point[] getWritableTileIndices() {
        return this.bufferedImage.getWritableTileIndices();
    }

    public int hashCode() {
        return this.bufferedImage.hashCode();
    }

    public boolean hasTileWriters() {
        return this.bufferedImage.hasTileWriters();
    }

    public boolean isAlphaPremultiplied() {
        return this.bufferedImage.isAlphaPremultiplied();
    }

    public boolean isTileWritable(int tileX, int tileY) {
        return this.bufferedImage.isTileWritable(tileX, tileY);
    }

    public void releaseWritableTile(int tileX, int tileY) {
        this.bufferedImage.releaseWritableTile(tileX, tileY);
    }

    public void removeTileObserver(TileObserver to) {
        this.bufferedImage.removeTileObserver(to);
    }

    public void setRGB(int startX, int startY, int w, int h, int[] rgbArray, int offset, int scansize) {
        this.bufferedImage.setRGB(startX, startY, w, h, rgbArray, offset, scansize);
    }

    public void setRGB(int x, int y, int rgb) {
        this.bufferedImage.setRGB(x, y, rgb);
    }

    public String toString() {
        return this.bufferedImage.toString();
    }
}
