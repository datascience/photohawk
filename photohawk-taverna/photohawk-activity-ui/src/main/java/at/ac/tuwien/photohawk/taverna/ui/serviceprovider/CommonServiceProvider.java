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
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.servicedescriptions.ServiceDescriptionProvider;

/**
 * Service provider for activities.
 */
public class CommonServiceProvider implements ServiceDescriptionProvider {

    /**
     * Name of this service provider.
     */
    public static final String PROVIDER_NAME = "Photohawk";

    private static final URI PROVIDER_ID = URI
        .create("http://tuwien.ac.at/2013/photohawk/service-provider/CommonServiceProvider");

    @Override
    public void findServiceDescriptionsAsync(FindServiceDescriptionsCallBack callBack) {
        List<ServiceDescription<?>> results = new ArrayList<ServiceDescription<?>>();

        results.add(new SimpleSSIMServiceDesc());
        results.add(new EqualServiceDesc());
        results.add(new AEServiceDesc());
        results.add(new MAEServiceDesc());
        results.add(new PAEServiceDesc());
        results.add(new MSEServiceDesc());

        callBack.partialResults(results);

        callBack.finished();
    }

    @Override
    public Icon getIcon() {
        return RAWvernaServiceIcon.getIcon();
    }

    @Override
    public String getName() {
        return PROVIDER_NAME;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getId() {
        return PROVIDER_ID.toASCIIString();
    }

}
