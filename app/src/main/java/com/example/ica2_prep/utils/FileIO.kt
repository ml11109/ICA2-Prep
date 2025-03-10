package com.example.ica2_prep.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

// Text files

fun saveTextToFile(context: Context, filename: String, text: String) {
    val file = File(context.filesDir, filename)
    file.writeText(text)
}

fun readTextFromFile(context: Context, filename: String): String {
    val file = File(context.filesDir, filename)
    return if (file.exists()) file.readText() else "No saved text found"
}


// PDFs

/*
In <application> in manifest:
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.provider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/filepaths" />
</provider>

In filepaths.xml in res/xml:
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <files-path name="pdfs" path="pdfs/" />
</paths>
*/

fun getPdfUri(context: Context, fileName: String, resourceId: Int): Uri {
    val file = File(context.filesDir, "pdfs/$fileName")

    // Copy PDF from assets to internal storage
    if (!file.exists()) {
        file.parentFile?.mkdirs()
        context.resources.openRawResource(resourceId).use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
    }

    // Get URI of PDF
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
}

fun openPdf(context: Context, uri: Uri) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY
    }
    context.startActivity(intent)
}

fun openPdf(context: Context, fileName: String, resourceId: Int) {
    openPdf(context, getPdfUri(context, fileName, resourceId))
}
