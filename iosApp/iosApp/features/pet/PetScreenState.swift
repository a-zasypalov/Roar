import Firebase
import Foundation
import GoogleSignIn
import sharedLib
import SwiftUI

class PetScreenState: ObservableObject {
    let viewModel = ViewModelProvider().petScreenViewModel()

    @Published var screenState: PetScreenContract.State
    @Published var presentEditPet: Bool = false
    @Published var presentDeletePetDialog: Bool = false
    
    @Published var petPreview: PetWithInteractions
    @Binding var navStack: NavigationPath

    init(pet: PetWithInteractions, navStack: Binding<NavigationPath>) {
        self._navStack = navStack
        self.petPreview = pet
        screenState = viewModel.setInitialState()
        viewModel.observeViewState { state in
            self.screenState = state
        }
        viewModel.observeEffect { effect in
            switch effect {
            case is PetScreenContract.EffectNavigationToEditPet: self.presentEditPet = true
            case is PetScreenContract.EffectNavigationToInteractionDetails: break
            case is PetScreenContract.EffectNavigationToInteractionTemplates: break
            case is PetScreenContract.EffectNavigateBack: self.navStack.removeLast()
            default: break
            }
        }
        
        if isNotPreview {
            viewModel.buildScreenState(petId: pet.id)
        }
    }
    
    func deletePet() {
        viewModel.setEvent(event: PetScreenContract.EventOnDeletePetConfirmed(petId: petPreview.id))
    }
    
    func openInteraction(interaction: InteractionWithReminders) {
        navStack.append(InteractionScreen(interaction: interaction))
    }

    deinit {
        viewModel.dispose()
    }
}
