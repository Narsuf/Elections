package com.n27.elections.domain

import com.n27.core.domain.election.models.Elections
import com.n27.core.extensions.sortByDateAndFormat
import com.n27.core.extensions.sortResultsByElectsAndVotes
import com.n27.elections.domain.mappers.toGeneralElections
import com.n27.elections.domain.models.GeneralElections
import com.n27.elections.domain.repositories.ElectionRepository
import kotlinx.coroutines.flow.flow
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

class ElectionUseCase(private val repository: ElectionRepository) {

    suspend fun getElections() = flow<Result<GeneralElections>> {
        repository.getElectionsRemotely()
            .onSuccess {
                repository.saveElections(it.items)
                emit(success(it.sort()))
            }
            .onFailure { error ->
                repository.getElectionsLocally()
                    ?.let { emit(success(it.sort())) }
                    ?: emit(failure(error))
            }
    }

    private fun Elections.sort(): GeneralElections = Elections(
        items
            .map { it.sortResultsByElectsAndVotes() }
            .sortByDateAndFormat()
    ).toGeneralElections()
}