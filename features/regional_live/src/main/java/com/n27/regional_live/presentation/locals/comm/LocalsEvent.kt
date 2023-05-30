package com.n27.regional_live.presentation.locals.comm

import com.n27.core.data.remote.api.models.LocalElectionIds


sealed class LocalsEvent {

    object ShowError : LocalsEvent()

    data class RequestElection(val ids: LocalElectionIds) : LocalsEvent()
}
