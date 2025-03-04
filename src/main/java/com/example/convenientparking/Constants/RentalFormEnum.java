package com.example.convenientparking.Constants;

import lombok.AllArgsConstructor;
@AllArgsConstructor
public enum RentalFormEnum {
    HOUR(1),
    DAY(2),
    MONTH(3),
    YEAR(4);
    public final long value;
}
