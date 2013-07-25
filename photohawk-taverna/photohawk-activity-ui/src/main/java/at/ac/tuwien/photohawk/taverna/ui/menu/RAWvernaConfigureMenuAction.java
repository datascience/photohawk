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
package at.ac.tuwien.photohawk.taverna.ui.menu;

import javax.swing.Action;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;
import at.ac.tuwien.photohawk.taverna.RAWvernaActivity;
import at.ac.tuwien.photohawk.taverna.ui.config.RAWvernaConfigureAction;

public class RAWvernaConfigureMenuAction extends AbstractConfigureActivityMenuAction<RAWvernaActivity> {

    public RAWvernaConfigureMenuAction() {
        super(RAWvernaActivity.class);
    }

    @Override
    protected Action createAction() {
        RAWvernaActivity a = findActivity();
        Action result = null;
        result = new RAWvernaConfigureAction(findActivity(), getParentFrame());
        result.putValue(Action.NAME, "Configure example service");
        addMenuDots(result);
        return result;
    }

}
