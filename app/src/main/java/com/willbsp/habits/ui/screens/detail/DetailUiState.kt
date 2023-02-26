package com.willbsp.habits.ui.screens.detail

data class DetailUiState(
    val habitName: String,
    val streak: Int = 0,
    val score: Int = 0,
)
