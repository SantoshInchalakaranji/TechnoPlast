package com.prplmnstr.technoplast.utils

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import com.itextpdf.text.PageSize

import com.prplmnstr.technoplast.R
import com.prplmnstr.technoplast.models.Record
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class CreatePdf {

    var height = 1025

    val width = PageSize.A4.height.toInt()
    private val REQUEST_STORAGE_PERMISSION = 1

    fun createPdf(context: Context, activity: Activity, dayRecord: Record,nightRecord: Record) {


        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_STORAGE_PERMISSION
            )
        } else {
                var fileName ="TechnoPlast"
            val headingsAns2: MutableList<String> = ArrayList()
            val headingsAns1: MutableList<String> = ArrayList()

            if(!dayRecord.name.isEmpty()){
                headingsAns1.add(dayRecord.name)
                headingsAns1.add(dayRecord.productionWT)
                headingsAns1.add(dayRecord.article)
                headingsAns1.add(dayRecord.loadTime)
                headingsAns1.add(dayRecord.numCavity)
                headingsAns1.add("")
                headingsAns1.add(dayRecord.rawMaterial)
                headingsAns1.add(dayRecord.pigment)


                headingsAns2.add(dayRecord.mould)
                headingsAns2.add(dayRecord.date)
                headingsAns2.add(dayRecord.orderQty)
                headingsAns2.add(dayRecord.unloadTime)
                headingsAns2.add(dayRecord.heating)
                headingsAns2.add(dayRecord.heatingAct)
                headingsAns2.add(dayRecord.totalMaterialUsed)
                headingsAns2.add(dayRecord.totalMbUsed)
                if(!nightRecord.name.isEmpty()){
                    fileName = dayRecord.name+"_"+dayRecord.date+"_"+dayRecord.shift+"&"+nightRecord.shift
                }else{
                    fileName = dayRecord.name+"_"+dayRecord.date+"_"+dayRecord.shift
                }
            }else
            {

                fileName = nightRecord.name+"_"+nightRecord.date+"_"+nightRecord.shift

                headingsAns1.add(nightRecord.name)
                headingsAns1.add(nightRecord.productionWT)
                headingsAns1.add(nightRecord.article)
                headingsAns1.add(nightRecord.loadTime)
                headingsAns1.add(nightRecord.numCavity)
                headingsAns1.add("")
                headingsAns1.add(nightRecord.rawMaterial)
                headingsAns1.add(nightRecord.pigment)


                headingsAns2.add(nightRecord.mould)
                headingsAns2.add(nightRecord.date)
                headingsAns2.add(nightRecord.orderQty)
                headingsAns2.add(nightRecord.unloadTime)
                headingsAns2.add(nightRecord.heating)
                headingsAns2.add(nightRecord.heatingAct)
                headingsAns2.add(nightRecord.totalMaterialUsed)
                headingsAns2.add(nightRecord.totalMbUsed)
            }

            val stringFilePath =
                Environment.getExternalStorageDirectory().path + "/Download/$fileName.pdf"
            val file = File(stringFilePath)

            val pdfDocument = PdfDocument()

// title
            val titlePaint = Paint().apply {
                textAlign = Paint.Align.CENTER
                textSize = 24f
                typeface = ResourcesCompat.getFont(
                    context, R.font.segeo_bold
                )
            }
            val headingPaint = Paint().apply {
                textAlign = Paint.Align.LEFT
                textSize = 14f
                typeface = ResourcesCompat.getFont(
                    context, R.font.segeo_semi_bold
                )
            }
            val subTitlePaint = Paint().apply {
                textAlign = Paint.Align.CENTER
                textSize = 18f
                typeface = ResourcesCompat.getFont(
                    context, R.font.segeo_bold
                )
            }

            val shiftPaint = Paint().apply {
                textAlign = Paint.Align.CENTER
                textSize = 16f
                typeface = ResourcesCompat.getFont(
                    context, R.font.segeo_semi_bold
                )
            }

            val textPaint = Paint().apply {
                textAlign = Paint.Align.CENTER
                textSize = 14f
                typeface = ResourcesCompat.getFont(context, R.font.segeo_regular)
            }

            val linePaint = Paint().apply {
                textAlign = Paint.Align.CENTER
                textSize = 32f
                typeface = ResourcesCompat.getFont(context, R.font.segeo_bold)
            }
            val myPageInfo = PdfDocument.PageInfo.Builder(width, height, 1).create()
            val page1 = pdfDocument.startPage(myPageInfo)
            val canvas = page1.canvas


            val recordsHeight: Float = (120 + 8 * 25).toFloat()


            val columnWidth = ((width - 40) / 4).toFloat()
            val textWidth = 30f
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
            canvas.drawText("TECHNO PLAST", (width / 2).toFloat(), 70f, titlePaint)
            canvas.drawLine(20f, 85f, columnStart, 85f, linePaint)

            // subHeading
            canvas.drawText(
                "MACHINE PRODUCTION SHEET(MPS)",
                (width / 2).toFloat(),
                110f,
                subTitlePaint
            )
            canvas.drawLine(20f, 120f, columnStart, 120f, linePaint)


            // 1st line
            canvas.drawLine(20f, 35f, 20f, recordsHeight, linePaint)


// start line

            //end line
            val headings1: MutableList<String> = ArrayList()
            headings1.add("NAME OF MACHINE")
            headings1.add("PRODUCTION WT")
            headings1.add("ACT WT OF ARTICLE")
            headings1.add("MOULD LOAD TIME")
            headings1.add("NO OF CAVITY")
            headings1.add("")
            headings1.add("RAW MATERIAL")
            headings1.add("MB/PIGMENT")






            val headings2: MutableList<String> = ArrayList()
            headings2.add("MOULD NAME")
            headings2.add("DATE")
            headings2.add("PO(ORDER QTY)")
            headings2.add("MOULD UNLOAD TIME")
            headings2.add("HEATING")
            headings2.add("HEATING act")
            headings2.add("TOTAL MATERIAL USED(KG)")
            headings2.add("TOTAL MB USED(KG)")

            val timingList: MutableList<String> = ArrayList()
            timingList.add("09:00 - 10:00")
            timingList.add("10:00 - 11:00")
            timingList.add("11:00 - 12:00")
            timingList.add("12:00 - 01:00")
            timingList.add("01:00 - 02:00")
            timingList.add("02:00 - 03:00")
            timingList.add("03:00 - 03:00")
            timingList.add("04:00 - 05:00")
            timingList.add("05:00 - 06:00")
            timingList.add("06:00 - 07:00")
            timingList.add("07:00 - 08:00")
            timingList.add("08:00 - 09:00")

            val timingListAns1: MutableList<String> = ArrayList()
            timingListAns1.add(dayRecord.one.toString())
            timingListAns1.add(dayRecord.two.toString())
            timingListAns1.add(dayRecord.three.toString())
            timingListAns1.add(dayRecord.four.toString())
            timingListAns1.add(dayRecord.five.toString())
            timingListAns1.add(dayRecord.six.toString())
            timingListAns1.add(dayRecord.seven.toString())
            timingListAns1.add(dayRecord.eight.toString())
            timingListAns1.add(dayRecord.nine.toString())
            timingListAns1.add(dayRecord.ten.toString())
            timingListAns1.add(dayRecord.eleven.toString())
            timingListAns1.add(dayRecord.twelve.toString())

            val timingListAns2: MutableList<String> = ArrayList()
            timingListAns2.add(nightRecord.one.toString())
            timingListAns2.add(nightRecord.two.toString())
            timingListAns2.add(nightRecord.three.toString())
            timingListAns2.add(nightRecord.four.toString())
            timingListAns2.add(nightRecord.five.toString())
            timingListAns2.add(nightRecord.six.toString())
            timingListAns2.add(nightRecord.seven.toString())
            timingListAns2.add(nightRecord.eight.toString())
            timingListAns2.add(nightRecord.nine.toString())
            timingListAns2.add(nightRecord.ten.toString())
            timingListAns2.add(nightRecord.eleven.toString())
            timingListAns2.add(nightRecord.twelve.toString())

            val totalHeadingList: MutableList<String> = ArrayList()
            totalHeadingList.add("QTY")
            totalHeadingList.add("BAG")
            totalHeadingList.add("COUNTER START")
            totalHeadingList.add("COUNTER END")
            totalHeadingList.add("OPEATOR NAME")
            totalHeadingList.add("OPEATOR SIGN")
            totalHeadingList.add("LUMP(GATTA) KG")
            totalHeadingList.add("GRINDING MATERIAL LEFT")
            totalHeadingList.add("SUMMARY")

            val totalHeadingListAns1: MutableList<String> = ArrayList()
            totalHeadingListAns1.add(dayRecord.qty.toString())
            totalHeadingListAns1.add(dayRecord.bag.toString())
            totalHeadingListAns1.add(dayRecord.start)
            totalHeadingListAns1.add(dayRecord.end)
            totalHeadingListAns1.add(dayRecord.operator)
            totalHeadingListAns1.add("")
            totalHeadingListAns1.add(dayRecord.lump.toString())
            totalHeadingListAns1.add(dayRecord.grindingMaterialLeft.toString())
            if(dayRecord.reason.length>33)
                dayRecord.reason = dayRecord.reason.substring(0,33)+"..."
            totalHeadingListAns1.add(dayRecord.reason)


            val totalHeadingListAns2: MutableList<String> = ArrayList()
            totalHeadingListAns2.add(nightRecord.qty.toString())
            totalHeadingListAns2.add(nightRecord.bag.toString())
            totalHeadingListAns2.add(nightRecord.start)
            totalHeadingListAns2.add(nightRecord.end)
            totalHeadingListAns2.add(nightRecord.operator)
            totalHeadingListAns2.add("")
            totalHeadingListAns2.add(nightRecord.lump.toString())
            totalHeadingListAns2.add(nightRecord.grindingMaterialLeft.toString())
            if(nightRecord.reason.length>33)
                nightRecord.reason = nightRecord.reason.substring(0,33)+"..."
            totalHeadingListAns2.add(nightRecord.reason)


            var horizontalLineStart = 120f

            var textHorizontal = 137f

            textStart = textWidth
            for (i in 0..7) {
                horizontalLineStart += 25f
                canvas.drawText(headings1.get(i), textStart, textHorizontal, headingPaint)
                canvas.drawText(headingsAns1.get(i), textStart+columnWidth, textHorizontal, headingPaint)
                canvas.drawText(
                    headings2.get(i),
                    textStart + 2 * columnWidth,
                    textHorizontal,
                    headingPaint
                )
                canvas.drawText(headingsAns2.get(i), textStart+columnWidth*3, textHorizontal, headingPaint)

                textHorizontal = horizontalLineStart + 17
                canvas.drawLine(
                    20f,
                    horizontalLineStart,
                    columnStart,
                    horizontalLineStart,
                    linePaint
                )
            }



            horizontalLineStart += 25f

            textHorizontal += 27f

            // shift details
            canvas.drawLine(20f, horizontalLineStart, columnStart, horizontalLineStart, linePaint)
            canvas.drawText("DAY", (textStart - 10) + columnWidth, textHorizontal, shiftPaint)
            canvas.drawLine(
                (textStart - 10) + columnWidth,
                horizontalLineStart + 30,
                (textStart - 10) + columnWidth,
                horizontalLineStart + 60,
                linePaint
            )
            canvas.drawLine(
                (textStart - 10) + columnWidth * 3,
                horizontalLineStart + 30,
                (textStart - 10) + columnWidth * 3,
                horizontalLineStart + 60,
                linePaint
            )
            canvas.drawLine(20f, horizontalLineStart, 20f, horizontalLineStart + 60, linePaint)
            canvas.drawLine(
                20 + columnWidth * 2,
                horizontalLineStart,
                20 + columnWidth * 2,
                horizontalLineStart + 60,
                linePaint
            )
            canvas.drawLine(
                columnStart,
                horizontalLineStart,
                columnStart,
                horizontalLineStart + 60,
                linePaint
            )
            canvas.drawText("NIGHT", (textStart - 10) + columnWidth * 3, textHorizontal, shiftPaint)
            horizontalLineStart += 30f
            textHorizontal += 30f
            canvas.drawText("TIMING", textStart, textHorizontal, headingPaint)
            canvas.drawText("TIMING", textStart + columnWidth * 2, textHorizontal, headingPaint)
            canvas.drawText("PRODUCTION/HR", textStart + columnWidth, textHorizontal, headingPaint)
            canvas.drawText(
                "PRODUCTION/HR",
                textStart + columnWidth * 3,
                textHorizontal,
                headingPaint
            )
            canvas.drawLine(20f, horizontalLineStart, columnStart, horizontalLineStart, linePaint)
            horizontalLineStart += 30f
            textHorizontal += 30f
            var newHorizontaLineStart = horizontalLineStart

            canvas.drawLine(20f, horizontalLineStart, columnStart, horizontalLineStart, linePaint)



            textStart = textWidth
            for (i in 0..11) {
                horizontalLineStart += 25f
                canvas.drawText(timingList.get(i), textStart, textHorizontal, headingPaint)
                canvas.drawText(
                    timingList.get(i),
                    textStart + 2 * columnWidth,
                    textHorizontal,
                    headingPaint
                )
                canvas.drawText(timingListAns1.get(i), textStart+columnWidth, textHorizontal, headingPaint)
                canvas.drawText(timingListAns2.get(i), textStart+columnWidth*3, textHorizontal, headingPaint)
                textHorizontal = horizontalLineStart + 17
                canvas.drawLine(
                    20f,
                    horizontalLineStart,
                    columnStart,
                    horizontalLineStart,
                    linePaint
                )
            }

            //vertical lines for day night table
            columnStart = 20f
            for (i in 0..4) {
                canvas.drawLine(
                    columnStart,
                    newHorizontaLineStart,
                    columnStart,
                    newHorizontaLineStart + (12 * 25).toFloat(),
                    linePaint
                )
                columnStart = columnStart + columnWidth
            }

            horizontalLineStart += 25f
            columnStart = columnStart - columnWidth
            textHorizontal += 27f

            canvas.drawLine(20f, horizontalLineStart, columnStart, horizontalLineStart, linePaint)
            canvas.drawText("DAY TOTAL", (textStart - 10) + columnWidth, textHorizontal, shiftPaint)

            canvas.drawLine(20f, horizontalLineStart, 20f, horizontalLineStart + 30, linePaint)
            canvas.drawLine(
                20 + columnWidth * 2,
                horizontalLineStart,
                20 + columnWidth * 2,
                horizontalLineStart + 30,
                linePaint
            )
            canvas.drawLine(
                columnStart,
                horizontalLineStart,
                columnStart,
                horizontalLineStart + 30,
                linePaint
            )
            canvas.drawText(
                "NIGHT TOTAL",
                (textStart - 10) + columnWidth * 3,
                textHorizontal,
                shiftPaint
            )
            horizontalLineStart += 30f
            textHorizontal += 30f

            canvas.drawLine(20f, horizontalLineStart, columnStart, horizontalLineStart, linePaint)

            newHorizontaLineStart = horizontalLineStart
            textStart = textWidth
            for (i in 0..8) {
                horizontalLineStart += 25f
                canvas.drawText(totalHeadingList.get(i), textStart, textHorizontal, headingPaint)
                canvas.drawText(
                    totalHeadingList.get(i),
                    textStart + 2 * columnWidth,
                    textHorizontal,
                    headingPaint
                )
                canvas.drawText(totalHeadingListAns1.get(i), textStart+columnWidth, textHorizontal, headingPaint)
                canvas.drawText(totalHeadingListAns2.get(i), textStart+columnWidth*3, textHorizontal, headingPaint)
                textHorizontal = horizontalLineStart + 17
                canvas.drawLine(
                    20f,
                    horizontalLineStart,
                    columnStart,
                    horizontalLineStart,
                    linePaint
                )
            }


            columnStart = 20f
            for (i in 0..4) {
                canvas.drawLine(
                    columnStart,
                    newHorizontaLineStart,
                    columnStart,
                    newHorizontaLineStart + (9 * 25).toFloat(),
                    linePaint
                )
                columnStart = columnStart + columnWidth
            }



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

    
}