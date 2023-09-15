import sharedLib
import SwiftUI

struct AddPetTypeView: View {
    let onTypeChosen: (PetType) -> Void

    var body: some View {
        HStack {
            Spacer()

            CardIconButton(
                image: "ic_cat",
                iconSize: .medium
            ) { onTypeChosen(PetType.cat) }

            Spacer()

            CardIconButton(
                image: "ic_dog",
                iconSize: .medium
            ) { onTypeChosen(PetType.dog) }

            Spacer()
        }
        .navigationLargeTitle(title: "Who is it?")
        .expanded()
        .screenBackground(color: Color(.systemGroupedBackground))
    }
}

#Preview {
    AddPetTypeView(onTypeChosen: { _ in })
}
