package com.n27.test.observers

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.Closeable

class FlowTestObserver<T>(scope: CoroutineScope, flow: Flow<T>) : Closeable {

    val values = mutableListOf<T>()

    private val job: Job = scope.launch {
        flow.collect { values.add(it) }
    }

    fun assertValue(value: T): FlowTestObserver<T> {
        assertTrue(values.isNotEmpty())
        assertEquals(value, values.last())
        return this
    }

    fun assertValues(vararg values: T): FlowTestObserver<T> {
        assertEquals(values.toList(), this.values)
        return this
    }

    override fun close() = job.cancel()
}
