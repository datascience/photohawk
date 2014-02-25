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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import at.ac.tuwien.photohawk.executor.CommandExecutor;
import at.ac.tuwien.photohawk.executor.ILogDevice;


/**
 * This class is a wraps the normal CommandExecutor to get direct access to the
 * resulting InputStream.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 * @version 1.0
 */
public class RawCommandExecutor {

    private ILogDevice fOuputLogDevice = null;
    private ILogDevice fErrorLogDevice = null;
    private String fWorkingDirectory = null;
    private List fEnvironmentVarList = null;

    private StringBuffer fCmdOutput = null;
    private StringBuffer fCmdError = null;
    private AsyncStreamReader fCmdOutputThread = null;
    private AsyncStreamReader fCmdErrorThread = null;
    public Process getProcess(String commandLine) throws IOException {
        return new NestedCommandExecutor().runCommandHelper(commandLine);
    }

    public int runCommand(String commandLine) throws Exception {
        /* run command */
        Process process =new NestedCommandExecutor().runCommandHelper(commandLine);

        /* start output and error read threads */
        startOutputAndErrorReadThreads(process.getInputStream(), process.getErrorStream());

        /* wait for command execution to terminate */
        int exitStatus = -1;
        try {
            exitStatus = process.waitFor();

        } catch (Throwable ex) {
            throw new Exception(ex.getMessage());

        } finally {
            /* notify output and error read threads to stop reading */
            notifyOutputAndErrorReadThreadsToStopReading();
            InputStream in = process.getInputStream();
            in.close();
        }

        return exitStatus;
    }


    public int runCommand(Process process) throws Exception {

        /* start output and error read threads */
        startOutputAndErrorReadThreads(process.getInputStream(), process.getErrorStream());


        /* wait for command execution to terminate */
        int exitStatus = -1;
        try {
            exitStatus = process.waitFor();


        } catch (Throwable ex) {
            throw new Exception(ex.getMessage());

        } finally {
            /* notify output and error read threads to stop reading */
            notifyOutputAndErrorReadThreadsToStopReading();

            InputStream in = process.getInputStream();
            in.close();
        }
        return exitStatus;
    }

    private void startOutputAndErrorReadThreads(InputStream processOut, InputStream processErr) {
        fCmdOutput = new StringBuffer();
        fCmdOutputThread = new AsyncStreamReader(processOut, fCmdOutput, fOuputLogDevice, "OUTPUT");
        fCmdOutputThread.start();

        fCmdError = new StringBuffer();
        fCmdErrorThread = new AsyncStreamReader(processErr, fCmdError, fErrorLogDevice, "ERROR");
        fCmdErrorThread.start();
    }

    private void notifyOutputAndErrorReadThreadsToStopReading() {
        fCmdOutputThread.stopReading();
        fCmdErrorThread.stopReading();
    }



    private class NestedCommandExecutor extends CommandExecutor {
        public Process runCommandHelper(String commandLine) throws IOException {
            return super.runCommandHelper(commandLine);
        }
    }



}

class AsyncStreamReader extends Thread {
    private StringBuffer fBuffer = null;
    private InputStream fInputStream = null;
    private String fThreadId = null;
    private boolean fStop = false;
    private ILogDevice fLogDevice = null;

    private String fNewLine = null;

    public AsyncStreamReader(InputStream inputStream, StringBuffer buffer, ILogDevice logDevice, String threadId) {
        fInputStream = inputStream;
        fBuffer = buffer;
        fThreadId = threadId;
        fLogDevice = logDevice;

        fNewLine = System.getProperty("line.separator");
    }

    public String getBuffer() {
        return fBuffer.toString();
    }

    public void run() {
        try {
            readCommandOutput();
        } catch (Exception ex) {
            // ex.printStackTrace(); //DEBUG
        }
    }

    private void readCommandOutput() throws IOException {
        BufferedReader bufOut = new BufferedReader(new InputStreamReader(fInputStream));
        String line = null;
        while ((fStop == false) && ((line = bufOut.readLine()) != null)) {
           // fBuffer.append(line + fNewLine);
           // printToDisplayDevice(line);
        }
        bufOut.close();
        // printToConsole("END OF: " + fThreadId); //DEBUG
    }

    public void stopReading() {
        fStop = true;
    }

    private void printToDisplayDevice(String line) {
        if (fLogDevice != null)
            fLogDevice.log(line);
        else {
            printToConsole(line);// DEBUG
        }
    }

    private synchronized void printToConsole(String line) {
        System.out.println(line);
    }
}
