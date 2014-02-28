/*
 * Copyright 2014 artur.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package at.ac.tuwien.photohawk.app;

import at.ac.tuwien.photohawk.evaluation.colorconverter.StaticColor;
import at.ac.tuwien.photohawk.evaluation.colorconverter.hsb.HSBColorConverter;
import at.ac.tuwien.photohawk.evaluation.colorconverter.srgb.SRGBColorConverter;
import at.ac.tuwien.photohawk.evaluation.operation.TransientOperation;
import at.ac.tuwien.photohawk.evaluation.operation.metric.*;
import at.ac.tuwien.photohawk.evaluation.preprocessing.ScaleToNearestFactorPreprocessor;
import at.ac.tuwien.photohawk.evaluation.preprocessing.ShrinkResizePreprocessor;
import at.ac.tuwien.photohawk.evaluation.util.ConvenientBufferedImageWrapper;
import at.ac.tuwien.photohawk.evaluation.util.RawImageReader;

import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
/**
 *
 * @author artur
 */
public class Processor {
    final float FLOAT_ASSERT_DELTA = 0.000001f;

    final int DEFAULT_IMAGE_SIZE = 5;

    final Point DEFAULT_STARTPOINT = new Point(0, 0);

    final Point DEFAULT_ENDPOINT = new Point(DEFAULT_IMAGE_SIZE, DEFAULT_IMAGE_SIZE);

    Float run(String mode,String path1, String path2) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        RawImageReader.ConversionParameter params=new RawImageReader.ConversionParameter();
        BufferedImage[] images={RawImageReader.read(path1, params),RawImageReader.read(path2, params)};

        if (images == null) {
            throw new IOException("One of the images is corrupted or does not exist");
        }

        // Convert to SRGB

        images[0] = new SRGBColorConverter(new ConvenientBufferedImageWrapper(images[0])).getImage().getBufferedImage();
        images[1] = new SRGBColorConverter(new ConvenientBufferedImageWrapper(images[1])).getImage().getBufferedImage();

        ConvenientBufferedImageWrapper wrapped1 = new ConvenientBufferedImageWrapper(images[0]);
        ConvenientBufferedImageWrapper wrapped2 = new ConvenientBufferedImageWrapper(images[1]);
        HSBColorConverter c1 = new HSBColorConverter(new SRGBColorConverter(wrapped1));
        HSBColorConverter c2 = new HSBColorConverter(new SRGBColorConverter(wrapped2));
        TransientOperation<Float, StaticColor> op=null;
        if (mode.equals("ssim"))
        {
        SimpleSSIMMetric ssim = new SimpleSSIMMetric(c1, c2, new Point(0, 0), new Point(images[0].getWidth(), images[0].getHeight()));
        op = ssim.execute();
        }
        else if (mode.equals("ae"))
        {
            AEMetric ae=new AEMetric(c1,c2,DEFAULT_STARTPOINT, DEFAULT_ENDPOINT) ;
            op = ae.execute();
        }
        else if (mode.equals("pae"))
        {
            PAEMetric pae=new PAEMetric(c1,c2,DEFAULT_STARTPOINT, DEFAULT_ENDPOINT) ;
            op = pae.execute();
        }
        else if (mode.equals("mae"))
        {
            MAEMetric mae=new MAEMetric(c1,c2,DEFAULT_STARTPOINT, DEFAULT_ENDPOINT) ;
            op = mae.execute();
        }
        else if (mode.equals("mse"))
        {
            MSEMetric mse=new MSEMetric(c1,c2,DEFAULT_STARTPOINT, DEFAULT_ENDPOINT) ;
            op = mse.execute();
        }
        if (op!=null)
            return op.getAggregatedResult();
        return null;
    }
}
