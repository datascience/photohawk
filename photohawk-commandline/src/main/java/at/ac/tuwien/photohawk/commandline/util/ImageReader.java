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
     */
    public BufferedImage readImage(File file, String thisMode, String otherMode) throws IOException {
        if (thisMode.equals(Photohawk.READ_MODE_DIRECT)) {
            BufferedImage img = ImageIO.read(file);
            if (img == null) {
                throw new IOException("Cannot read image " + file.getAbsolutePath());
            }
            return img;
        }

        if (thisMode.equals(Photohawk.READ_MODE_DCRAW)) {
            List<String> params = parameterReader.readParameters(baseKey, otherMode.equals(Photohawk.READ_MODE_DCRAW));
            RawImageReader ir = new RawImageReader();
            return ir.read(file.getAbsolutePath(), params);
        }

        return null;
    }
}
