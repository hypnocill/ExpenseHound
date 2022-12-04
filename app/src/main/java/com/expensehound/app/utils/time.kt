package com.expensehound.app.utils

import java.text.SimpleDateFormat
import java.util.*

fun getStartOfMonthAsTimestamp(): Long {
    val c = Calendar.getInstance();
    c.set(Calendar.DAY_OF_MONTH, 1);

    return c.timeInMillis
}

fun convertTimestampToDatetimeString(timestamp: Long): String {
    val simpleDate = SimpleDateFormat("dd/M/yyyy HH:mm:ss")

    return simpleDate.format(timestamp) ?: ""
}
