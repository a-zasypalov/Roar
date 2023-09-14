import sharedLib
import SwiftUI

struct HomeScreenNoPetsView: View {
    let user: User
    @State private var addPetPath: [AddPetFlowScreens] = []

    var body: some View {
        NavigationStack(path: $addPetPath) {
            Spacer()
            Text("Hey, \(user.name). Let's add the first pet!")
            Spacer()

            AddPetFlowView(addPetPath: $addPetPath)
        }
    }
}
