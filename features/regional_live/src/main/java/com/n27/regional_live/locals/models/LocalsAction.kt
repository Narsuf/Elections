package com.n27.regional_live.locals.models

import com.n27.core.data.models.Election
import com.n27.core.data.remote.api.models.LocalElectionIds

internal sealed class LocalsAction {

    data class NavigateToDetail(val ids: LocalElectionIds): LocalsAction()
    data class ShowErrorSnackbar(val error: String? = null) : LocalsAction()
}
