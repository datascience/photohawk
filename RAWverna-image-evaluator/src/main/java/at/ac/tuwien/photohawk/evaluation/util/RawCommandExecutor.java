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

import java.io.IOException;
import java.io.InputStream;

import at.ac.tuwien.photohawk.executor.CommandExecutor;




/**
 * This class is a wraps the normal CommandExecutor to get direct access to the
 * resulting InputStream.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 * @version 1.0
 */
public class RawCommandExecutor {

    public static Process getProcess(String commandLine) throws IOException {
        return new NestedCommandExecutor().runCommandHelper(commandLine);
    }

    public static int runProcess(Process process) throws Exception {
        /* wait for command execution to terminate */
        int exitStatus = -1;
        try {
            exitStatus = process.waitFor();

        } catch (Throwable ex) {
            throw new Exception(ex.getMessage());

        } finally {
            /* notify output and error read threads to stop reading */
            InputStream in = process.getInputStream();
            in.close();
        }
        return exitStatus;
    }

    private static class NestedCommandExecutor extends CommandExecutor {
        public Process runCommandHelper(String commandLine) throws IOException {
            return super.runCommandHelper(commandLine);
        }
    }

}
