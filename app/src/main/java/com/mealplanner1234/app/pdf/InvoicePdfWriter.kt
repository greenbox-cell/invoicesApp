package com.mealplanner1234.app.pdf

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.mealplanner1234.app.data.Dates
import com.mealplanner1234.app.data.Invoice
import com.mealplanner1234.app.data.Money
import java.io.File

/**
 * Draws a single-page A4 invoice using the platform PdfDocument API (no third-party PDF SDK).
 */
object InvoicePdfWriter {

    private const val PAGE_WIDTH = 595
    private const val PAGE_HEIGHT = 842
    private const val MARGIN = 48f
    private const val BRAND = 0xFF0E6B56.toInt()
    private const val INK = 0xFF1C1917.toInt()
    private const val MUTED = 0xFF57534E.toInt()

    fun write(context: Context, invoice: Invoice): File {
        val dir = File(context.cacheDir, "invoices").apply { mkdirs() }
        val file = File(dir, "${invoice.number}.pdf")

        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
        val page = document.startPage(pageInfo)
        draw(page.canvas, invoice)
        document.finishPage(page)

        file.outputStream().use { document.writeTo(it) }
        document.close()
        return file
    }

    private fun draw(canvas: Canvas, invoice: Invoice) {
        val titlePaint = paint(22f, true, Color.WHITE)
        val brandPaint = paint(11f, false, Color.WHITE)
        val headingPaint = paint(18f, true, INK)
        val labelPaint = paint(10f, true, MUTED)
        val bodyPaint = paint(12f, false, INK)
        val amountPaint = paint(20f, true, BRAND)

        // Header bar
        val headerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = BRAND }
        canvas.drawRect(0f, 0f, PAGE_WIDTH.toFloat(), 110f, headerPaint)
        canvas.drawText("INVOICE", MARGIN, 52f, titlePaint)
        canvas.drawText("Invoices  ·  ${invoice.number}", MARGIN, 78f, brandPaint)

        var y = 150f
        y = labeledValue(canvas, "Bill to", invoice.clientName, MARGIN, y, labelPaint, headingPaint)
        y = labeledValue(
            canvas,
            "Date",
            Dates.formatPdf(invoice.dateMillis),
            MARGIN + 280f,
            150f,
            labelPaint,
            bodyPaint
        )
        labeledValue(
            canvas,
            "Status",
            invoice.invoiceStatus.label,
            MARGIN + 280f,
            190f,
            labelPaint,
            bodyPaint
        )

        y = 250f
        canvas.drawText("DESCRIPTION", MARGIN, y, labelPaint)
        y += 8f
        val rule = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFFE7E5E4.toInt()
            strokeWidth = 1.5f
        }
        canvas.drawLine(MARGIN, y, PAGE_WIDTH - MARGIN, y, rule)

        y += 18f
        val description = invoice.description.ifBlank { "—" }
        val textPaint = TextPaint(bodyPaint)
        val layout = StaticLayout.Builder
            .obtain(description, 0, description.length, textPaint, (PAGE_WIDTH - 2 * MARGIN).toInt())
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(0f, 1.2f)
            .build()
        canvas.save()
        canvas.translate(MARGIN, y)
        layout.draw(canvas)
        canvas.restore()

        y += layout.height + 36f
        canvas.drawLine(MARGIN, y, PAGE_WIDTH - MARGIN, y, rule)
        y += 36f

        canvas.drawText("AMOUNT", MARGIN, y, labelPaint)
        canvas.drawText(
            Money.format(invoice.amountCents, invoice.currency),
            MARGIN,
            y + 28f,
            amountPaint
        )

        val footer = paint(9f, false, MUTED)
        canvas.drawText(
            "Generated locally by Invoices. This document is not stored in the cloud.",
            MARGIN,
            PAGE_HEIGHT - 36f,
            footer
        )
    }

    private fun labeledValue(
        canvas: Canvas,
        label: String,
        value: String,
        x: Float,
        y: Float,
        labelPaint: Paint,
        valuePaint: Paint
    ): Float {
        canvas.drawText(label.uppercase(), x, y, labelPaint)
        canvas.drawText(value, x, y + 22f, valuePaint)
        return y + 48f
    }

    private fun paint(size: Float, bold: Boolean, colorInt: Int) = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = size
        color = colorInt
        typeface = Typeface.create(Typeface.SANS_SERIF, if (bold) Typeface.BOLD else Typeface.NORMAL)
    }
}
