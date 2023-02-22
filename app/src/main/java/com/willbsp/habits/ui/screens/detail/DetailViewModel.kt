package com.willbsp.habits.ui.screens.detail

import androidx.lifecycle.ViewModel
import com.willbsp.habits.data.repo.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val habitsRepository: HabitRepository
) : ViewModel()