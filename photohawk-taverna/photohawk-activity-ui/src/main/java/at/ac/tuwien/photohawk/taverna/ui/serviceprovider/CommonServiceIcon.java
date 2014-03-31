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

import at.ac.tuwien.photohawk.taverna.QaActivity;
import net.sf.taverna.t2.workbench.activityicons.ActivityIconSPI;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

/**
 * Icon for services.
 */
public class CommonServiceIcon implements ActivityIconSPI {

    private static final String ICON_PATH = "/hawkeye-icon.png";

    private static Icon icon;

    @Override
    public int canProvideIconScore(Activity<?> activity) {
        if (activity instanceof QaActivity) {
            return DEFAULT_ICON;
        }
        return NO_ICON;
    }

    @Override
    public Icon getIcon(Activity<?> activity) {
        return getIcon();
    }

    /**
     * Returns the icon.
     * 
     * @return the icon
     */
    public static Icon getIcon() {
        if (icon == null) {
            icon = new ImageIcon(CommonServiceIcon.class.getResource(ICON_PATH));
        }
        return icon;
    }

}
