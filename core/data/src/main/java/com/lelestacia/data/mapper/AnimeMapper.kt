package com.lelestacia.data.mapper

import com.lelestacia.database.entity.anime.AnimeEntity
import com.lelestacia.model.Anime
import com.lelestacia.network.model.anime.AnimeResponse
import java.util.Date

fun AnimeResponse.asAnime(): Anime =
    Anime(
        malID = malId,
        coverImages = coverImages.webp.largeImageUrl,
        trailer = Anime.Trailer(
            youtubeId = trailer?.youtubeId,
            url = trailer?.url,
            images = trailer?.images?.maximumImageUrl
        ),
        title = title,
        titleEnglish = titleEnglish,
        titleJapanese = titleJapanese,
        type = type ?: "Unknown",
        source = source,
        episodes = episodes,
        status = status,
        airing = airing,
        startedDate = aired.from,
        finishedDate = aired.to,
        duration = duration,
        rating = rating ?: "Unknown",
        score = score,
        scoredBy = scoredBy,
        rank = rank,
        synopsis = synopsis,
        season = season,
        year = year,
        studios = studio.map { studio ->
            studio.name
        },
        genres = genres.map { genre ->
            genre.name
        },
        isFavorite = false
    )

fun Anime.asNewEntity(): AnimeEntity =
    AnimeEntity(
        animeID = malID,
        image = coverImages,
        trailer = AnimeEntity.Trailer(
            id = trailer?.youtubeId,
            url = trailer?.url,
            image = trailer?.images,
        ),
        title = title,
        titleEnglish = titleEnglish,
        titleJapanese = titleJapanese,
        type = type,
        episodes = episodes,
        status = status,
        rating = rating,
        score = score,
        scoredBy = scoredBy,
        rank = rank,
        synopsis = synopsis,
        season = season,
        year = year,
        genres = genres,
        lastViewed = Date(),
        isFavorite = isFavorite,
        startedDate = startedDate,
        finishedDate = finishedDate,
        createdAt = Date(),
        updatedAt = null,
        source = source,
        airing = airing,
        duration = duration,
        studios = studios
    )

fun AnimeEntity.asAnime(): Anime = Anime(
    malID = animeID,
    coverImages = image,
    trailer = Anime.Trailer(
        youtubeId = trailer?.id,
        url = trailer?.url,
        images = trailer?.image
    ),
    title = title,
    titleEnglish = titleEnglish,
    titleJapanese = titleJapanese,
    type = type,
    episodes = episodes,
    status = status,
    rating = rating,
    score = score,
    scoredBy = scoredBy,
    rank = rank,
    synopsis = synopsis,
    season = season,
    year = year,
    genres = genres,
    isFavorite = isFavorite,
    startedDate = startedDate,
    finishedDate = finishedDate,
    source = source,
    airing = airing,
    duration = duration,
    studios = studios
)