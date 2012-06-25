package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.util;

import java.io.IOException;
import java.io.InputStream;

import eu.planets_project.pp.plato.util.CommandExecutor;

/**
 * This class is a wraps the normal CommandExecutor to get direct access to the resulting
 * InputStream.
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
