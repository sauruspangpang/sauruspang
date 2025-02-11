package com.ksj.sauruspang.Learnpackage

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ScoreViewModel : ViewModel() {
    private val _correctAnswers = mutableIntStateOf(0)
    val correctAnswers: State<Int> get() = _correctAnswers // Expose as State

    fun incrementScore() {
        _correctAnswers.intValue += 1
    }

    fun resetScore() {
        _correctAnswers.intValue = 0
    }
}