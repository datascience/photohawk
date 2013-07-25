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
import java.util.List;

import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

import at.ac.tuwien.photohawk.taverna.SimpleSSIMActivity;
import at.ac.tuwien.photohawk.taverna.SimpleSSIMActivityConfigurationBean;

/**
 * Service description for SSIM.
 */
public class SimpleSSIMServiceDesc extends AbstractServiceDesc<SimpleSSIMActivityConfigurationBean> {

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
    public String getName() {
        return SERVICE_NAME;
    }

    @Override
    protected List<? extends Object> getIdentifyingData() {
        List<Object> data = new ArrayList<Object>(2);
        data.add(CommonServiceProvider.PROVIDER_NAME);
        data.add(SERVICE_NAME);
        return data;
    }
}
