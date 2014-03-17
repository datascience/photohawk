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

package at.ac.tuwien.photohawk.commandline;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparsers;

import java.io.IOException;
import java.util.Properties;

/**
 * Photohawk command line utility.
 */
public class Photohawk {

    private static final String COMMAND = "command";

    /**
     * Main method for photohawk commandline.
     *
     * @param args command line parameters
     */
    public static void main(String[] args) {
        Photohawk p = new Photohawk();
        try {
            p.init(args);
        } catch (PhotohawkException e) {
            System.err.println("Error initializing application.");
        }
    }

    /**
     * Initializes photohawk commandline setting up parameter parsing.
     *
     * @param args command line parameters
     * @throws PhotohawkException if the program could not be initialized
     */
    public void init(String[] args) throws PhotohawkException {
        ArgumentParser parser = ArgumentParsers.newArgumentParser("photohawk");

        try {
            parser.version("${prog} " + gitProperties().getProperty("git.commit.id.describe") + "\n\nLicensed under the Apache License, Version 2.0\nCopyright 2010-2014 Vienna University of Technology");
            parser.addArgument("--version").action(Arguments.version()).help("show version information and exit");
        } catch (IOException e) {
            throw new PhotohawkException("Error reading git properties.", e);
        }

        Subparsers subparsers = parser.addSubparsers().title("Algorithm")
                .help("Comparison algorithm");

        // SSIM
        Ssim ssimCmd = new Ssim(subparsers);
        ssimCmd.init();

        // MSE
        Mse mseCmd = new Mse(subparsers);
        mseCmd.init();

        // AE
        Ae aeCmd = new Ae(subparsers);
        aeCmd.init();

        // MAE
        Mae maeCmd = new Mae(subparsers);
        maeCmd.init();

        // PAE
        Pae paeCmd = new Pae(subparsers);
        paeCmd.init();

        // Equal
        Equal equalCmd = new Equal(subparsers);
        equalCmd.init();

        try {
            Namespace res = parser.parseArgs(args);
            Command c = (Command) res.get(COMMAND);
            c.configure(res);
            c.evaluate();
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }
    }

    /**
     * Reads git properties containing information about the current git status.
     * <p/>
     * See pl.project13.maven - git-commit-id-plugin maven plugin for details.
     *
     * @return properties with git information
     * @throws IOException if the properties could not be read
     */
    private Properties gitProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("git.properties"));
        return properties;
    }
}
