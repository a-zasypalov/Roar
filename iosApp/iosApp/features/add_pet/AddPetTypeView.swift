import SwiftUI

struct AddPetTypeView: View {
    @Binding var path: [AddPetFlowScreens]

    var body: some View {
        HStack {
            Button(action: { path.append(.avatar) }) {
                Text("Next")
            }
        }.navigationTitle("Who is it?")
    }
}
