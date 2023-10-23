import sharedLib
import SwiftUI

struct AddInteractionFlowView: View {
    @Binding var navStack: NavigationPath
    @ViewBuilder let label: () -> any View
    let petId: String

    @ObservedObject var viewModel: AddPetFlowState

    init(navStack: Binding<NavigationPath>, petId: String, label: @escaping () -> any View) {
        viewModel = AddPetFlowState(navStack: navStack)
        _navStack = navStack
        self.label = label
        self.petId = petId
    }

    var body: some View {
        Button(action: {
            navStack.append(AddInteractionFlowScreens.interactionTemplates(petId: petId))
        }) {
            AnyView(label())
        }
        .navigationDestination(for: AddInteractionFlowScreens.self) { screen in
            switch screen {
            case .interactionTemplates: TemplatesListScreenView(petId: "") { templateId in
                navStack.append(AddInteractionFlowScreens.interactionForm(templateId: templateId))
            }
            case .interactionForm(let templateId): Text(templateId)
            }
        }
    }
}
