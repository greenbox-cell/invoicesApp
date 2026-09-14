package com.invoices.app.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "invoices",
    indices = [Index("dateMillis"), Index("status")]
)
data class Invoice(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val clientName: String,
    val dateMillis: Long,
    /** Amount stored as integer cents to avoid floating-point rounding issues. */
    val amountCents: Long,
    val currencyCode: String,
    val description: String,
    val status: String,
    val createdAt: Long,
    val updatedAt: Long
) {
    val currency: InvoiceCurrency get() = InvoiceCurrency.fromCode(currencyCode)
    val invoiceStatus: InvoiceStatus get() = InvoiceStatus.fromStorage(status)
    val number: String get() = "INV-" + id.toString().padStart(4, '0')
}
