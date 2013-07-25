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

import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

import at.ac.tuwien.photohawk.taverna.AEErrorActivity;
import at.ac.tuwien.photohawk.taverna.CommonActivityConfigurationBean;

/**
 * Service description for Equal.
 */
public class AEServiceDesc extends AbstractServiceDesc<CommonActivityConfigurationBean> {

    private static final String SERVICE_NAME = "Absolute Error";

    public AEServiceDesc() {
        super(SERVICE_NAME);
    }

    @Override
    public Class<? extends Activity<CommonActivityConfigurationBean>> getActivityClass() {
        return AEErrorActivity.class;
    }

    @Override
    public CommonActivityConfigurationBean getActivityConfiguration() {
        CommonActivityConfigurationBean bean = new CommonActivityConfigurationBean();
        return bean;
    }

}
