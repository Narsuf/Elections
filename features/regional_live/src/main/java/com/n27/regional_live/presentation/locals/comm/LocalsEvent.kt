package com.n27.regional_live.presentation.locals.comm

import com.n27.core.domain.live.models.LocalElectionIds

sealed class LocalsEvent {

    data class ShowError(val throwable: Throwable) : LocalsEvent()

    data class RequestElection(val ids: LocalElectionIds) : LocalsEvent()
}
