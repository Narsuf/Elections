package com.n27.core.extensions

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun SharedPreferences.containsIO(key: String) = withContext(Dispatchers.IO) { contains(key) }
