package com.n27.regional_live.presentation.locals.models

import com.n27.core.domain.live.models.LocalElectionIds

internal sealed class LocalsAction {

    data class NavigateToDetail(val ids: LocalElectionIds): LocalsAction()
    data class ShowErrorSnackbar(val error: String?) : LocalsAction()
}
