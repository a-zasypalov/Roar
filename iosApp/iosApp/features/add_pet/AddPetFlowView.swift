import SwiftUI

struct AddPetFlowView: View {
    @Binding var addPetPath: [AddPetFlowScreens]
    
    var body: some View {
        NavigationLink(value: AddPetFlowScreens.type) {
            Text("Add Pet")
        }
        .navigationDestination(for: AddPetFlowScreens.self) { screen in
            switch screen {
            case .type: AddPetTypeView(path: $addPetPath)
            case .avatar: AddPetAvatarView(path: $addPetPath)
            case .form: AddPetFormView(path: $addPetPath)
            case .setup: AddPetSetupView(path: $addPetPath)
            }
        }
    }
}
