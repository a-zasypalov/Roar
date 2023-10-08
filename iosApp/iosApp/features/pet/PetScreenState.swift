import Firebase
import Foundation
import GoogleSignIn
import sharedLib

class PetScreenState: ObservableObject {
    let viewModel = ViewModelProvider().petScreenViewModel()

    @Published var screenState: PetScreenContract.State
    @Published var presentEditPet: Bool = false
    @Published var presentDeletePetDialog: Bool = false

    init() {
        screenState = viewModel.setInitialState()
        viewModel.observeViewState { state in
            self.screenState = state
        }
        viewModel.observeEffect { effect in
            switch effect {
            case is PetScreenContract.EffectNavigationToEditPet: self.presentEditPet = true
            case is PetScreenContract.EffectNavigationToInteractionDetails: break
            case is PetScreenContract.EffectNavigationToInteractionTemplates: break
            default: break
            }
        }
    }

    deinit {
        viewModel.dispose()
    }
}
