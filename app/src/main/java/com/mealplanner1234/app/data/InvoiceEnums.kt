package com.mealplanner1234.app.data

/**
 * Supported invoice currencies. Symbols are used in the UI and on generated PDFs.
 */
enum class InvoiceCurrency(val code: String, val symbol: String, val label: String) {
    USD("USD", "$", "US Dollar ($)"),
    ILS("ILS", "₪", "Israeli Shekel (₪)");

    companion object {
        fun fromCode(code: String): InvoiceCurrency =
            entries.firstOrNull { it.code == code } ?: USD
    }
}

enum class InvoiceStatus(val storageValue: String, val label: String) {
    PENDING("PENDING", "Pending"),
    PAID("PAID", "Paid");

    companion object {
        fun fromStorage(value: String): InvoiceStatus =
            entries.firstOrNull { it.storageValue == value } ?: PENDING
    }
}

enum class InvoiceFilter { ALL, PENDING, PAID }
