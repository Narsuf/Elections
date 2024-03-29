package com.n27.core.data.local.json

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.buffer
import okio.source
import java.nio.charset.StandardCharsets.UTF_8
import javax.inject.Singleton

@Singleton
class JsonReader {

    suspend fun getStringJson(res: String) = withContext(Dispatchers.IO) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(res)
        val buffer = inputStream.source().buffer()
        buffer.readString(UTF_8).apply { inputStream.close() }
    }
}

