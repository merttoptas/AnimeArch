package com.halilozcan.animearch.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halilozcan.animearch.data.NetworkResponseState
import com.halilozcan.animearch.domain.entity.TopAnimeEntity
import com.halilozcan.animearch.domain.mapper.AnimeListMapper
import com.halilozcan.animearch.domain.usecase.GetTopCharacterUseCase
import com.halilozcan.animearch.ui.AnimeHomeUiData
import com.halilozcan.animearch.ui.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
data class HomeViewModel @Inject constructor(
    private val getTopCharacterUseCase: GetTopCharacterUseCase,
    private val mapper: AnimeListMapper<TopAnimeEntity, AnimeHomeUiData>
) :
    ViewModel() {

    private val _screenState =
        MutableStateFlow<ScreenState<List<AnimeHomeUiData>>>(value = ScreenState.Loading)
    val screenState: Flow<ScreenState<List<AnimeHomeUiData>>> get() = _screenState

    init {
        getTopPokeCards()
    }

    private fun getTopPokeCards() {
        viewModelScope.launch {
            getTopCharacterUseCase().collectLatest {
                when (it) {
                    is NetworkResponseState.Error -> {
                        _screenState.emit(ScreenState.Error(it.exception.message.orEmpty()))
                    }
                    NetworkResponseState.Loading -> {
                        _screenState.emit(ScreenState.Loading)
                    }
                    is NetworkResponseState.Success -> {
                        _screenState.emit(ScreenState.Success(mapper.map(it.result)))
                    }
                }
            }
        }
    }
}