package com.expensehound.app.utils

import java.util.*

fun getStartOfMonthAsTimestamp(): Long {
    val c = Calendar.getInstance();
    c.set(Calendar.DAY_OF_MONTH, 1);

    return c.timeInMillis
}