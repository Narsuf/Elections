package com.n27.regional_live.locals.entities

import com.n27.core.data.models.Election
import com.n27.core.data.remote.api.models.LocalElectionIds
import com.n27.regional_live.locals.dialog.entities.MunicipalityAction

internal sealed class LocalsAction {

    data class NavigateToDetail(val election: Election, val ids: LocalElectionIds): LocalsAction()
    data class ShowErrorSnackbar(val error: String? = null) : LocalsAction()
}