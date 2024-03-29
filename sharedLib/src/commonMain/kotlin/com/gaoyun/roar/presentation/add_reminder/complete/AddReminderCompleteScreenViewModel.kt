package com.gaoyun.roar.presentation.add_reminder.complete

import com.gaoyun.roar.presentation.BaseViewModel

class AddReminderCompleteScreenViewModel :
    BaseViewModel<AddReminderCompleteScreenContract.Event, AddReminderCompleteScreenContract.State, AddReminderCompleteScreenContract.Effect>() {

    override fun setInitialState() = AddReminderCompleteScreenContract.State(isLoading = false)

    override fun handleEvents(event: AddReminderCompleteScreenContract.Event) {
        when (event) {
            is AddReminderCompleteScreenContract.Event.ContinueButtonClicked -> setEffect {
                AddReminderCompleteScreenContract.Effect.Navigation.Continue
            }
        }
    }
}