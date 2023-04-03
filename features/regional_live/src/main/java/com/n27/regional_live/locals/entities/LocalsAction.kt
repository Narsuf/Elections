package com.n27.regional_live.locals.entities

import com.n27.core.data.models.Election
import com.n27.core.data.remote.api.models.LocalElectionIds

internal sealed class LocalsAction {

    data class NavigateToDetail(val election: Election, val ids: LocalElectionIds): LocalsAction()
}