package com.n27.core.extensions

import org.json.JSONObject

fun JSONObject.getKey() = names()?.getString(0)