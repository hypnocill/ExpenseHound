package com.expensehound.app.data.entity

import android.content.Context
import com.expensehound.app.R

enum class Currency {
    BGN,
    USD,
    EUR
}

enum class Category {
    OTHERS,
    CLOTHING,
    BILLS,
    FOOD,
    RENT,
    FUN,
    EDUCATION,
    INVESTMENT,
    HEALTH,
    REPAIRS,
}

enum class RecurringInterval() {
    NONE,
    DAILY,
    WEEKLY,
    MONTHLY
}

fun getCurrencyString(context: Context, currency: Currency): String {
    val activity = context

    return when (currency) {
        Currency.BGN -> activity.getString(R.string.currency_bgn)
        Currency.EUR -> activity.getString(R.string.currency_eur)
        Currency.USD -> activity.getString(R.string.currency_usd)
    }
}

fun getCategoryString(context: Context, category: Category): String {
    val activity = context

    return when (category) {
        Category.OTHERS -> activity.getString(R.string.category_other)
        Category.CLOTHING -> activity.getString(R.string.category_clothing)
        Category.BILLS -> activity.getString(R.string.category_bills)
        Category.FOOD -> activity.getString(R.string.category_food)
        Category.RENT -> activity.getString(R.string.category_rent)
        Category.FUN -> activity.getString(R.string.category_fun)
        Category.EDUCATION -> activity.getString(R.string.category_edu)
        Category.INVESTMENT -> activity.getString(R.string.category_invest)
        Category.HEALTH -> activity.getString(R.string.category_health)
        Category.REPAIRS -> activity.getString(R.string.category_repairs)
    }
}



fun getRecurringIntervalString(context: Context, recurringInterval: RecurringInterval): String {
    val activity = context

    return when (recurringInterval) {
        RecurringInterval.NONE -> activity.getString(R.string.recurring_interval_none)
        RecurringInterval.DAILY -> activity.getString(R.string.recurring_interval_daily)
        RecurringInterval.WEEKLY -> activity.getString(R.string.recurring_interval_weekly)
        RecurringInterval.MONTHLY -> activity.getString(R.string.recurring_interval_monthly)
    }
}


