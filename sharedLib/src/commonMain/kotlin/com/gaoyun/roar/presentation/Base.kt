package com.gaoyun.roar.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.ViewModel


const val LAUNCH_LISTEN_FOR_EFFECTS = "launch-listen-to-effects"

abstract class MultiplatformBaseViewModel : ViewModel() {
    abstract val viewState: MutableStateFlow<*>
}