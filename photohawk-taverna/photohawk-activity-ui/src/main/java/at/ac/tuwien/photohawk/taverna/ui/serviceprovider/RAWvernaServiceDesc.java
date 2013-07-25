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

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;

import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

import at.ac.tuwien.photohawk.taverna.RAWvernaActivity;
import at.ac.tuwien.photohawk.taverna.RAWvernaActivityConfigurationBean;

public class RAWvernaServiceDesc extends ServiceDescription<RAWvernaActivityConfigurationBean> {

    /**
     * The subclass of Activity which should be instantiated when adding a
     * service for this description
     */
    @Override
    public Class<? extends Activity<RAWvernaActivityConfigurationBean>> getActivityClass() {
        return RAWvernaActivity.class;
    }

    /**
     * The configuration bean which is to be used for configuring the
     * instantiated activity. Making this bean will typically require some of
     * the fields set on this service description, like an endpoint URL or
     * method name.
     * 
     */
    @Override
    public RAWvernaActivityConfigurationBean getActivityConfiguration() {
        RAWvernaActivityConfigurationBean bean = new RAWvernaActivityConfigurationBean();
        bean.setExampleString(exampleString);
        bean.setExampleUri(exampleUri);
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
        return exampleString;
    }

    /**
     * The path to this service description in the service palette. Folders will
     * be created for each element of the returned path.
     */
    @Override
    public List<String> getPath() {
        // For deeper paths you may return several strings
        return Arrays.asList("RAWvernas " + exampleUri);
    }

    /**
     * Return a list of data values uniquely identifying this service
     * description (to avoid duplicates). Include only primary key like fields,
     * ie. ignore descriptions, icons, etc.
     */
    @Override
    protected List<? extends Object> getIdentifyingData() {
        // FIXME: Use your fields instead of example fields
        return Arrays.<Object> asList(exampleString, exampleUri);
    }

    // FIXME: Replace example fields and getters/setters with any required
    // and optional fields. (All fields are searchable in the Service palette,
    // for instance try a search for exampleString:3)
    private String exampleString;
    private URI exampleUri;

    public String getExampleString() {
        return exampleString;
    }

    public URI getExampleUri() {
        return exampleUri;
    }

    public void setExampleString(String exampleString) {
        this.exampleString = exampleString;
    }

    public void setExampleUri(URI exampleUri) {
        this.exampleUri = exampleUri;
    }

}
