package com.n27.core.presentation.common

import com.n27.core.data.models.Election

typealias OnGeneralElectionClicked = (congressElection: Election, senateElection: Election) -> Unit
typealias OnLiveElectionClicked = (election: Election, id: String?) -> Unit