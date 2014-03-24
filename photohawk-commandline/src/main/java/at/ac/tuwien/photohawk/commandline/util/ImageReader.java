/*******************************************************************************
 * Copyright 2010-2014 Vienna University of Technology
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

package at.ac.tuwien.photohawk.commandline.util;

import at.ac.tuwien.photohawk.commandline.Photohawk;
import at.ac.tuwien.photohawk.dcraw.ParameterReader;
import at.ac.tuwien.photohawk.dcraw.RawImageReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;

/**
 * Image reader.
 */
public class ImageReader {
    private ParameterReader parameterReader;
    private String baseKey;

    /**
     * Creates a new image reader.
     *
     * @param baseKey base key for the parameter reader
     */
    public ImageReader(String baseKey) {
        this.parameterReader = new ParameterReader();
        this.baseKey = baseKey;
    }

    /**
     * Reads an image from the provided file depending on the provided read modes of the file to read and the file it will be compared with.
     *
     * @param file      the file to read
     * @param thisMode  read mode of this file
     * @param otherMode read mode of the other file
     * @return the image
     * @throws IOException if an error occurred during read
     */
    public BufferedImage readImage(File file, String thisMode, String otherMode) throws IOException {
        if (thisMode.equals(Photohawk.READ_MODE_DIRECT)) {
            return readDirect(file);
        }

        if (thisMode.equals(Photohawk.READ_MODE_DCRAW)) {
            List<String> params = parameterReader.readParameters(baseKey, otherMode.equals(Photohawk.READ_MODE_DCRAW));
            RawImageReader ir = new RawImageReader();
            return ir.read(file.getAbsolutePath(), params);
        }
        return null;
    }

    /**
     * Determines the read mode depending on the file and the selected node.
     *
     * @param file the file to read
     * @param mode read mode of the file
     * @return the determined read mode or null if none found
     * @throws IOException if an error occurered during read
     */
    public String determineReadMode(File file, String mode) throws IOException {
        if (mode.equals(Photohawk.READ_MODE_DIRECT) || mode.equals(Photohawk.READ_MODE_DCRAW)) {
            return mode;
        }

        if (mode.equals(Photohawk.READ_MODE_DCRAW_FALLBACK)) {
            RawImageReader ir = new RawImageReader();
            if (ir.canDecode(file.getAbsolutePath())) {
                return Photohawk.READ_MODE_DCRAW;
            } else {
                return Photohawk.READ_MODE_DIRECT;
            }
        }

        if (mode.equals(Photohawk.READ_MODE_DIRECT_MIMETYPE)) {
            String mimetype = Files.probeContentType(file.toPath());
            Iterator<javax.imageio.ImageReader> readers = ImageIO.getImageReadersByMIMEType(mimetype);
            if (readers.hasNext()) {
                return Photohawk.READ_MODE_DIRECT;
            } else {
                return Photohawk.READ_MODE_DCRAW;
            }
        }

        return null;
    }

    /**
     * Reads the file using Java's ImageIO.
     *
     * @param file the file to read
     * @return the image
     * @throws IOException if an error occurred during read
     */
    private BufferedImage readDirect(File file) throws IOException {
        BufferedImage img = ImageIO.read(file);
        if (img == null) {
            throw new IOException("Cannot read image " + file.getAbsolutePath());
        }
        return img;
    }
}
