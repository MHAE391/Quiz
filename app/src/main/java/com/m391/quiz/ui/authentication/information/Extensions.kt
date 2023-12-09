package com.m391.quiz.ui.authentication.information

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import java.util.Calendar
import java.util.Locale

fun setDateOfBarth(context: Context, dateOfBarth: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val years = calendar.get(Calendar.YEAR)
    val months = calendar.get(Calendar.MONTH)
    val days = calendar.get(Calendar.DAY_OF_MONTH)
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, day: Int ->
            dateOfBarth("$day/${month + 1}/$year")
        },
        years,
        months,
        days
    )
    datePickerDialog.show()
}

