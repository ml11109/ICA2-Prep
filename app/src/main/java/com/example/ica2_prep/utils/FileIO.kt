package com.example.ica2_prep.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import androidx.core.content.FileProvider
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.io.OutputStreamWriter

// Text files

fun saveTextToFile(context: Context, filename: String, text: String) {
    val file = File(context.filesDir, filename)
    file.writeText(text)
}

fun readTextFromFile(context: Context, filename: String): String {
    val file = File(context.filesDir, filename)
    return if (file.exists()) file.readText() else "No saved text found"
}

fun deleteFile(context: Context, filename: String) {
    val file = File(context.filesDir, filename)
    if (file.exists()) file.delete()
}

/*
// Launcher to save file with file picker

val saveFileLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.CreateDocument("text/plain"),
) { uri ->
    if (uri != null) {
        saveTextToUri(context, uri, viewModel.text)
        statusMessage = "File saved"
    } else {
        statusMessage = "File save cancelled"
    }
}

// To launch

saveFileLauncher.launch(fileName)
 */

fun saveTextToUri(context: Context, uri: Uri, text: String) {
    try {
        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            OutputStreamWriter(outputStream).use { writer ->
                writer.write(text)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/*
// Launcher to open file with file picker

val filePickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.OpenDocument(),
) { uri ->
    if (uri != null) {
        fileUri = uri
        fileName = getFileNameFromUri(context, uri)
        fileContent = getTextFromUri(context, uri)
        // Use fileName / fileContent as needed
    }
}

// To launch

filePickerLauncher.launch(arrayOf("text/plain"))
 */

fun getFileNameFromUri(context: Context, uri: Uri): String {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    val nameIndex = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME) ?: -1
    val name = if (cursor != null && cursor.moveToFirst() && nameIndex >= 0) {
        cursor.getString(nameIndex)
    } else {
        "Unknown File"
    }
    cursor?.close()
    return name
}

fun getTextFromUri(context: Context, uri: Uri): String {
    return try {
        context.contentResolver.openInputStream(uri).use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                reader.readText()
            }
        }
    } catch (e: Exception) {
        "Error reading file"
    }
}


// PDFs

/*
// In <application> in manifest
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.provider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/filepaths" />
</provider>

// In filepaths.xml in res/xml
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <files-path name="pdfs" path="pdfs/" />
</paths>

// To open PDF
openPdf(context, "example.pdf", R.raw.example)
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
