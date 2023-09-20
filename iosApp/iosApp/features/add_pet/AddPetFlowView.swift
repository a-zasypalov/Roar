import sharedLib
import SwiftUI

struct AddPetFlowView: View {
    @Binding var navStack: [MainNavStackScreens]

    @State var petName = ""
    @State var petType = PetType.cat
    @State var avatar = "ic_cat"

    var body: some View {
        NavigationLink(value: MainNavStackScreens.addPetFlowType) {
            Image(systemName: "plus")
        }
        .navigationDestination(for: MainNavStackScreens.self) { screen in
            switch screen {
            case .addPetFlowType: AddPetTypeView { petType in
                    self.petType = petType
                    navStack.append(.addPetFlowAvatar)
                }
            case .addPetFlowAvatar: AddPetAvatarView(petType: $petType) { avatar in
                    self.avatar = avatar
                    navStack.append(.addPetFlowForm)
                }
            case .addPetFlowForm: AddPetFormView(petType: $petType, petAvatar: $avatar) { name in
                    self.petName = name
                    navStack.append(.addPetFlowSetup)
                }
            case .addPetFlowSetup: AddPetSetupView(petName: $petName, petAvatar: $avatar) {
                    navStack.removeAll()
                }
            }
        }
    }
}
