package com.n27.elections.presentation

import PieChart
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.n27.core.components.Dimens
import com.n27.core.components.image.Lottie
import com.n27.core.domain.election.models.Election
import com.n27.core.extensions.getPieChartData
import com.n27.elections.R
import com.n27.elections.presentation.entities.MainUiState
import com.n27.elections.presentation.entities.MainUiState.HasElections

typealias OnElectionClicked = (congressElection: Election, senateElection: Election) -> Unit


@Composable
@OptIn(ExperimentalMaterialApi::class)
fun MainScreen(
    state: MainUiState,
    onPullToRefresh: () -> Unit,
    onElectionClicked: OnElectionClicked
) {
    val pullRefreshState = rememberPullRefreshState(state.isLoading, { onPullToRefresh() })

    var boxModifier = Modifier
        .fillMaxSize()
        .pullRefresh(pullRefreshState)

    if (state is MainUiState.NoElections) boxModifier = boxModifier.verticalScroll(rememberScrollState())

    Box(boxModifier) {

            when (state) {
                is HasElections -> {
                    //if (state.error != null) showSnackbar(state.error)

                    ElectionList(state, onElectionClicked)

                    /*liveElectionsButtonActivityMain.isVisible =
                        isFeatureEnabled(Constants.REGIONAL_LIVE) || isFeatureEnabled(Constants.CONGRESS_LIVE)*/
                }

                is MainUiState.NoElections -> {
                    when {
                        state.isLoading -> Lottie(R.raw.loading_votes, Modifier.align(Alignment.Center))
                        state.error != null -> Lottie(
                            R.raw.empty_box,
                            Modifier.align(Alignment.Center),
                            isError = true
                        )
                    }

            }
        }

        PullRefreshIndicator(
            refreshing = state.isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
private fun ElectionList(state: HasElections, onElectionClicked: OnElectionClicked) {
    LazyColumn {
        itemsIndexed(state.congressElections) { index, election ->
            ElevatedCard(
                onClick = { onElectionClicked(election, state.senateElections[index]) },
                Modifier.padding(
                    horizontal = Dimens.defaultSpacing,
                    vertical = Dimens.tightSpacing
                ),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
            ) {
                PieChart(
                    data = election.results.getPieChartData(),
                    Modifier.padding(
                        start = Dimens.maxSpacing,
                        top = Dimens.loosestSpacing,
                        end = Dimens.maxSpacing
                    )
                )
                Text(
                    election.date,
                    Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                        .padding(bottom = Dimens.loosestSpacing),
                    Color.Gray,
                    fontSize = Dimens.cardTextSizeYearGeneralElections,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}