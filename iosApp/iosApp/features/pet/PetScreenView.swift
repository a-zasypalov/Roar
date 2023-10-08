import sharedLib
import SwiftUI

struct PetScreenView: View {
    @ObservedObject var viewState: PetScreenState
    @State private var navStack: [MainNavStackScreens] = []
    @State private var pet: PetWithInteractions
    
    init() {
        viewState = PetScreenState()
    }
    
    var body: some View {
        NavigationStack(path: $navStack) {
          ProgressView()
        }.popover(isPresented: $viewState.presentEditPet) {
           
        }
        .confirmationDialog("", isPresented: $viewState.presentDeletePetDialog) {
            
        }
    }
}
