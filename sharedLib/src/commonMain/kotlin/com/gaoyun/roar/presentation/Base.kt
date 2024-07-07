package com.gaoyun.roar.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

interface ViewState

interface ViewEvent

interface ViewSideEffect
interface NavigationSideEffect: ViewSideEffect
object BackNavigationEffect: NavigationSideEffect

const val LAUNCH_LISTEN_FOR_EFFECTS = "launch-listen-to-effects"

abstract class MultiplatformBaseViewModel<Event : ViewEvent, UiState : ViewState, Effect : ViewSideEffect> : ViewModel() {

    internal val initialState: UiState by lazy { setInitialState() }

    abstract fun setInitialState(): UiState

    internal val _viewState: MutableStateFlow<UiState> = MutableStateFlow(initialState)
    val viewState: StateFlow<UiState> = _viewState

    internal val _event: MutableSharedFlow<Event> = MutableSharedFlow()

    internal val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    val scope = viewModelScope

    init {
        subscribeToEvents()
    }

    fun setEvent(event: Event) {
        scope.launch { _event.emit(event) }
    }

    protected fun setState(reducer: UiState.() -> UiState) {
        val newState = viewState.value.reducer()
        _viewState.value = newState
    }

    internal fun subscribeToEvents() {
        scope.launch {
            _event.collect {
                handleEvents(it)
            }
        }
    }

    abstract fun handleEvents(event: Event)
    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        scope.launch { _effect.send(effectValue) }
    }

    override fun onCleared() {
        super.onCleared()
        dispose()
    }

    fun dispose() {
        scope.cancel()
    }

}

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

    fun dispose()

}