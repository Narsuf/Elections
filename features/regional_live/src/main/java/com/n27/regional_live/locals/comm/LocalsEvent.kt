package com.n27.regional_live.locals.comm

import com.n27.core.data.remote.api.models.LocalElectionIds


sealed class LocalsEvent {

    data class RequestElection(val ids: LocalElectionIds) : LocalsEvent()
    object ShowError : LocalsEvent()
}
