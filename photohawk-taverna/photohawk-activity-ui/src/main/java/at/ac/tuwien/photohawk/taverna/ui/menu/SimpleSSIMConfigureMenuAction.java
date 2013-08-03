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

import at.ac.tuwien.photohawk.taverna.SimpleSSIMActivity;
import at.ac.tuwien.photohawk.taverna.ui.config.SimpleSSIMConfigureAction;

/**
 * Creates a new configure menu for SSIM.
 */
public class SimpleSSIMConfigureMenuAction extends AbstractConfigureActivityMenuAction<SimpleSSIMActivity> {

    /**
     * Creates a new instance.
     */
    public SimpleSSIMConfigureMenuAction() {
        super(SimpleSSIMActivity.class);
    }

    @Override
    protected Action createAction() {
        Action result = new SimpleSSIMConfigureAction(findActivity(), getParentFrame());
        result.putValue(Action.NAME, "SSIM configuration");
        addMenuDots(result);
        return result;
    }
}
