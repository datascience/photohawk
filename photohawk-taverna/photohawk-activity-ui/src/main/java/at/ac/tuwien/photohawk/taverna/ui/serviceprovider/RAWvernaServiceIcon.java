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

import javax.swing.Icon;
import javax.swing.ImageIcon;

import net.sf.taverna.t2.workbench.activityicons.ActivityIconSPI;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;
import at.ac.tuwien.photohawk.taverna.RAWvernaActivity;

public class RAWvernaServiceIcon implements ActivityIconSPI {

    private static Icon icon;

    public int canProvideIconScore(Activity<?> activity) {
        if (activity instanceof RAWvernaActivity) {
            return DEFAULT_ICON;
        }
        return NO_ICON;
    }

    public Icon getIcon(Activity<?> activity) {
        return getIcon();
    }

    public static Icon getIcon() {
        if (icon == null) {
            icon = new ImageIcon(RAWvernaServiceIcon.class.getResource("/exampleIcon.png"));
        }
        return icon;
    }

}
