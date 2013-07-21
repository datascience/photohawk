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
//import java.net.URI;
//import java.util.Arrays;
//import java.util.List;
//
//import javax.swing.Icon;
//
//import net.sf.taverna.t2.activities.dataflow.servicedescriptions.DataflowActivityIcon;
//import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
//import net.sf.taverna.t2.servicedescriptions.ServiceDescriptionProvider;
//import net.sf.taverna.t2.workflowmodel.Dataflow;
//import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;
//
//public class NestedWorkflowServiceProvider implements ServiceDescriptionProvider {
//
//	private static final String A_CONFIGURABLE_NESTED_WORKFLOW = "RAWverna Test";
//	private static final String DATAFLOW = "Nested workflow Test 1";
//
//	private static final URI providerId = URI.create("http://tuwien.ac.at/2011/RAWverna/service-provider/NestedTest");
//
////	@Override
////	public Class<DataflowActivity> getActivityClass() {
////		return DataflowActivity.class;
////	}
////
////	@Override
////	public Dataflow getActivityConfiguration() {
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
////		String name = detachRootElement.getChildText("name");
////		Dataflow activity = null;
////		try {
////			activity = (Dataflow) deserializer.deserializeDataflow(detachRootElement, new HashMap<String, Element>());
////		} catch (Exception e) {
////			e.printStackTrace();
////			// logger.error("Could not create LocalWorkerServiceDescription",
////			// e);
////			// throw new ItemCreationException(e);
////		}
////
////		return activity;
////
////	}
//
//	@Override
//	public void findServiceDescriptionsAsync(FindServiceDescriptionsCallBack callBack) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public Icon getIcon() {
//		return DataflowActivityIcon.getDataflowIcon();
//	}
//
//	public String getName() {
//		return DATAFLOW;
//	}
//
//	public String getDescription() {
//		return A_CONFIGURABLE_NESTED_WORKFLOW;
//	}
//
//	public class TemplateServiceDescription extends ServiceDescription<Dataflow> {
//		public Icon getIcon() {
//			return TemplateServiceDescription.this.getIcon();
//		}
//
//		public String getName() {
//			return TemplateServiceDescription.this.getName();
//		}
//
//		public List<String> getPath() {
//			return Arrays.asList(SERVICE_TEMPLATES);
//		}
//
//		public boolean isTemplateService() {
//			return false;
//		}
//
//		@Override
//		protected List<Object> getIdentifyingData() {
//			// Do it by object identity
//			return null;
//		}
//
//		@Override
//		public Class<? extends Activity<Dataflow>> getActivityClass() {
//			return TemplateServiceDescription.this.getActivityClass();
//		}
//
//		@Override
//		public Dataflow getActivityConfiguration() {
//			return TemplateServiceDescription.this.getActivityConfiguration();
//		}
//
//		public String getDescription() {
//			return TemplateServiceDescription.this.getDescription();
//		}
//	}
//
////	@SuppressWarnings("unchecked")
////	public static ServiceDescription getServiceDescription() {
////		NestedWorkflowServiceProvider dts = new NestedWorkflowServiceProvider();
////		return dts.templateService;
////	}
//
//	public String getId() {
//		return providerId.toString();
//	}
//
//}
