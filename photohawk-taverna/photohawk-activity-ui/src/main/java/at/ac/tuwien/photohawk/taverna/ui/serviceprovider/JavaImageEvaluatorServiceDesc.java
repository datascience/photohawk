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
package at.ac.tuwien.photohawk.taverna.ui.serviceprovider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;

import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;
import at.ac.tuwien.photohawk.taverna.JavaImageEvaluatorActivity;
import at.ac.tuwien.photohawk.taverna.JavaImageEvaluatorActivityConfigurationBean;

public class JavaImageEvaluatorServiceDesc extends ServiceDescription<JavaImageEvaluatorActivityConfigurationBean> {

    private String serviceName;

    /**
     * The subclass of Activity which should be instantiated when adding a
     * service for this description
     */
    @Override
    public Class<? extends Activity<JavaImageEvaluatorActivityConfigurationBean>> getActivityClass() {
        return JavaImageEvaluatorActivity.class;
    }

    /**
     * The configuration bean which is to be used for configuring the
     * instantiated activity. Making this bean will typically require some of
     * the fields set on this service description, like an endpoint URL or
     * method name.
     * 
     */
    @Override
    public JavaImageEvaluatorActivityConfigurationBean getActivityConfiguration() {
        JavaImageEvaluatorActivityConfigurationBean bean = new JavaImageEvaluatorActivityConfigurationBean();
        List<String> measurementURIs = new ArrayList<String>(1);
        measurementURIs.add("outcome://object/image/similarity#equal");
        bean.setMeasurementURIs(measurementURIs);
        return bean;
    }

    /**
     * An icon to represent this service description in the service palette.
     */
    @Override
    public Icon getIcon() {
        return RAWvernaServiceIcon.getIcon();
    }

    /**
     * The display name that will be shown in service palette and will be used
     * as a template for processor name when added to workflow.
     */
    @Override
    public String getName() {
        return serviceName;
    }

    /**
     * The path to this service description in the service palette. Folders will
     * be created for each element of the returned path.
     */
    @Override
    public List<String> getPath() {
        // For deeper paths you may return several strings
        return Arrays.<String> asList("Photohawk", "legacy");
    }

    /**
     * Return a list of data values uniquely identifying this service
     * description (to avoid duplicates). Include only primary key like fields,
     * ie. ignore descriptions, icons, etc.
     */
    @Override
    protected List<? extends Object> getIdentifyingData() {
        return Arrays.<Object> asList(serviceName);
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

}
