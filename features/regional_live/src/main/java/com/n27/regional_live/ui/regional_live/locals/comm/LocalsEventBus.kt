package com.n27.regional_live.ui.regional_live.locals.comm

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalsEventBus @Inject constructor() {

    private val internalEvent = Channel<LocalsEvent>(capacity = 1, BufferOverflow.DROP_OLDEST)
    val event = internalEvent.receiveAsFlow()

    fun trySend(event: LocalsEvent) { internalEvent.trySend(event) }
}
