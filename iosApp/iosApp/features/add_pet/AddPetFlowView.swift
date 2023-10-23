import sharedLib
import SwiftUI

struct AddPetFlowView: View {
    @Binding var navStack: NavigationPath
    @ViewBuilder let label: () -> any View

    @ObservedObject var viewModel: AddPetFlowState

    init(navStack: Binding<NavigationPath>, label: @escaping () -> any View) {
        viewModel = AddPetFlowState(navStack: navStack)
        _navStack = navStack
        self.label = label
    }

    var body: some View {
        Button(action: {
            navStack.append(AddPetFlowScreens.addPetFlowType)
        }) {
            AnyView(label())
        }
        .navigationDestination(for: AddPetFlowScreens.self) { screen in
            switch screen {
            case .addPetFlowType: AddPetTypeView { petType in
                    viewModel.initPetForm(petType: petType.name, localeCode: Locale.current.language.languageCode?.identifier ?? "en", noBreedString: "No breed")
                    navStack.append(AddPetFlowScreens.addPetFlowAvatar(type: petType))
                }
            case .addPetFlowAvatar(let petType): AddPetAvatarView(petType: petType) { avatar in
                    navStack.append(AddPetFlowScreens.addPetFlowForm(type: petType, avatar: avatar))
                }
            case .addPetFlowForm(let petType, let avatar): AddPetFormView(petType: petType, petAvatar: avatar, breeds: viewModel.screenState.breeds) { event in
                    viewModel.createNewPet(event: event)
                }
            case .addPetFlowSetup(let petId): AddPetSetupView(petId: petId) {
                    navStack.removeLast(4)
                }
            }
        }
    }
}
