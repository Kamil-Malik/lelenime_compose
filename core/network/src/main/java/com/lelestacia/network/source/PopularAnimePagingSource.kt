package com.lelestacia.network.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lelestacia.network.endpoint.AnimeAPI
import com.lelestacia.network.model.anime.AnimeResponse
import kotlinx.coroutines.delay
import timber.log.Timber

class PopularAnimePagingSource(
    private val animeAPI: AnimeAPI
) : PagingSource<Int, AnimeResponse>() {

    override fun getRefreshKey(state: PagingState<Int, AnimeResponse>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AnimeResponse> {
        return try {
            val currentPage = params.key ?: 1
            val apiResponse = animeAPI.getPopularAnime(page = currentPage)
            Timber.d("Successfully fetch ${apiResponse.data.size} data")
            delay(
                if (currentPage == 1) {
                    0
                } else {
                    500
                }
            )
            val previousPage =
                if (currentPage == 1) {
                    null
                } else {
                    currentPage - 1
                }
            val nextPage =
                if (apiResponse.pagination.hasNextPage) {
                    currentPage + 1
                } else {
                    null
                }
            LoadResult.Page(
                data = apiResponse.data,
                prevKey = previousPage,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            Timber.e(e.message)
            LoadResult.Error(e)
        }
    }
}
