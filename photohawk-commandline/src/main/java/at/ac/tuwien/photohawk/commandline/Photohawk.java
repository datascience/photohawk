package at.ac.tuwien.photohawk.commandline;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparsers;

public class Photohawk {

	private static final String COMMAND = "command";

	public static void main(String[] args) {
		Photohawk p = new Photohawk();

		ArgumentParser parser = ArgumentParsers.newArgumentParser("photohawk");

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
}
