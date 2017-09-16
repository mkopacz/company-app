package pl.kopacz.service.util;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;

public class RoundUtil {

    public static BigDecimal roundToNearest005(BigDecimal x) {
        return roundToNearest(x, BigDecimal.valueOf(200));
    }

    private static BigDecimal roundToNearest(BigDecimal x, BigDecimal fraction) {
        BigDecimal multiplied = x.multiply(fraction);
        BigDecimal rounded = multiplied.setScale(0, HALF_UP);
        return rounded.divide(fraction);
    }

}
