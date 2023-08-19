package com.prplmnstr.technoplast.utils

import com.prplmnstr.technoplast.models.Date
import java.text.DateFormatSymbols
import java.util.Calendar

class Helper {
companion object {
    fun getTodayDateObject(): Date {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH] + 1 // Month value is 0-based, so add 1
        val day = calendar[Calendar.DAY_OF_MONTH]
        val date = Date()
        val monthString = DateFormatSymbols().months[month - 1].substring(0, 3)
        val dateInStringFormat = String.format("%d %s,%d", day, monthString, year)
        date.day = day
        date.month = month
        date.year = year
        date.dateInStringFormat = dateInStringFormat
        return date
    }

    fun getDateInStringFormat(day: Int, month: Int, year: Int): String {
        val monthString = DateFormatSymbols().months[month - 1].substring(0, 3)
        return String.format("%d %s,%d", day, monthString, year)
    }
}
}