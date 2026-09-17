package com.mealplanner1234.app.pdf

import android.content.ClipData
import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.mealplanner1234.app.data.Invoice

object PdfShare {
    fun share(context: Context, invoice: Invoice) {
        val file = InvoicePdfWriter.write(context, invoice)
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            clipData = ClipData.newRawUri(invoice.number, uri)
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, invoice.number)
            putExtra(Intent.EXTRA_TEXT, "Invoice ${invoice.number} for ${invoice.clientName}")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share invoice PDF"))
    }
}
