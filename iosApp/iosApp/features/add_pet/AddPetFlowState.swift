import Firebase
import Foundation
import GoogleSignIn
import sharedLib
import SwiftUI

class AddPetFlowState: ObservableObject {
    let viewModel = ViewModelProvider().addPetDataScreenViewModel()

    @Binding var navStack: NavigationPath
    @Published var screenState: AddPetDataScreenContract.State

    init(navStack: Binding<NavigationPath>) {
        self._navStack = navStack
        screenState = viewModel.setInitialState()
        viewModel.observeViewState { state in
            self.screenState = state
        }
        viewModel.observeEffect { effect in
            if effect is AddPetDataScreenContract.EffectNavigationToPetSetup {
                guard let petId = (effect as? AddPetDataScreenContract.EffectNavigationToPetSetup)?.petId else { return }
                self.navStack.append(AddPetFlowScreens.addPetFlowSetup(petId: petId))
            }
        }
    }

    func createNewPet(event: AddPetDataScreenContract.EventAddPetButtonClicked) {
        viewModel.handleEvents(event: event)
    }

    deinit {
        viewModel.dispose()
    }
}
