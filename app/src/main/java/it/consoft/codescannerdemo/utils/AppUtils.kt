package it.consoft.codescannerdemo.utils

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.core.net.toUri

object AppUtils {

    fun copyToClipboard(context: Context, value: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("plaintext", value)
        clipboard.setPrimaryClip(clip)
    }

    fun searchWithGoogle(context: Context, query: String) {
        search(context,"https://www.google.com/search?q=${Uri.encode(query)}")
    }


    fun searchWithDuckDuckGo(context: Context, query: String) {
        search(context,"https://duckduckgo.com/?q=${Uri.encode(query)}")
    }


    fun searchWithBing(context: Context, query: String) {
        search(context,"https://www.bing.com/search?q=${Uri.encode(query)}")
    }

    fun search(context: Context, url: String) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            url.toUri()
        )

        startActivityWithChooser(context, intent)
    }

    fun startActivityWithChooser(context: Context, intent: Intent) {
        try {
            context.startActivity(Intent.createChooser(intent, "Choose an app"))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                context,
                "No app found to handle the request",
                Toast.LENGTH_LONG
            ).show()
        }
    }

}