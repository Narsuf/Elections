package com.n27.core.extensions

import android.content.Context
import android.content.res.Configuration

fun Context.isDarkModeEnabled(): Boolean =
    (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
            Configuration.UI_MODE_NIGHT_YES