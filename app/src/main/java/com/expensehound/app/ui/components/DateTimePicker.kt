package com.expensehound.app.ui.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import java.util.Calendar

class DateTimePicker{
    companion object {
        fun showDateTimePicker(context: Context, onDateTimePicked: (result: Calendar) -> Unit) {
            val currentDate: Calendar = Calendar.getInstance()
            val date = Calendar.getInstance()
            DatePickerDialog(context,
                { view, year, monthOfYear, dayOfMonth ->
                    date.set(year, monthOfYear, dayOfMonth)

                    TimePickerDialog(context,
                        { view, hourOfDay, minute ->
                            date.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            date.set(Calendar.MINUTE, minute)
                            onDateTimePicked(date)
                        },
                        currentDate.get(Calendar.HOUR_OF_DAY),
                        currentDate.get(Calendar.MINUTE),
                        true
                    ).show()
                },
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DATE)
            ).show()
        }
    }

}