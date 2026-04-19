package com.example.expens_tracker.Data

import android.content.ContentValues
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.example.expens_tracker.API.Models.Expense
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfGenerator {

    fun createExpensePdf(context: Context, expenses: List<Expense>) {
        val pdfDocument = PdfDocument()
        val paint = Paint()
        val titlePaint = Paint().apply {
            textSize = 24f
            isFakeBoldText = true
        }

        // A4 size
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        // Header
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val nowString = dateFormat.format(Date())

        canvas.drawText("Expense Report", 40f, 50f, titlePaint)
        paint.textSize = 14f
        canvas.drawText("Generated on: $nowString", 40f, 80f, paint)

        // Table headers
        var yPosition = 120f
        paint.isFakeBoldText = true
        canvas.drawText("Date", 40f, yPosition, paint)
        canvas.drawText("Description", 150f, yPosition, paint)
        canvas.drawText("Amount", 450f, yPosition, paint)

        canvas.drawLine(40f, yPosition + 10f, 550f, yPosition + 10f, paint)
        paint.isFakeBoldText = false

        // Expense rows
        yPosition += 40f
        val maxY = 800f

        expenses.forEach { expense ->
            if (yPosition > maxY) return@forEach

            canvas.drawText(expense.expense_date, 40f, yPosition, paint)
            canvas.drawText(expense.description ?: "No description", 150f, yPosition, paint)
            canvas.drawText("$${String.format("%.2f", expense.amount)}", 450f, yPosition, paint)

            yPosition += 30f
        }

        pdfDocument.finishPage(page)

        // Save using MediaStore
        val fileName = "Expense_Report_${System.currentTimeMillis()}.pdf"

        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
            put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        try {
            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

            if (uri == null) {
                Toast.makeText(context, "Error: Unable to create file URI", Toast.LENGTH_SHORT).show()
                pdfDocument.close()
                return
            }

            resolver.openOutputStream(uri)?.use { outputStream ->
                pdfDocument.writeTo(outputStream)
            } ?: run {
                Toast.makeText(context, "Error: Unable to open output stream", Toast.LENGTH_SHORT).show()
                pdfDocument.close()
                return
            }

            Toast.makeText(context, "PDF saved to Downloads as $fileName", Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            pdfDocument.close()
        }
    }
}
