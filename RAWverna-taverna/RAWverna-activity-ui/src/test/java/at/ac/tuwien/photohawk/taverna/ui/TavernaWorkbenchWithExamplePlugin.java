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
package at.ac.tuwien.photohawk.taverna.ui;

import net.sf.taverna.t2.workbench.dev.DeveloperWorkbench;

/**
 * Run with parameters:
 * 
 * -Xmx300m -XX:MaxPermSize=140m -Dsun.swing.enableImprovedDragGesture
 * -Dtaverna.startup=.
 * 
 * NOTE: Do not save any workflows made using this test mode, as the plugin
 * information will be missing from the workflow file, and it will not open in a
 * Taverna run normally.
 * 
 */
public class TavernaWorkbenchWithExamplePlugin {
    public static void main(String[] args) throws Exception {
        DeveloperWorkbench.main(args);
    }
}
