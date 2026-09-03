package com.example.ui.pc

import fi.iki.elonen.NanoHTTPD
import java.io.File
import android.os.Environment

class PCShareServer(port: Int) : NanoHTTPD(port) {

    override fun serve(session: IHTTPSession): Response {
        val uri = session.uri
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        if (uri == "/") {
            return serveIndex(downloadsDir)
        } else if (uri.startsWith("/download/")) {
            val fileName = uri.substring("/download/".length)
            val file = File(downloadsDir, fileName)
            if (file.exists() && file.isFile) {
                return try {
                    val mimeType = getMimeTypeForFile(uri)
                    newFixedLengthResponse(Response.Status.OK, mimeType, file.inputStream(), file.length())
                } catch (e: Exception) {
                    newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "Error reading file")
                }
            }
        }

        return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT, "File not found")
    }

    private fun serveIndex(dir: File): Response {
        val files = dir.listFiles()?.filter { it.isFile } ?: emptyList()
        val html = buildString {
            append("<html><head><title>ShareLoad - PC Share</title>")
            append("<style>body { font-family: sans-serif; padding: 20px; } a { text-decoration: none; color: #0066cc; } li { margin: 10px 0; }</style>")
            append("</head><body>")
            append("<h1>ShareLoad - Shared Files</h1>")
            append("<p>Files from Downloads folder:</p>")
            append("<ul>")
            for (file in files) {
                append("<li><a href='/download/${file.name}'>${file.name}</a> (${file.length() / 1024} KB)</li>")
            }
            if (files.isEmpty()) {
                append("<li>No files available.</li>")
            }
            append("</ul></body></html>")
        }
        return newFixedLengthResponse(Response.Status.OK, MIME_HTML, html)
    }
}
