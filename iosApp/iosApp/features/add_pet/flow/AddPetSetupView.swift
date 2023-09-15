import SwiftUI

struct AddPetSetupView: View {
    @Binding var path: [AddPetFlowScreens]

    var body: some View {
        HStack {
            Button(action: { path.removeAll() }) {
                Text("Finish")
            }
        }
        .navigationTitle("YaY!")
        .navigationBarTitleDisplayMode(.large)
    }
}
