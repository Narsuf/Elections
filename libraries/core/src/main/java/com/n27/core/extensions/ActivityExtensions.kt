package com.n27.core.extensions

import android.app.Activity
import android.content.Intent
import android.net.Uri

fun Activity.openLink(link: String) {
    val enlaceUri = Uri.parse(link)
    val intent = Intent(Intent.ACTION_VIEW, enlaceUri)
    startActivity(intent)
}