/*******************************************************************************
 * Copyright 2013 Vienna University of Technology
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
//package at.ac.tuwien.RAWverna.ui.serviceprovider;
//
//import java.io.InputStream;
//import java.net.URI;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import javax.swing.Icon;
//
//import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
//import net.sf.taverna.t2.servicedescriptions.ServiceDescriptionProvider;
//import net.sf.taverna.t2.workflowmodel.Dataflow;
//import net.sf.taverna.t2.workflowmodel.serialization.xml.DataflowXMLDeserializer;
//
//import org.jdom.Element;
//import org.jdom.input.SAXBuilder;
//
//public class NestedTest2ServiceProvider implements ServiceDescriptionProvider {
//
//	private static final URI providerId = URI.create("http://tuwien.ac.at/2011/RAWverna/service-provider/NestedTest");
//
//	/**
//	 * Do the actual search for services. Return using the callBack parameter.
//	 */
//	@SuppressWarnings("unchecked")
//	public void findServiceDescriptionsAsync(FindServiceDescriptionsCallBack callBack) {
////		// Use callback.status() for long-running searches
////		// callBack.status("Resolving example services");
////
////		List<ServiceDescription> results = new ArrayList<ServiceDescription>();
////
////		JavaImageEvaluatorServiceDesc service = new JavaImageEvaluatorServiceDesc();
////		// Populate the service description bean
////		service.setServiceName(getName());
////
////		// Optional: set description
////		service.setDescription("Java evaluator for image comparison");
////		results.add(service);
////
////		// partialResults() can also be called several times from inside
////		// for-loop if the full search takes a long time
////		callBack.partialResults(results);
////
////		// No more results will be coming
////		callBack.finished();
////
////		DataflowXMLDeserializer deserializer = DataflowXMLDeserializer.getInstance();
////
////		String resource = "/Test.t2flow";
////
////		InputStream resourceAsStream = getClass().getResourceAsStream(resource);
////
////		SAXBuilder builder = new SAXBuilder();
////		Element detachRootElement = null;
////		try {
////			detachRootElement = builder.build(resourceAsStream).detachRootElement();
////		} catch (Exception e) {
////			// throw new Exception("Could not parse resource " + resource, e);
////		}
////
////		Dataflow activity = null;
////		try {
////			activity = (Dataflow) deserializer.deserializeDataflow(detachRootElement, new HashMap<String, Element>());
////		} catch (Exception e) {
////			e.printStackTrace();
////			// logger.error("Could not create LocalWorkerServiceDescription",
////			// e);
////			// throw new ItemCreationException(e);
////		}
//
//	}
//
//	/**
//	 * Icon for service provider
//	 */
//	public Icon getIcon() {
//		return RAWvernaServiceIcon.getIcon();
//	}
//
//	/**
//	 * Name of service provider, appears in right click for 'Remove service
//	 * provider'
//	 */
//	public String getName() {
//		return "Java image evaluator";
//	}
//
//	@Override
//	public String toString() {
//		return getName();
//	}
//
//	public String getId() {
//		return providerId.toASCIIString();
//	}
//
//}
