package com.prplmnstr.technoplast.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import com.itextpdf.text.PageSize
import com.prplmnstr.technoplast.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class CreatePdf {

    var height = 1025

    val width = PageSize.A4.height.toInt()


    fun createPdf(context: Context, activity: Activity) {
        //val fileName = "${plantReport.plantName}_${recordDate.getDateInStringFormat()}"
        val fileName = "test"
        val stringFilePath =
            Environment.getExternalStorageDirectory().path + "/Download/$fileName.pdf"
        val file = File(stringFilePath)

        val pdfDocument = PdfDocument()

// title
        val titlePaint = Paint().apply {
            textAlign = Paint.Align.CENTER
            textSize = 24f
            typeface = ResourcesCompat.getFont(context, R.font.segeo_bold
            )
        }
        val subTitlePaint = Paint().apply {
            textAlign = Paint.Align.CENTER
            textSize = 18f
            typeface = ResourcesCompat.getFont(context, R.font.segeo_bold
            )
        }

        val datePaint = Paint().apply {
            textAlign = Paint.Align.CENTER
            textSize = 14f
            typeface = ResourcesCompat.getFont(context, R.font.segeo_semi_bold)
        }

        val linePaint = Paint().apply {
            textAlign = Paint.Align.CENTER
            textSize = 32f
            typeface = ResourcesCompat.getFont(context, R.font.segeo_bold)
        }
            val myPageInfo = PdfDocument.PageInfo.Builder(width, height, 1).create()
            val page1 = pdfDocument.startPage(myPageInfo)
            val canvas = page1.canvas


        val recordsHeight: Float = (60 + 10 * 25).toFloat()


        val columnWidth = ((width - 40) / 4).toFloat()
        val textWidth = 20 + columnWidth / 2
        var textStart = textWidth
        var columnStart = 20f
        for (i in 0..3) {
            columnStart = columnStart + columnWidth

            textStart = textStart + columnWidth
            canvas.drawLine(columnStart, 120f, columnStart, recordsHeight, linePaint)
        }
        canvas.drawLine(columnStart, 35f, columnStart, 120f, linePaint)
            //writing start
        //heading
        canvas.drawLine(20f, 35f, columnStart, 35f, linePaint)
        canvas.drawText("TECHNO PLAST", (width/2).toFloat(), 70f, titlePaint)
        canvas.drawLine(20f, 85f, columnStart, 85f, linePaint)

        // subHeading
        canvas.drawText("MACHINE PRODUCTION SHEET(MPS)", (width/2).toFloat(), 110f, subTitlePaint)
        canvas.drawLine(20f, 120f, columnStart, 120f, linePaint)


            // 1st line
            canvas.drawLine(20f, 35f, 20f, recordsHeight, linePaint)



            // writing end

            pdfDocument.finishPage(page1)

            try {
                FileOutputStream(file).use { outputStream ->
                    pdfDocument.writeTo(outputStream)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            pdfDocument.close()

            Toast.makeText(context, "Downloaded in /Downloads folder.", Toast.LENGTH_LONG).show()


            val intent = Intent(Intent.ACTION_VIEW)
            var uri: Uri = Uri.fromFile(file)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(
                    context,
                    "${context.applicationContext.packageName}.provider",
                    file
                )
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            intent.setDataAndType(uri, "application/pdf")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            try {
                activity.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    context,
                    "No application found to open the PDF file.",
                    Toast.LENGTH_LONG
                ).show()
            }


    }
}