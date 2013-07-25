/**
 * 
 */
package at.ac.tuwien.photohawk.taverna.evaluation.evaluators;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Scanner;

import at.ac.tuwien.photohawk.taverna.evaluation.IEvaluator;
import at.ac.tuwien.photohawk.taverna.model.evaluation.MeasurementsDescriptor;

/**
 * @author kraxner
 * 
 */
public class EvaluatorBase implements IEvaluator {
    protected MeasurementsDescriptor descriptor;
    protected String descriptorStr;

    /**
     * @see at.ac.tuwien.photohawk.taverna.evaluation.IEvaluator#getPossibleMeasurements()
     */
    public String getPossibleMeasurements() {
        return descriptorStr;
    }

    /**
     * loads measurements description from the given file. populates descriptor
     * and descriptor String
     * 
     * @param filename
     * @return
     */
    protected boolean loadMeasurementsDescription(String name) {
        try {
            // InputStream description =
            // Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
            InputStream description = getClass().getClassLoader().getResourceAsStream(name);
            if (description != null) {
                InputStreamReader in = new InputStreamReader(description);
                descriptorStr = new Scanner(in).useDelimiter("\\A").next();
                descriptor = new MeasurementsDescriptor();
                descriptor.addMeasurementInfos(new StringReader(descriptorStr));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
