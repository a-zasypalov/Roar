import sharedLib
import SwiftUI

struct AddPetFlowView: View {
    @Binding var navStack: [MainNavStackScreens.AddPetFlow]

    @State private var petName = ""
    @State private var petType = PetType.cat
    @State private var avatar = "ic_cat"

    init(
        navStack: [MainNavStackScreens],
        petName: String = "",
        petType: PetType = PetType.cat,
        avatar: String = "ic_cat"
    ) {
        self.navStack = navStack
        self.petName = petName
        self.petType = petType
        self.avatar = avatar
    }

    var body: some View {
        NavigationLink(value: MainNavStackScreens.AddPetFlow.addPetFlowType) {
            Image(systemName: "plus")
        }
        .navigationDestination(for: MainNavStackScreens.AddPetFlow.self) { screen in
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
