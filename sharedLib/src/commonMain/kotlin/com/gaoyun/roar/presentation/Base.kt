package com.gaoyun.roar.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface ViewState

interface ViewEvent

interface ViewSideEffect

const val LAUNCH_LISTEN_FOR_EFFECTS = "launch-listen-to-effects"

expect abstract class BaseViewModel<Event : ViewEvent, UiState : ViewState, Effect : ViewSideEffect>() {

    internal val initialState: UiState
    abstract fun setInitialState(): UiState

    internal val _viewState: MutableStateFlow<UiState>
    val viewState: StateFlow<UiState>

    internal val _event: MutableSharedFlow<Event>

    internal val _effect: Channel<Effect>
    val effect: Flow<Effect>

    val scope: CoroutineScope

    fun setEvent(event: Event)

    protected fun setState(reducer: UiState.() -> UiState)

    internal fun subscribeToEvents()

    abstract fun handleEvents(event: Event)

    protected fun setEffect(builder: () -> Effect)

}