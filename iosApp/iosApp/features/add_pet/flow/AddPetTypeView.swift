import SwiftUI

struct AddPetTypeView: View {
    @Binding var path: [AddPetFlowScreens]

    var body: some View {
        HStack {
            Spacer()
            
            CardIconButton(
                tapAction: {  path.append(.avatar) },
                image: "ic_cat",
                iconSize: .medium
            )
            
            Spacer()
            
            CardIconButton(
                tapAction: {  path.append(.avatar) },
                image: "ic_dog",
                iconSize: .medium
            )
            
            Spacer()
        }
        .navigationLargeTitle(title: "Who is it?")
        .expanded()
        .screenBackground(color: Color(.systemGroupedBackground))
    }
}

struct AddPetType_Previews: PreviewProvider {
    static var previews: some View {
        AddPetTypeView(path: .constant([]))
    }
}
