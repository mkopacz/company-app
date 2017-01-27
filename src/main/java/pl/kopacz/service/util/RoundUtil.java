package pl.kopacz.service.util;

public class RoundUtil {

    public static double roundToNearest005(double x) {
        return roundToNearest(x, 200d);
    }

    private static double roundToNearest(double x, double fraction) {
        return Math.round(x * fraction) / fraction;
    }

}
