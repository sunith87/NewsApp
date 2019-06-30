package com.newsapp.util

import java.text.SimpleDateFormat
import java.util.*

class DateFormatUtil {

    companion object {
        private val DATE_FORMAT = "dd/MM/yyyy"
    }

    fun getFormattedDate(webPublicationDate: Date): String {
        val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.ROOT)
        return dateFormat.format(webPublicationDate)
    }
}