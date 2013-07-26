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

import at.ac.tuwien.photohawk.taverna.CommonActivityConfigurationBean;
import at.ac.tuwien.photohawk.taverna.MAEActivity;

/**
 * Service description for MAE.
 */
public class MAEServiceDesc extends AbstractServiceDesc<CommonActivityConfigurationBean> {

    private static final String SERVICE_NAME = "Mean Absolute Error";

    /**
     * Creates a new service description.
     */
    public MAEServiceDesc() {
        super(SERVICE_NAME);
    }

    @Override
    public Class<? extends Activity<CommonActivityConfigurationBean>> getActivityClass() {
        return MAEActivity.class;
    }

    @Override
    public CommonActivityConfigurationBean getActivityConfiguration() {
        return new CommonActivityConfigurationBean();
    }
}
