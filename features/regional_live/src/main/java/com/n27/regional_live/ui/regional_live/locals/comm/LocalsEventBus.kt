package com.n27.regional_live.ui.regional_live.locals.comm

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalsEventBus @Inject constructor() {

    private val internalEvent = MutableSharedFlow<LocalsEvent>(
        replay = 0,
        extraBufferCapacity = 10,
        onBufferOverflow = BufferOverflow.SUSPEND
    )

    val event = internalEvent.asSharedFlow()

    suspend fun emit(event: LocalsEvent) { internalEvent.emit(event) }
}
