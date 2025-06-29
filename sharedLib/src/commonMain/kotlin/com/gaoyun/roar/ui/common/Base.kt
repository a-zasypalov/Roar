package com.gaoyun.roar.ui.common

import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    abstract val viewState: MutableStateFlow<*>
}