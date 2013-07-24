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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.servicedescriptions.ServiceDescriptionProvider;

/**
 * Service provider for activities.
 */
public class CommonServiceProvider implements ServiceDescriptionProvider {

    private static final URI PROVIDER_ID = URI
        .create("http://tuwien.ac.at/2011/RAWverna/service-provider/JavaImageEvaluator");

    @Override
    public void findServiceDescriptionsAsync(FindServiceDescriptionsCallBack callBack) {
        // Use callback.status() for long-running searches
        // callBack.status("Resolving example services");

        List<ServiceDescription<?>> results = new ArrayList<ServiceDescription<?>>();

        SimpleSSIMServiceDesc service = new SimpleSSIMServiceDesc();
        results.add(service);

        // partialResults() can also be called several times from inside
        // for-loop if the full search takes a long time
        callBack.partialResults(results);

        // No more results will be coming
        callBack.finished();
    }

    @Override
    public Icon getIcon() {
        return RAWvernaServiceIcon.getIcon();
    }

    @Override
    public String getName() {
        return "Java image evaluator";
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getId() {
        return PROVIDER_ID.toASCIIString();
    }

}
