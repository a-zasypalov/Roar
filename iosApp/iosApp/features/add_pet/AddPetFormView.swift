import SwiftUI

struct AddPetFormView: View {
    @Binding var path: [AddPetFlowScreens]

    var body: some View {
        HStack {
            Button(action: { path.append(.setup) }) {
                Text("Finish")
            }
        }.navigationTitle("Pet data")
    }
}
