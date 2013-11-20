package com.acmetelecom.Utils;

import java.math.BigDecimal;

public class MoneyFormatter {
    public static String penceToPounds(BigDecimal pence) {
        BigDecimal pounds = pence.divide(BigDecimal.valueOf(100));
        return String.format("%.2f", pounds.doubleValue());
    }
}
