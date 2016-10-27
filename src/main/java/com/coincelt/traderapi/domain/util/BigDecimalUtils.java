package com.coincelt.traderapi.domain.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Convert balance from decimal to long
 */
public class BigDecimalUtils {

    public static final int DECIMAL_SCALE = 8;
    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    private static final BigDecimal SATOSHIS = new BigDecimal(100000000);

    public static Long bigDecimalToLong(BigDecimal bigDecimal) {
        bigDecimal.setScale(DECIMAL_SCALE, ROUNDING_MODE);
        return bigDecimal.multiply(SATOSHIS).longValue();
    }

    public static Long bigDecimalToLongAsAbs(BigDecimal bigDecimal) {
        bigDecimal.setScale(DECIMAL_SCALE, ROUNDING_MODE);
        return bigDecimal.multiply(SATOSHIS).abs().longValue();
    }

    public static BigDecimal longToBigDecimal(long amountAsSatoshis) {
        return new BigDecimal(amountAsSatoshis).divide(SATOSHIS);
    }

    public static BigDecimal textToBigDecimal(String amountAsText){
        return new BigDecimal(amountAsText).setScale(DECIMAL_SCALE, ROUNDING_MODE);
    }

    public static BigDecimal doubleToBigDecimal(double amountAsDouble){
        return new BigDecimal(amountAsDouble).setScale(DECIMAL_SCALE, ROUNDING_MODE);
    }
}
