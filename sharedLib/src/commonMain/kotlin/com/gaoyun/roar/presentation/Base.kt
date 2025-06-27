package com.gaoyun.roar.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    abstract val viewState: MutableStateFlow<*>
}