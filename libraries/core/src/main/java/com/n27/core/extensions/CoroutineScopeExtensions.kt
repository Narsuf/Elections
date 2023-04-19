package com.n27.core.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * My problem with the existing launch(CoroutineExceptionHandler) is that it doesn't allow you to handle the exceptions
 * in a suspend function. So you would have to use tryEmit or trySend instead of emit and send. It's probably not that
 * important, but why not take advantage of the same coroutine to handle both operations and their possible exceptions.
 */
inline fun CoroutineScope.launchCatching(
    crossinline errorBlock: suspend (Throwable) -> Unit,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    launch {
        runCatching { block() }.getOrElse { errorBlock(it) }
    }
}
