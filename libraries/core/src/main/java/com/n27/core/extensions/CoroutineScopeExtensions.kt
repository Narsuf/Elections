package com.n27.core.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

inline fun CoroutineScope.launchCatching(
    crossinline errorBlock: suspend (Throwable) -> Unit,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    launch {
        runCatching { block() }.getOrElse { errorBlock(it) }
    }
}