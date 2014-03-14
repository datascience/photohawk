package at.ac.tuwien.photohawk.commandline;

import net.sourceforge.argparse4j.inf.Namespace;

public interface Command {

	void init();

	void configure(Namespace n);

	void evaluate();
}
