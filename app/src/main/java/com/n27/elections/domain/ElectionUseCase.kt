package com.n27.elections.domain

import com.n27.core.domain.election.Elections
import com.n27.core.extensions.sortByDateAndFormat
import com.n27.core.extensions.sortResultsByElectsAndVotes
import com.n27.elections.domain.mappers.toGeneralElections
import com.n27.elections.domain.models.GeneralElections
import kotlinx.coroutines.flow.flow
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

class ElectionUseCase(private val repository: ElectionRepository) {

    suspend fun getElections() = flow<Result<GeneralElections>> {
        repository.getElectionsRemotely()
            .onSuccess {
                repository.saveElections(it.items)
                emit(success(it.sort().toGeneralElections()))
            }
            .onFailure { error ->
                repository.getElectionsLocally()
                    ?.let { emit(success(it.sort().toGeneralElections())) }
                    ?: emit(failure(error))
            }
    }

    private fun Elections.sort() = Elections(
        items
            .map { it.sortResultsByElectsAndVotes() }
            .sortByDateAndFormat()
    )
}