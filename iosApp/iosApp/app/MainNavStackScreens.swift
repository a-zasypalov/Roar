import sharedLib

enum MainNavStackScreens: Hashable {
    case petScreen(pet: PetWithInteractions)
    
    case addPetFlowType
    case addPetFlowAvatar
    case addPetFlowForm
    case addPetFlowSetup
}
