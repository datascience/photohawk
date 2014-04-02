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
package at.ac.tuwien.photohawk.dcraw;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * DCRaw based image reader.
 *
 * @see <a href="http://www.cybercom.net/~dcoffin/dcraw/">DCRaw</a>
 */
public class RawImageReader {

    /**
     * Environment variable name for the DCRaw executable.
     */
    public static final String DCRAW_ENV_VAR = "DCRAW";

    private String dcrawBin;

    /**
     * Creates a new image reader.
     */
    public RawImageReader() {
        this(null);
    }

    /**
     * Creates a new image reader with the provided DCRaw executable.
     *
     * @param dcrawBin dcraw executable
     */
    public RawImageReader(String dcrawBin) {
        this.dcrawBin = dcrawBin;
    }

    /**
     * Finds the DCRaw executable.
     *
     * @return the DCRaw executable
     */
    public String getDcrawBin() {
        // Check field
        if (dcrawBin != null) {
            return dcrawBin;
        }

        // Check environment
        String dcrawEnv = System.getenv().get(DCRAW_ENV_VAR);
        if (dcrawEnv != null) {
            return dcrawEnv;
        }

        // Guess by OS
        if (System.getProperty("os.name").startsWith("Windows")) {
            return "dcraw.exe";
        } else {
            return "dcraw";
        }
    }

    /**
     * Reads a RAW image using dcraw.
     *
     * @param file   path to the file
     * @param params conversion parameters
     * @return the image as BufferedImage
     * @throws IOException if the image could no be read
     */
    public BufferedImage read(final String file, final List<String> params) throws IOException {
        BufferedImage img = null;

        if (canDecode(file)) {
            List<String> command = new ArrayList<String>();

            command.add(getDcrawBin());
            command.add("-c");
            command.addAll(params);
            command.add(file);

            ProcessBuilder pb = new ProcessBuilder(command);
            Process p = pb.start();

            AsyncReader errorReader = new AsyncReader(new BufferedInputStream(p.getErrorStream()));
            FutureTask<String> errorTask = new FutureTask<String>(errorReader);
            ExecutorService pool = Executors.newSingleThreadExecutor();
            try {
                pool.execute(errorTask);
                img = ImageIO.read(p.getInputStream());

                if (img == null) {
                    throw new IOException("ImageIO could not read the image provided by DCRAW");
                }

                int exitStatus = p.waitFor();
                if (exitStatus != 0) {
                    throw new IOException("Dcraw terminated with exit status " + exitStatus);
                }
            } catch (InterruptedException e) {
                throw new IOException("Dcraw execution for file [" + file + "] was interrupted", e);
            } finally {
                p.getOutputStream().close();
                p.getInputStream().close();
                errorReader.close();
                pool.shutdown();
            }
        } else {
            throw new IOException("Cannot decode file [" + file + "]");
        }
        return img;
    }

    /**
     * Checks if the image reader can decode the provided file.
     *
     * @param file the file to check
     * @return true if the image reader can decode the file, false otherwise
     * @throws IOException if an error occurred during decoding the file
     */
    public boolean canDecode(final String file) throws IOException {
        List<String> command = new ArrayList<String>();
        command.add(getDcrawBin());
        command.add("-i");
        command.add("-v");
        command.add(file);
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process p = pb.start();
        try {
            return p.waitFor() == 0;
        } catch (InterruptedException e) {
            throw new IOException("Error decoding file [" + file + "]", e);
        } finally {
            p.getInputStream().close();
            p.getErrorStream().close();
            p.getOutputStream().close();
        }

    }

    /**
     * Reader to asynchronously read from a input stream.
     */
    public class AsyncReader implements Callable<String> {
        private InputStream is;
        private Scanner s;

        /**
         * Creates a new async reader to read from the provided input stream.
         *
         * @param is the input stream to read from
         */
        public AsyncReader(InputStream is) {
            this.is = is;
            s = new Scanner(is, "UTF-8");
        }

        @Override
        public String call() {
            return s.useDelimiter("\\A").next();
        }

        /**
         * Closes the input stream and other resources of this reader.
         *
         * @throws IOException if the resources could not be closed
         */
        public void close() throws IOException {
            is.close();
            s.close();
        }
    }
}
