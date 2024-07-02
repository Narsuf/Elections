package com.n27.elections.presentation

import DarkMode
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.Constants.NO_RESULTS
import com.n27.core.components.Dimens
import com.n27.core.components.image.Lottie
import com.n27.core.domain.election.models.Election
import com.n27.core.extensions.getPieChartData
import com.n27.elections.R
import com.n27.elections.presentation.entities.MainUiState
import com.n27.elections.presentation.entities.MainUiState.HasElections
import com.n27.elections.presentation.entities.MainUiState.NoElections

typealias OnElectionClicked = (congressElection: Election, senateElection: Election) -> Unit

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun MainScreen(
    state: MainUiState,
    darkMode: DarkMode,
    onPullToRefresh: () -> Unit,
    onElectionClicked: OnElectionClicked
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val pullRefreshState = rememberPullRefreshState(shouldShowRefreshing(state) && state.isLoading, { onPullToRefresh() })

    var boxModifier = Modifier
        .fillMaxSize()
        .pullRefresh(pullRefreshState)

    if (state is NoElections) boxModifier = boxModifier.verticalScroll(rememberScrollState())

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Box(boxModifier.padding(padding)) {
            if (state.error != null) ErrorSnackbar(state.error, snackbarHostState)

            when (state) {
                is HasElections -> {
                    ElectionList(state, darkMode, onElectionClicked)

                    /*liveElectionsButtonActivityMain.isVisible =
                        isFeatureEnabled(Constants.REGIONAL_LIVE) || isFeatureEnabled(Constants.CONGRESS_LIVE)*/
                }

                is NoElections -> {
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
}

private fun shouldShowRefreshing(state: MainUiState) = (state is NoElections && state.error != null) || state is HasElections

@Composable
private fun ErrorSnackbar(errorMsg: String?, snackbarHost: SnackbarHostState) {
    val scope = rememberCoroutineScope()

    val error = stringResource(
        when (errorMsg) {
            NO_RESULTS -> R.string.preliminary_results_not_available_yet
            NO_INTERNET_CONNECTION -> R.string.no_internet_connection
            else -> R.string.something_wrong
        }
    )

    LaunchedEffect(scope) {
        snackbarHost.showSnackbar(message = error, duration = SnackbarDuration.Short)
    }
}

@Composable
private fun ElectionList(state: HasElections, darkMode: DarkMode, onElectionClicked: OnElectionClicked) {
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
                    darkMode,
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