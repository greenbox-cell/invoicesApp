package com.invoices.app.data

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object Money {
    fun format(cents: Long, currency: InvoiceCurrency): String {
        val amount = BigDecimal(cents).movePointLeft(2)
        val nf = NumberFormat.getNumberInstance(Locale.US).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }
        return currency.symbol + nf.format(amount)
    }

    fun parseToCents(raw: String): Long? {
        val trimmed = raw.trim().replace(",", "")
        if (trimmed.isEmpty()) return null
        return try {
            BigDecimal(trimmed)
                .setScale(2, RoundingMode.HALF_UP)
                .movePointRight(2)
                .longValueExact()
        } catch (_: RuntimeException) {
            null
        }
    }

    fun centsToInput(cents: Long): String {
        return BigDecimal(cents)
            .movePointLeft(2)
            .setScale(2, RoundingMode.HALF_UP)
            .toPlainString()
    }
}

object Dates {
    private val utc = TimeZone.getTimeZone("UTC")
    private val display = SimpleDateFormat("MMM d, yyyy", Locale.US).apply { timeZone = utc }
    private val pdf = SimpleDateFormat("dd MMM yyyy", Locale.US).apply { timeZone = utc }

    fun formatDisplay(millis: Long): String = display.format(Date(millis))
    fun formatPdf(millis: Long): String = pdf.format(Date(millis))
}
