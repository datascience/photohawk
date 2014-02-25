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
import at.ac.tuwien.photohawk.evaluation.operation.metric.SimpleSSIMMetric;
import at.ac.tuwien.photohawk.evaluation.preprocessing.ScaleToNearestFactorPreprocessor;
import at.ac.tuwien.photohawk.evaluation.preprocessing.ShrinkResizePreprocessor;
import at.ac.tuwien.photohawk.evaluation.util.ConvenientBufferedImageWrapper;
import net.sf.ij_plugins.dcraw.DCRawException;
import net.sf.ij_plugins.dcraw.DCRawReader;
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
    Float run(String path1, String path2) throws IOException, DCRawException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        File file1=new File(path1);
        File file2=new File(path2);
        String path1NoExt = FilenameUtils.removeExtension(path1);
        String path2NoExt = FilenameUtils.removeExtension(path2);

        DCRawReader reader = new DCRawReader();

        reader.addLogListener(new DCRawReader.LogListener() {
            @Override
            public void log(String message) {
                System.out.println("message = " + message);
            }
        });
       // String[] readerFileSuffixes = ImageIO.getReaderFileSuffixes();
        reader.executeCommand(new String[]{
                "-v", // Print verbose messages
                "-w", // Use camera white balance, if possible
                "-T", // Write TIFF instead of PPM
                "-j", // Don't stretch or rotate raw pixels
                "-W", // Don't automatically brighten the image
                file1.getAbsolutePath()});

        reader.executeCommand(new String[]{
                "-v", "-w", "-T", "-j", "-W", file2.getAbsolutePath()});
        reader.removeAllLogListeners();

        file1=new File(path1NoExt + ".tiff");
        file2=new File(path2NoExt + ".tiff");
        BufferedImage[] images={ImageIO.read(file1),ImageIO.read(file2)};
        if (images == null) {
            throw new IOException("One of the images is corrupted or does not exist");
        }

        // Convert to SRGB
        images[0] = new SRGBColorConverter(new ConvenientBufferedImageWrapper(images[0])).getImage().getBufferedImage();
        images[1] = new SRGBColorConverter(new ConvenientBufferedImageWrapper(images[1])).getImage().getBufferedImage();
        // Resize
        ShrinkResizePreprocessor shrink = new ShrinkResizePreprocessor(images[0], images[1]);
        shrink.process();
        images[0] = shrink.getResult1();
        images[1] = shrink.getResult2();
        shrink = null;

        // Scale
        int targetSize = SimpleSSIMMetric.DEFAULT_TARGET_SIZE;
        ScaleToNearestFactorPreprocessor scale = new ScaleToNearestFactorPreprocessor(images[0], images[1], targetSize);

        scale.process();
        images[0] = scale.getResult1();
        images[1] = scale.getResult2();
        scale = null;

        // Evaluate
        ConvenientBufferedImageWrapper wrapped1 = new ConvenientBufferedImageWrapper(images[0]);
        HSBColorConverter c1 = new HSBColorConverter(new SRGBColorConverter(wrapped1));
        ConvenientBufferedImageWrapper wrapped2 = new ConvenientBufferedImageWrapper(images[1]);
        HSBColorConverter c2 = new HSBColorConverter(new SRGBColorConverter(wrapped2));

        // TODO: What happens if one image is smaller than the
        // SCALE_TARGET_SIZE?
        SimpleSSIMMetric ssim = null;
        ssim = new SimpleSSIMMetric(c1, c2, new Point(0, 0), new Point(images[0].getWidth(), images[0].getHeight()));
        TransientOperation<Float, StaticColor> op = ssim.execute();
        file1.delete();
        file2.delete();
        return op.getAggregatedResult();

    }
}
