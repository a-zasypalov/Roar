import sharedLib
import SwiftUI

struct AddPetFlowView: View {
    @Binding var navStack: [MainNavStackScreens]

    @State private var petName = ""
    @State private var petType = PetType.cat
    @State private var avatar = "ic_cat"

    var body: some View {
        Button(action: {
            navStack.append(.addPetFlowType)
        }) {
            Image(systemName: "plus")
        }
//        .navigationDestination(for: MainNavStackScreens.self) { screen in
//            switch screen {
//            case .addPetFlowType: AddPetTypeView { petType in
//                    self.petType = petType
//                    navStack.append(.addPetFlowAvatar)
//                }
//            case .addPetFlowAvatar: AddPetAvatarView(petType: $petType) { avatar in
//                    self.avatar = avatar
//                    navStack.append(.addPetFlowForm)
//                }
//            case .addPetFlowForm: AddPetFormView(petType: $petType, petAvatar: $avatar) { name in
//                    self.petName = name
//                    navStack.append(.addPetFlowSetup)
//                }
//            case .addPetFlowSetup: AddPetSetupView(petName: $petName, petAvatar: $avatar) {
//                    navStack.removeAll()
//                }
//            default: ProgressView()
//            }
//        }
    }
}
