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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


/**
 * This class provides wraps a raw image reader and provides some parameters to
 * influence the conversion.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 */
public class RawImageReader {

    private static String dcrawBinPath;
    private static String OS = null;
    public static String getOsName()
    {
        if(OS == null) { OS = System.getProperty("os.name"); }
        return OS;
    }
    public static boolean isWindows()
    {
        return getOsName().startsWith("Windows");
    }
    private static String pickDcrawExecutable() {
        return isWindows() ? "dcraw.exe" : "dcraw";
    }

    private static void validateDCRaw() throws IOException {
        validateDcraw(null);
    }
    public static void validateDcraw(String pathToDcraw) throws IOException {
        if (dcrawBinPath!=null && !dcrawBinPath.equals(""))
            return;
        if (pathToDcraw!=null){
            if (!pathToDcraw.endsWith(pickDcrawExecutable()))
            {
                if (pathToDcraw.endsWith("/"))
                    pathToDcraw = pathToDcraw + pickDcrawExecutable();
                else
                    pathToDcraw = pathToDcraw + "/" + pickDcrawExecutable();
            }
            File file = new File(pathToDcraw);
            if (file.exists())
            {
                dcrawBinPath=file.getAbsolutePath();
                //System.out.println("DCRAW executable attached successfully");
                return;
            }
        }else{
            File file = new File("dcraw/" + pickDcrawExecutable());
            if (file.exists())
            {
                dcrawBinPath=file.getAbsolutePath();
                //System.out.println("DCRAW executable attached successfully");
                return;
            }   else {
                file = new File(pickDcrawExecutable());
                dcrawBinPath=file.getAbsolutePath();
                //System.out.println("DCRAW executable attached successfully");
                return;

            }
        }
        throw new IOException("No DCRAW executable found at [ " + dcrawBinPath + " ]");
    }



    /**
     * Reads a RAW image using dcraw.
     * 
     * @param file
     *            path to the file
     * @param params
     *            conversion parameters
     * @return the image as BufferedImage
     * @throws IOException
     *             if the image could no be read
     */
    public static BufferedImage read(String file, ConversionParameter params) throws IOException {

        BufferedImage img = null;
        validateDCRaw();

        try {
            //CommandExecutor exec = new CommandExecutor();
            RawCommandExecutor exec   = new RawCommandExecutor();
            // Check if dcraw can decode the file
            int exitstatus = exec.runCommand(dcrawBinPath + " -i -v " + file);
            if (exitstatus == 0) {

                String command = dcrawBinPath + " -c";

                if (params.isUse16Bit()) {
                    command += " -T";
                }

                // Quickfix
                if (params.isUseTIFF()) {
                    // command += " -6";
                }

                if (!params.isUseAutoStretchOrRotation())
                {
                    command += " -j";
                }

                if (params.isUseFixedWhiteLevel()) {
                    command += " -W";
                }

                if (params.isUseCameraWhiteBalance()) {
                    command += " -w";
                }

                // Set wanted ColorSpace
                command += " -o " + params.getCs().getValue();

                // Set interpolation level
                if (!params.getInterpolation().getValue().equals("")) {
                    command += " " + params.getInterpolation().getValue();
                }

                Process process = exec.getProcess(command + " " + file);
                img = ImageIO.read(process.getInputStream());
                exitstatus = exec.runCommand(process);

                if (exitstatus != 0) {
                    throw new IOException("Error while converting raw");
                }
            } else{
                throw new IOException("No DCRAW executable found at [ " + dcrawBinPath + " ]");
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }

        return img;
    }

    public static class ConversionParameter {
        private boolean use16Bit;
        private boolean useTIFF;
        private ConversionColorSpace cs;
        private InterpretationType interpolation;
        private boolean useCameraWhiteBalance;
        private boolean useFixedWhiteLevel;
        private boolean useAutoStretchOrRotation;

        public ConversionParameter() {
            this(true, true, ConversionColorSpace.sRGB, InterpretationType.ColorInterpolated, true, true,false);
        }

        public ConversionParameter(boolean use16Bit, boolean useTIFF, ConversionColorSpace cs,
                                   InterpretationType interpolation, boolean useCameraWhitebalance, boolean useFixedWhiteLevel, boolean useAutoStretchOrRotation) {
            this.use16Bit = use16Bit;
            this.useTIFF = useTIFF;
            this.cs = cs;
            this.interpolation = interpolation;
            this.useCameraWhiteBalance = useCameraWhitebalance;
            this.useFixedWhiteLevel = useFixedWhiteLevel;
            this.useAutoStretchOrRotation= useAutoStretchOrRotation;
        }

        public boolean isUse16Bit() {
            return this.use16Bit;
        }

        public boolean isUseTIFF() {
            return this.useTIFF;
        }

        public boolean isUseCameraWhiteBalance() {
            return this.useCameraWhiteBalance;
        }

        public boolean isUseFixedWhiteLevel() {
            return this.useFixedWhiteLevel;
        }

        public ConversionColorSpace getCs() {
            return this.cs;
        }

        public InterpretationType getInterpolation() {
            return this.interpolation;
        }

        public boolean isUseAutoStretchOrRotation() {
            return this.useAutoStretchOrRotation;
        }

        public enum ConversionColorSpace {
            NoConversion(0),
            sRGB(1),
            AdobeRGB(2),
            WideGamutRGB(3),
            KodakProPhoto(4),
            XYZ(5);

            private final int value;

            ConversionColorSpace(int value) {
                this.value = value;
            }

            private int getValue() {
                return value;
            }
        }

        public enum InterpretationType {
            NoInterpretation("-D"),
            Delinearize("-d"),
            ColorInterpolated("");

            private final String value;

            InterpretationType(String value) {
                this.value = value;
            }

            private String getValue() {
                return value;
            }
        }
    }
}
