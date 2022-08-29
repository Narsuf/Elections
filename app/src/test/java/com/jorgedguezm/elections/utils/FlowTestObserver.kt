package com.jorgedguezm.elections.utils

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.Closeable

class FlowTestObserver<T>(scope: CoroutineScope, flow: Flow<T>) : Closeable {

    private val _values = mutableListOf<T>()

    private val job: Job = scope.launch {
        flow.collect { _values.add(it) }
    }

    fun assertValues(vararg values: T): FlowTestObserver<T> {
        assertEquals(values.toList(), _values)
        return this
    }

    override fun close() = job.cancel()
}
