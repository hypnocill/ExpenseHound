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

fun isMonday(calendar: Calendar): Boolean {
    val day = calendar.get(Calendar.DAY_OF_WEEK);

    return day == Calendar.MONDAY
}

fun isFirstDayOfMonth(calendar: Calendar): Boolean {
    return calendar.get(Calendar.DAY_OF_MONTH) == 1
}