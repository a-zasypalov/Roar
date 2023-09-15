import sharedLib
import SwiftUI

struct AddPetAvatarView: View {
    @Binding var petType: PetType
    let onAvatarChosen: (String) -> Void

    let columns = [
        GridItem(.flexible(), spacing: 16),
        GridItem(.flexible(), spacing: 16),
        GridItem(.flexible(), spacing: 16),
    ]

    var body: some View {
        ScrollView {
            LazyVGrid(columns: columns, alignment: .center, spacing: 16) {
                ForEach(1 ..< 51) { index in
                    let avatarName = "ic_\(petType.name.lowercased())_\(index)"

                    CardIconButton(image: avatarName, iconSize: .auto)
                        { onAvatarChosen(avatarName) }
                }
            }
            .padding()
        }
        .navigationLargeTitle(title: "Avatar")
        .expanded()
        .screenBackground(color: Color(.systemGroupedBackground))
    }
}

#Preview {
    AddPetAvatarView(petType: .constant(.cat)) { _ in }
}
