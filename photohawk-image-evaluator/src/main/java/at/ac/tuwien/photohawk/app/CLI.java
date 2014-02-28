/*
 * Copyright 2014 artur.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.ac.tuwien.photohawk.app;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author artur
 */
public class CLI {

    public static void main(String[] args) {
        if (args.length < 1 || args[0] == null) {
            System.out
                    .println("Please provide paths to raw images");
            System.out
                    .println("e.g.: java -jar profiler.jar mode /home/user/image1.nef /home/user/image2.dng");
            System.out
                    .println("      where mode = ssim, ae, pae, mae or mse");
            return;
        }
        String[] input={args[0],args[1],args[2]};
        try {
            Processor p=new Processor();
            Float result=p.run(input[0],input[1],input[2]);
            System.out.println(result);
        } catch (IOException ex) {
            Logger.getLogger(CLI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }
    
    

}
