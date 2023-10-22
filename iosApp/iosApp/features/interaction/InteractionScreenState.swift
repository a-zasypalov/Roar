import Firebase
import Foundation
import GoogleSignIn
import sharedLib
import SwiftUI

class InteractionScreenState: ObservableObject {
    let viewModel = ViewModelProvider().interactionScreenViewModel()

    @Published var screenState: InteractionScreenContract.State
    @Published var presentEditInteraction: Bool = false
    @Published var presentDeleteInteractionDialog: Bool = false
    @Published var presentDeleteReminderFromHistoryDialog: Bool = false

    @Published var interactionPreview: InteractionWithReminders
    @Binding var navStack: NavigationPath
    @Published var notes: String

    init(interaction: InteractionWithReminders, navStack: Binding<NavigationPath>) {
        self._navStack = navStack
        self.interactionPreview = interaction
        self.notes = interaction.notes

        self.screenState = viewModel.setInitialState()
        viewModel.observeViewState { state in
            self.screenState = state
        }
        viewModel.observeEffect { effect in
            switch effect {
            case is InteractionScreenContract.EffectNavigationToEditInteraction: self.presentEditInteraction = true
            case is InteractionScreenContract.EffectShowRemoveInteractionDialog: self.presentDeleteInteractionDialog = true
            case is InteractionScreenContract.EffectShowRemoveReminderFromHistoryDialog: self.presentDeleteReminderFromHistoryDialog = true
            case is InteractionScreenContract.EffectNavigateBack: self.navStack.removeLast()
            default: break
            }
        }

        if isNotPreview {
            viewModel.buildScreenState(interactionId: interaction.id)
        }
    }

    func deleteInteraction() {
        // viewModel.setEvent(event: InteractionScreenContract.EventOnActivateButtonClick(interactionId: <#T##String#>, activate:)
    }

    deinit {
        viewModel.dispose()
    }
}
