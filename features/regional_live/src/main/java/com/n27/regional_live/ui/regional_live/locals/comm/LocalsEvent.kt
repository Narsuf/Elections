package com.n27.regional_live.ui.regional_live.locals.comm

import com.n27.core.data.api.models.LocalElectionIds
import com.n27.core.data.json.models.Province
import com.n27.core.data.models.Election


sealed class LocalsEvent {

    data class RequestElection(val ids: LocalElectionIds) : LocalsEvent()
    data class ShowError(val error: String? = null) : LocalsEvent()
}
