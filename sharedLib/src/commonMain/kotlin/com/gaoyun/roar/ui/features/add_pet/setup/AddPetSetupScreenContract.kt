package com.gaoyun.roar.ui.features.add_pet.setup

class AddPetSetupScreenContract {
    sealed class Event {
        data object ContinueButtonClicked : Event()
        data object OpenTemplatesButtonClicked : Event()
    }
}