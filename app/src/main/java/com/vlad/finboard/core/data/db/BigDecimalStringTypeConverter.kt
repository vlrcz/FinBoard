package com.vlad.finboard.core.data.db

import androidx.room.TypeConverter
import java.math.BigDecimal

class BigDecimalStringTypeConverter {

    @TypeConverter
    fun bigDecimalToString(input: BigDecimal): String {
        return input.toPlainString()
    }

    @TypeConverter
    fun stringToBigDecimal(input: String): BigDecimal {
        return input.toBigDecimal()
    }
}