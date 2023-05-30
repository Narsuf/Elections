package com.n27.regional_live.presentation.locals.comm

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalsEventBus @Inject constructor() {

    private val internalEvent = MutableSharedFlow<LocalsEvent>()
    val event = internalEvent.asSharedFlow()

    suspend fun emit(event: LocalsEvent) { internalEvent.emit(event) }
}
