package com.n27.core.extensions

import java.text.Normalizer

fun String.removeAccents(): String = Normalizer
    .normalize(this, Normalizer.Form.NFD)
    .replace("\\p{M}".toRegex(), "")