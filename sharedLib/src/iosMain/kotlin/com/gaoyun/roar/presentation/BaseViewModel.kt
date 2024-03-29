package com.gaoyun.roar.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

actual abstract class BaseViewModel<Event : ViewEvent, UiState : ViewState, Effect : ViewSideEffect> {

    internal actual val initialState: UiState by lazy { setInitialState() }

    actual abstract fun setInitialState(): UiState

    internal actual val _viewState: MutableStateFlow<UiState> = MutableStateFlow(initialState)
    actual val viewState: StateFlow<UiState> = _viewState

    internal actual val _event: MutableSharedFlow<Event> = MutableSharedFlow()

    internal actual val _effect: Channel<Effect> = Channel()
    actual val effect = _effect.receiveAsFlow()

    protected fun observeEffect(onChange: (Effect) -> Unit) {
        effect.onEach { onChange(it) }.launchIn(scope)
    }

    protected fun observeViewState(onChange: (UiState) -> Unit) {
        viewState.onEach { onChange(it) }.launchIn(scope)
    }

    actual val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    init {
        subscribeToEvents()
    }

    actual fun setEvent(event: Event) {
        scope.launch { _event.emit(event) }
    }

    protected actual fun setState(reducer: UiState.() -> UiState) {
        val newState = viewState.value.reducer()
        _viewState.value = newState
    }

    internal actual fun subscribeToEvents() {
        scope.launch {
            _event.collect {
                handleEvents(it)
            }
        }
    }

    actual abstract fun handleEvents(event: Event)
    protected actual fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        scope.launch { _effect.send(effectValue) }
    }

    actual fun dispose() {
        scope.cancel()
    }

}