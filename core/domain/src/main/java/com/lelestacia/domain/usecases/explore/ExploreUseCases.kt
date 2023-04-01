package com.lelestacia.domain.usecases.explore

import androidx.paging.PagingData
import com.lelestacia.data.repository.IAnimeRepository
import com.lelestacia.model.Anime
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExploreUseCases @Inject constructor(
    private val repository: IAnimeRepository
) : IExploreUseCases {

    override suspend fun insertOrUpdateAnimeHistory(anime: Anime) {
        repository.insertOrUpdateAnimeHistory(anime = anime)
    }

    override fun getAiringAnime(): Flow<PagingData<Anime>> {
        return repository.getAiringAnime()
    }

    override fun getUpcomingAnime(): Flow<PagingData<Anime>> {
        return repository.getUpcomingAnime()
    }

    override fun getPopularAnime(): Flow<PagingData<Anime>> {
        return repository.getPopularAnime()
    }

    override fun getAnimeSearch(
        searchQuery: String,
        type: String?,
        status: String?,
        rating: String?
    ): Flow<PagingData<Anime>> {
        return repository.getAnimeSearch(
            searchQuery = searchQuery,
            type = type,
            status = status,
            rating = rating
        )
    }
}