import sharedLib
import SwiftUI

struct HomeScreenNoPetsView: View {
    let user: User
    @Binding var navStack: NavigationPath

    @State private var petName = ""
    @State private var petType = PetType.cat
    @State private var avatar = "ic_cat"

    var body: some View {
        Spacer()
        Text("Hey, \(user.name). Let's add the first pet!")
        Spacer()
        AddPetFlowView(navStack: $navStack) {
            Text("Add first pet")
                .mainActionButtonStyle()
        }
        .padding()
        .buttonStyle(.borderedProminent)
    }
}
