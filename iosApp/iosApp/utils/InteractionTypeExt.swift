import sharedLib

extension InteractionType {
    func imageName() -> String {
        let imageName = switch self {
        case .deworming: "drops"
        case .bathing: "soap"
        case .flees: "drops"
        case .grooming: "grooming"
        case .healthCheck: "stethoscope"
        case .pills: "pills"
        case .vaccination: "vaccine"
        case .custom: "paw"
        case .nails: "nails"
        default: "paw"
        }

        return imageName
    }
}
