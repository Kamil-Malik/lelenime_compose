package com.lelestacia.network.source

import androidx.paging.PagingSource
import com.lelestacia.network.model.anime.AnimeResponse

interface IAnimeNetworkService {
    fun getAiringAnime(): PagingSource<Int, AnimeResponse>
    fun getUpcomingAnime(): PagingSource<Int, AnimeResponse>
    fun getPopularAnime(): PagingSource<Int, AnimeResponse>
    fun getAnimeSearch(
        searchQuery: String,
        type: String? = null,
        status: String? = null,
        rating: String? = null
    ): PagingSource<Int, AnimeResponse>
}
