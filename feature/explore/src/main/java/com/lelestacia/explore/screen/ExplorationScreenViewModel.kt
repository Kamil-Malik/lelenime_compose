package com.lelestacia.explore.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lelestacia.common.displayStyle.DisplayStyle
import com.lelestacia.domain.usecases.explore.IExploreUseCases
import com.lelestacia.domain.usecases.settings.IUserPreferencesUseCases
import com.lelestacia.explore.component.header.HeaderScreenState
import com.lelestacia.explore.stateAndEvent.ExploreScreenEvent
import com.lelestacia.explore.stateAndEvent.ExploreScreenState
import com.lelestacia.model.Anime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class ExplorationScreenViewModel @Inject constructor(
    private val useCases: IExploreUseCases,
    private val useCasesPreferences: IUserPreferencesUseCases
) : ViewModel() {

    private val currentSearchQuery: MutableStateFlow<String> = MutableStateFlow("")

    private val headerState: MutableStateFlow<HeaderScreenState> =
        MutableStateFlow(HeaderScreenState())

    private val displayedStyle: MutableStateFlow<DisplayStyle> =
        MutableStateFlow(DisplayStyle.CARD)

    private val displayedAnimeType: MutableStateFlow<DisplayType> =
        MutableStateFlow(DisplayType.POPULAR)

    private val searchedAnime: Flow<PagingData<Anime>> = currentSearchQuery
        .debounce(0)
        .distinctUntilChanged()
        .flatMapLatest { useCases.getAnimeSearch(searchQuery = it) }

    private val anime: Flow<PagingData<Anime>> = displayedAnimeType.flatMapLatest { type ->
        when (type) {
            DisplayType.POPULAR -> useCases.getPopularAnime().cachedIn(viewModelScope)
            DisplayType.AIRING -> useCases.getAiringAnime().cachedIn(viewModelScope)
            DisplayType.UPCOMING -> useCases.getUpcomingAnime().cachedIn(viewModelScope)
            DisplayType.SEARCH -> searchedAnime.cachedIn(viewModelScope)
        }
    }

    val explorationScreenState: StateFlow<ExploreScreenState> =
        combine(
            headerState,
            displayedStyle,
            displayedAnimeType
        ) { headerState: HeaderScreenState, displayedStyle: DisplayStyle, displayedType: DisplayType ->
            ExploreScreenState(
                headerScreenState = headerState,
                displayStyle = displayedStyle,
                displayType = displayedType,
                anime = anime
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ExploreScreenState()
        )

    fun onEvent(event: ExploreScreenEvent) {
        Timber.d("Explore Screen Event: $event")
        when (event) {
            is ExploreScreenEvent.OnDisplayTypeChanged -> {
                displayedAnimeType.update {
                    event.selectedType
                }

                if (event.selectedType == DisplayType.SEARCH) return
                headerState.update {
                    it.copy(
                        searchedAnimeTitle = "",
                        isSearching = false
                    )
                }
                currentSearchQuery.update { "" }
            }

            is ExploreScreenEvent.OnDisplayStyleChanged -> displayedStyle.update {
                event.selectedStyle
            }

            ExploreScreenEvent.OnFilterOptionMenuChangedState -> headerState.update {
                it.copy(
                    isFilterOptionOpened = !it.isFilterOptionOpened
                )
            }

            ExploreScreenEvent.OnDisplayStyleOptionMenuStateChanged -> headerState.update {
                it.copy(
                    isDisplayStyleOptionOpened = !it.isDisplayStyleOptionOpened
                )
            }

            is ExploreScreenEvent.OnSearchQueryChanged -> headerState.update {
                it.copy(
                    searchQuery = event.newSearchQuery
                )
            }

            ExploreScreenEvent.OnStartSearching -> headerState.update {
                it.copy(
                    isSearching = true
                )
            }

            ExploreScreenEvent.OnSearch -> {
                displayedAnimeType.update {
                    DisplayType.SEARCH
                }
                headerState.update {
                    it.copy(
                        searchedAnimeTitle = it.searchQuery
                    )
                }
                currentSearchQuery.update {
                    headerState.value.searchQuery
                }
            }

            ExploreScreenEvent.OnStopSearching -> {
                headerState.update {
                    it.copy(
                        searchQuery = "",
                        isSearching = false
                    )
                }
            }
        }
    }

    fun insertOrUpdateAnimeHistory(anime: Anime) = viewModelScope.launch {
        useCases.insertOrUpdateAnimeHistory(anime = anime)
    }

    init {
        viewModelScope.launch {
            useCasesPreferences.getUserDisplayStyle().collectLatest { stylePreferences ->
                val displayStyle = when (stylePreferences) {
                    1 -> DisplayStyle.CARD
                    2 -> DisplayStyle.COMPACT_CARD
                    else -> DisplayStyle.LIST
                }
                Timber.d("Updated Style Preferences : $displayStyle")
                displayedStyle.update { displayStyle }
            }
        }
    }
}
