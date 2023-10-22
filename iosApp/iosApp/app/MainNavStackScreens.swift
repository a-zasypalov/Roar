import sharedLib

enum AddPetFlowScreens: Hashable {
    case addPetFlowType
    case addPetFlowAvatar(type: PetType)
    case addPetFlowForm(type: PetType, avatar: String)
    case addPetFlowSetup(petId: String)
}

struct PetScreen: Hashable {
    let pet: PetWithInteractions
}

struct InteractionScreen: Hashable {
    let interaction: InteractionWithReminders
}
