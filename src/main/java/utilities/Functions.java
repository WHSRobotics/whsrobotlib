package utilities;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.min;

public class Functions {
    /**
     * Limit a value between a minimum and an maximum
     * @param input The value to limit
     * @param min The minimum boundary value
     * @param max The maximum boundary value
     * @return The new clamped value
     */
    public static double clamp(double input, double min, double max){
        return Math.min(Math.max(input, min), max);
    }

    /**
     * Scale a value between a minimum and maximum
     * @param input Value to scale
     * @param min The minimum boundary value
     * @param max The maximum boundary value
     * @return The new scaled value
     * @see #map(double, double, double, double, double)
     */
    public double map(double input, double min, double max){
        if(input < min) return min;
        if(input > max) return max;
        return (input-min)*(max-min)/(max-min) + min;
    }

    /**
     * Scale a map a value between two input boundaries to two output boundaries
     * @param input Value to scale
     * @param in_min The minimum input value
     * @param in_max The maximum input value
     * @param out_min The minimm output value
     * @param out_max The maximum output value
     * @return
     * @see #map(double, double, double)
     */
    public double map(double input, double in_min, double in_max, double out_min, double out_max){
        if(in_min >= in_max) throw new IllegalArgumentException("in_min cannot be greater than in_max");
        if(out_min >= out_max) throw new IllegalArgumentException("out_min cannot be greater than out_max");
        if(input <= in_min) return out_min;
        if (input >= in_max) return out_max;
        try {
            return (input - in_min)*(out_max-out_min)/(in_max - in_min) + out_min;
        } catch(ArithmeticException e){ } //the rare case that both out_min and out_max are both 0
        return 0;
    }

    public <T> ArrayList<T> asArrayList(T[] arr){
        return new ArrayList<T>(Arrays.asList(arr));
    }

}
