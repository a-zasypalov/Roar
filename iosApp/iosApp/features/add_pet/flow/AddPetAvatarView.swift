import SwiftUI

struct AddPetAvatarView: View {
    @Binding var path: [AddPetFlowScreens]

    var body: some View {
        HStack {
            Button(action: { path.append(.form) }) {
                Text("Next")
            }
        }
        .navigationTitle("Avatar")
        .navigationBarTitleDisplayMode(.large)
    }
}
