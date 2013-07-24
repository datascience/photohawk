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
package at.ac.tuwien.RAWverna.ui.serviceprovider;

import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;

import at.ac.tuwien.RAWverna.SimpleSSIMActivity;
import at.ac.tuwien.RAWverna.SimpleSSIMActivityConfigurationBean;

import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

/**
 * Service description for SSIM.
 */
public class SimpleSSIMServiceDesc extends ServiceDescription<SimpleSSIMActivityConfigurationBean> {

    private static final String SERVICE_NAME = "SSIM";

    @Override
    public Class<? extends Activity<SimpleSSIMActivityConfigurationBean>> getActivityClass() {
        return SimpleSSIMActivity.class;
    }

    @Override
    public SimpleSSIMActivityConfigurationBean getActivityConfiguration() {
        SimpleSSIMActivityConfigurationBean bean = new SimpleSSIMActivityConfigurationBean();
        return bean;
    }

    @Override
    public Icon getIcon() {
        return RAWvernaServiceIcon.getIcon();
    }

    @Override
    public String getName() {
        return SERVICE_NAME;
    }

    @Override
    public List<String> getPath() {
        // For deeper paths you may return several strings
        return Arrays.asList("RAWverna");
    }

    @Override
    protected List<? extends Object> getIdentifyingData() {
        return Arrays.<Object> asList(SERVICE_NAME);
    }
}
