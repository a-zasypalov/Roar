import sharedLib
import SwiftUI

struct PetScreenView: View {
    @ObservedObject var viewState: PetScreenState
    @Binding private var navStack: [MainNavStackScreens]
    @State private var pet: PetWithInteractions
    
    init(navStack: Binding<[MainNavStackScreens]>, pet: PetWithInteractions) {
        viewState = PetScreenState()
        self.pet = pet
        self._navStack = navStack
    }
    
    var body: some View {
        NavigationView {
            Button(action: {
                navStack.append(.addPetFlowType)
            }) {
                Text("Test pet")
            }
        }
        .navigationLargeTitle(title: pet.name)
//        .popover(isPresented: $viewState.presentEditPet) {
//           
//        }
//        .confirmationDialog("", isPresented: $viewState.presentDeletePetDialog) {
//            
//        }
    }
}
