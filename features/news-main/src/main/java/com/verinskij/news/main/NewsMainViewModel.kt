package com.verinskij.news.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.verinskij.news.data.ArticlesRepository
import com.verinskij.news.data.RequestResult
import com.verinskij.news.data.map
import com.verinskij.news.main.models.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

internal class NewsMainViewModel(
    private val useCase: GetAllArticlesUseCase
) : ViewModel() {

    //private val _state: MutableStateFlow<State>

    val state: StateFlow<State> = useCase()
    .map { it.toState() }
    .stateIn(viewModelScope, SharingStarted.Lazily, State.None)
}

private fun RequestResult<List<Article>>.toState(): State {
    return when (this) {
        is RequestResult.Error -> State.Error()
        is RequestResult.InProgress -> State.Loading(data)
        is RequestResult.Success -> State.Success(checkNotNull(data))
    }
}

sealed class State {

    object None : State()
    class Loading(val articles: List<Article>?) : State()
    class Error : State()
    class Success(val articles: List<Article>) : State()
}