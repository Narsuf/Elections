package com.n27.core.data.json

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.buffer
import okio.source
import java.nio.charset.StandardCharsets
import javax.inject.Singleton

@Singleton
class JsonReader {

    suspend fun getStringJson(res: String) = runCatching {
        withContext(Dispatchers.IO) {
            val inputStream = javaClass.classLoader!!.getResourceAsStream(res)
            val buffer = inputStream.source().buffer()
            buffer.readString(StandardCharsets.UTF_8).apply { inputStream.close() }
        }
    }.getOrElse { "" }

}

