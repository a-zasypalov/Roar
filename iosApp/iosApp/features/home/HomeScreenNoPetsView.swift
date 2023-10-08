import sharedLib
import SwiftUI

struct HomeScreenNoPetsView: View {
    let user: User
    @Binding var navStack: [MainNavStackScreens]

    var body: some View {
        NavigationStack(path: $navStack) {
            Spacer()
            Text("Hey, \(user.name). Let's add the first pet!")
            Spacer()

            AddPetFlowView(navStack: navStack)
        }
    }
}
