import sharedLib
import SwiftUI

struct AddPetAvatarView: View {
    let petType: PetType
    let onAvatarChosen: (String) -> Void

    let columns = [
        GridItem(.flexible()),
        GridItem(.flexible()),
        GridItem(.flexible()),
    ]

    var body: some View {
        ScrollView {
            LazyVGrid(columns: columns, alignment: .center, spacing: 8) {
                ForEach(1 ..< 51) { index in
                    let avatarName = "ic_\(petType.name.lowercased())_\(index)"

                    CardIconButton(image: avatarName, iconSize: .small) {
                        onAvatarChosen(avatarName)
                    }
                }
            }
            .padding()
        }
        .navigationLargeTitle(title: "Avatar")
        .expanded()
        .screenBackground(color: Color(.systemGroupedBackground))
    }
}

struct AddPetAvatarView_Previews: PreviewProvider {
    static var previews: some View {
        AddPetAvatarView(petType: .cat) { _ in }
    }
}
