package at.ac.tuwien.RAWverna.ui.serviceprovider;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.servicedescriptions.ServiceDescriptionProvider;

public class JavaFITSEvaluatorServiceProvider implements ServiceDescriptionProvider {

	private static final URI providerId = URI
			.create("http://tuwien.ac.at/2011/RAWverna/service-provider/JavaFITSEvaluator");

	/**
	 * Do the actual search for services. Return using the callBack parameter.
	 */
	@SuppressWarnings("unchecked")
	public void findServiceDescriptionsAsync(FindServiceDescriptionsCallBack callBack) {
		// Use callback.status() for long-running searches
		// callBack.status("Resolving example services");

		List<ServiceDescription> results = new ArrayList<ServiceDescription>();

		JavaFITSEvaluatorServiceDesc service = new JavaFITSEvaluatorServiceDesc();
		// Populate the service description bean
		service.setServiceName(getName());

		// Optional: set description
		service.setDescription("Java evaluator for FITS comparison");
		results.add(service);

		// partialResults() can also be called several times from inside
		// for-loop if the full search takes a long time
		callBack.partialResults(results);

		// No more results will be coming
		callBack.finished();
	}

	/**
	 * Icon for service provider
	 */
	public Icon getIcon() {
		return RAWvernaServiceIcon.getIcon();
	}

	/**
	 * Name of service provider, appears in right click for 'Remove service
	 * provider'
	 */
	public String getName() {
		return "Java FITS evaluator";
	}

	@Override
	public String toString() {
		return getName();
	}

	public String getId() {
		return providerId.toASCIIString();
	}

}
