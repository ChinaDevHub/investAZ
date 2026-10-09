package com.example.investaz.data.mapper

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

/** java.time is unavailable below API 26, so ISO-8601 is parsed with a per-thread SimpleDateFormat. */
internal object IsoTimestampParser {

    private val format = ThreadLocal.withInitial {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }

    fun toEpochMillis(iso: String): Long? = try {
        format.get()?.parse(iso)?.time
    } catch (e: ParseException) {
        null
    }
}
