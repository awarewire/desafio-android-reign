package com.example.desafio.presentation.hits

sealed class MainStateUi {
    object Initial : MainStateUi()
    data class DisplayHits(val hist: List<HitStateUi>) : MainStateUi()
    object ErrorNetwork : MainStateUi()
    object Loading : MainStateUi()
}