import sharedLib
import SwiftUI

struct AddPetFlowView: View {
    @Binding var addPetPath: [AddPetFlowScreens]

    @State var petName = ""
    @State var petType = PetType.cat
    @State var avatar = "ic_cat"

    var body: some View {
        NavigationLink(value: AddPetFlowScreens.type) {
            Text("Add Pet")
        }
        .navigationDestination(for: AddPetFlowScreens.self) { screen in
            switch screen {
            case .type: AddPetTypeView { petType in
                    self.petType = petType
                    addPetPath.append(.avatar)
                }
            case .avatar: AddPetAvatarView(petType: $petType) { avatar in
                    self.avatar = avatar
                    addPetPath.append(.form)
                }
            case .form: AddPetFormView(petType: $petType, petAvatar: $avatar) { name in
                    self.petName = name
                    addPetPath.append(.setup)
                }
            case .setup: AddPetSetupView(petName: $petName, petAvatar: $avatar) {
                    addPetPath.removeAll()
                }
            }
        }
    }
}
