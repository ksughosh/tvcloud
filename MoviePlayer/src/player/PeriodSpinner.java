package player;

/**
 * Created by Marcel on 15.07.2014.
 */
public class PeriodSpinner {

    private int minValue;
    private int maxValue;
    private int value;
    private int stepSize;

    public PeriodSpinner(int minValue, int maxValue, int value, int stepSize) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.value = value;
        this.stepSize = stepSize;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getValue() {
        return value;
    }

    public int getStepSize() {
        return stepSize;
    }

    public void setStepSize(int stepSize) {
        this.stepSize = stepSize;
    }

    public void inc() {
        if (value + stepSize <= maxValue)
            value += stepSize;
    }

    public void dec() {
        if (value - stepSize >= minValue)
            value -= stepSize;
    }
}
