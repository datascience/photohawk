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

/**
 * Abstract class for service descriptors.
 * 
 * @param <T>
 *            type of configuration bean
 */
public abstract class AbstractServiceDesc<T> extends ServiceDescription<T> {

    private String name;

    /**
     * Creates a new service description with the provided service name.
     * 
     * @param name
     *            name of the service
     * @param description
     *            description of the service
     */
    public AbstractServiceDesc(String name, String description) {
        super();
        this.name = name;
        setDescription(description);
    }

    /**
     * Creates a new service description with the provided service name.
     * 
     * @param name
     *            name of the service
     */
    public AbstractServiceDesc(String name) {
        this(name, "");
    }

    private List<String> path = Arrays.<String> asList(CommonServiceProvider.PROVIDER_NAME);

    @Override
    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    @Override
    public Icon getIcon() {
        return CommonServiceIcon.getIcon();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    protected List<? extends Object> getIdentifyingData() {
        List<Object> data = new ArrayList<Object>(2);
        data.add(CommonServiceProvider.PROVIDER_NAME);
        data.add(name);
        return data;
    }
}
