package at.ac.tuwien.photohawk.evaluation.colorconverter;

/**
 * Class representing an abstract static color.
 */
public abstract class AbstractStaticColor implements StaticColor {

    protected float[] channelValues;

    /**
     * Creates a new AbstractStaticColor.
     * 
     * @param values
     *            the values of this color
     */
    protected AbstractStaticColor(float[] values) {
        setChannelValues(values);
    }

    @Override
    public float[] getChannelValues() {
        return channelValues;
    }

    @Override
    public float getChannelValue(int idx) {
        return channelValues[idx];
    }

    @Override
    public void setChannelValues(float[] channelValues) {
        this.channelValues = channelValues;
    }

    @Override
    public void setChannelValue(int idx, float channelValue) {
        channelValues[idx] = channelValue;
    }

    @Override
    public int getNumberOfChannels() {
        return getChannelDescription().length;
    }

    @Override
    public String getChannelDescription(int idx) {
        return getChannelDescription()[idx];
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < channelValues.length; i++) {
            result += getChannelDescription(i) + ": " + channelValues[i] + "\n";
        }
        return result;
    }
}
